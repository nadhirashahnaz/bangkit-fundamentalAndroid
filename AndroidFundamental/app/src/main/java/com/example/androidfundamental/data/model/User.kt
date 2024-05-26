package com.example.androidfundamental.data.model

import com.google.gson.annotations.SerializedName

data class User(
    val login: String? = null,
    val id: Int? = null,
    @field:SerializedName("avatar_url")
    val avatarUrl: String? = null,
    val followersUrl: String? = null,
    val followingUrl: String? = null,
    )