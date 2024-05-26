package com.example.androidfundamental.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidfundamental.api.MyApiConfig
import com.example.androidfundamental.data.model.User
import com.example.androidfundamental.data.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> = _loading

     fun findUser(query: String, onSuccess: (List<User>) -> Unit, onFailure: (String) -> Unit){
        _loading.value=true
        val client = MyApiConfig.getApiService().getListUser(query)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _loading.value=false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        onSuccess(responseBody.items)
                    } else {
                        onFailure("Response body is null")
                    }
                } else {
                    onFailure(response.message())
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _loading.value=false
                onFailure(t.message ?: "Unknown error occurred")
            }
        })
    }
}

