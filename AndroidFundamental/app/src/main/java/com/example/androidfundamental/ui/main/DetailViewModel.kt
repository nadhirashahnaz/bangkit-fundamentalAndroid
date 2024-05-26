package com.example.androidfundamental.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidfundamental.api.MyApiConfig
import com.example.androidfundamental.data.model.DetailResponse
import com.example.androidfundamental.data.model.UserDatabase
import com.example.androidfundamental.data.model.FavoriteUser
import com.example.androidfundamental.data.model.FavoriteUserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val _userDetail = MutableLiveData<DetailResponse>()

    private var userDao: FavoriteUserDao?
    private var userDatabase : UserDatabase?

    init{
        userDatabase = UserDatabase.getDatabase(application)
        userDao = userDatabase?.favoriteUserDao()
    }

    fun getUserDetail(): LiveData<DetailResponse> {
        return _userDetail
    }

    fun setUserDetail(username: String) {
        MyApiConfig.getApiService().getUserDetail(username).enqueue(object : Callback<DetailResponse> {
            override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>) {
                if (response.isSuccessful) {
                    _userDetail.postValue(response.body())
                } else {
                    Log.d("DetailUserViewModel", "Failed to fetch user detail: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                Log.e("DetailUserViewModel", "onFailure: ${t.message}", t)
            }
        })
    }

    fun addFavorite(username: String, id: Int, avatarUrl : String){
        CoroutineScope(Dispatchers.IO).launch {
            var user = FavoriteUser(
                username, id, avatarUrl
            )
            userDao?.addFavorite(user)
        }
    }

    fun checkUser(id: Int) = userDao?.checkUser(id)

    fun removeFavorite(id:Int){
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFavorite(id)
        }
    }
}
