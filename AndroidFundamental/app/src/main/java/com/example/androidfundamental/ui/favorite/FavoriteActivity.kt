package com.example.androidfundamental.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidfundamental.data.model.FavoriteUser
import com.example.androidfundamental.data.model.User
import com.example.androidfundamental.databinding.ActivityFavoriteBinding
import com.example.androidfundamental.ui.main.DetailCardActivity
import com.example.androidfundamental.ui.main.UserAdapter

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
        val userAdapter = UserAdapter(ArrayList()) { user ->
            val intent = Intent(this, DetailCardActivity::class.java)
            showLoading(true)
            intent.putExtra(DetailCardActivity.EXTRA_USERNAME, user.login)
            intent.putExtra(DetailCardActivity.EXTRA_ID, user.id)
            intent.putExtra(DetailCardActivity.EXTRA_AVATAR, user.avatarUrl)
            startActivity(intent)
            showLoading(false)
        }
        binding.rvUser.adapter = userAdapter

        favoriteViewModel.getFavorite()?.observe(this, Observer { favoriteUsers ->
            val userList = mapFavoriteUsers(favoriteUsers)
            userAdapter.submitList(userList)
        })

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

    private fun mapFavoriteUsers(favoriteUsers: List<FavoriteUser>): ArrayList<User> {
        val userList = ArrayList<User>()
        for (favoriteUser in favoriteUsers) {
            val user = User(
                favoriteUser.login,
                favoriteUser.id,
                favoriteUser.avatar_url
            )
            userList.add(user)
        }
        return userList
    }
}
