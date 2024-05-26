package com.example.androidfundamental.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.androidfundamental.R
import com.example.androidfundamental.databinding.ActivityDetailBinding
import com.example.androidfundamental.ui.detail.SectionAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailCardActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_AVATAR = "EXTRA_AVATAR"
        const val EXTRA_USERNAME = "extra_username"
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val bundle = Bundle().apply {
            putString(EXTRA_USERNAME, username)
        }

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java) // Ubah sesuai nama yang benar
        username?.let {
            viewModel.setUserDetail(it)
            viewModel.getUserDetail().observe(this) { user ->
                user?.let {
                    with(binding) {
                        tvUsername.text = user.login
                        itemName.text = user.name
                        followers.text = "${user.followers} Followers"
                        following.text = "${user.following} Following"
                        Glide.with(this@DetailCardActivity)
                            .load(user.avatar_url)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .centerCrop()
                            .into(profilePict)
                        showLoading(false)
                    }
                }
            }

            showLoading(true)
            favoriteCheck()

            val sectionAdapter = SectionAdapter(this, supportFragmentManager, bundle)
            binding.apply {
                pager.adapter = sectionAdapter
                tabs.setupWithViewPager(pager)
            }
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun favoriteCheck(){

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra(EXTRA_AVATAR)

        var _isCheked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main){
                if(count != null){
                    if(count > 0){
                        _isCheked = true
                    }
                    binding.toggleFavorite.isChecked = _isCheked
                    if (_isCheked) {
                        binding.toggleFavorite.background =
                            ContextCompat.getDrawable(applicationContext, R.drawable.favorite_full)
                    } else {
                        binding.toggleFavorite.background =
                            ContextCompat.getDrawable(applicationContext, R.drawable.favorite_blank)
                    }
                }
            }
        }
        binding.toggleFavorite.setOnClickListener{
            _isCheked = !_isCheked
            binding.toggleFavorite.isChecked = _isCheked
            if(_isCheked){
                if (username != null) {
                    if (avatarUrl != null) {
                        viewModel.addFavorite(username,id, avatarUrl)
                    }
                }
                binding.toggleFavorite.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.favorite_full)
            }else{
                viewModel.removeFavorite(id)
                binding.toggleFavorite.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.favorite_blank)
            }
        }
    }
}
