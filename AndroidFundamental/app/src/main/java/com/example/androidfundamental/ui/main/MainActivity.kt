package com.example.androidfundamental.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.androidfundamental.data.model.User
import com.example.androidfundamental.databinding.ActivityMainBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidfundamental.R
import com.example.androidfundamental.ui.favorite.FavoriteActivity

class MainActivity : AppCompatActivity() {
    companion object {
        internal const val TAG = "MainActivity"
        private const val DEFAULT_SEARCH_QUERY = "nadhira"
        private const val PREFS_NAME = "MyPrefs"
        private const val DARK_MODE_PREF = "darkModePref"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private var currentSearchQuery: String = DEFAULT_SEARCH_QUERY
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isDarkModeEnabled = sharedPref.getBoolean(DARK_MODE_PREF, false)
        setDarkMode(isDarkModeEnabled)

        viewModel.loading.observe(this, Observer { isLoading ->
            showLoading(isLoading)
        })

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager

        viewModel.findUser(DEFAULT_SEARCH_QUERY, this::setUserData) { errorMessage ->
            Log.e(TAG, "onFailure: $errorMessage")
            // Handle failure
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                currentSearchQuery = query // Simpan query yang baru
                viewModel.findUser(query, this@MainActivity::setUserData) { errorMessage ->
                    Log.e(TAG, "onFailure: $errorMessage")
                    // Handle failure
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        // Menambahkan switch untuk dark mode
        val switchDarkModeItem = menu.findItem(R.id.switch_dark_mode)
        val switchDarkMode = switchDarkModeItem.actionView as Switch
        switchDarkMode.isChecked = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            setDarkMode(isChecked)
            sharedPref.edit().putBoolean(DARK_MODE_PREF, isChecked).apply()
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                // Handle favorite action
                Intent(this, FavoriteActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUserData(userList: List<User>) {
        val listUser = ArrayList<User>()

        for (user in userList) {
            user.login?.let { listUser.add(user) }
        }

        val adapter = UserAdapter(listUser) { item ->
            val intent = Intent(this, DetailCardActivity::class.java)
            showLoading(true)
            intent.putExtra(DetailCardActivity.EXTRA_USERNAME, item.login)
            intent.putExtra(DetailCardActivity.EXTRA_ID, item.id)
            intent.putExtra(DetailCardActivity.EXTRA_AVATAR, item.avatarUrl)
            startActivity(intent)
            showLoading(false)

        }

        binding.rvUser.adapter = adapter
    }


    fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
    private fun setDarkMode(isDarkModeEnabled: Boolean) {
        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}
