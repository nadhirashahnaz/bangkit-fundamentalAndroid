package com.example.androidfundamental.api

import com.example.androidfundamental.BuildConfig
import com.example.androidfundamental.data.model.DetailResponse
import com.example.androidfundamental.data.model.User
import com.example.androidfundamental.data.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    fun getListUser(
        @Query("q") id: String
    ): Call<UserResponse>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username : String
    ): Call<DetailResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username : String
    ): Call<List<User>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username : String
    ): Call<List<User>>
}
