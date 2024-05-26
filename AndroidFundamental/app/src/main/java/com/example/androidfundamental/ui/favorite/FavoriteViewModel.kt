package com.example.androidfundamental.ui.favorite

import com.example.androidfundamental.data.model.FavoriteUser
import com.example.androidfundamental.data.model.FavoriteUserDao
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.androidfundamental.data.model.UserDatabase

class FavoriteViewModel(application: Application): AndroidViewModel(application) {
    private var userDao: FavoriteUserDao?
    private var userDatabase : UserDatabase?

    init{
        userDatabase = UserDatabase.getDatabase(application)
        userDao = userDatabase?.favoriteUserDao()
    }

    fun getFavorite(): LiveData<List<FavoriteUser>>?{
        return userDao?.getFavorite()
    }
}