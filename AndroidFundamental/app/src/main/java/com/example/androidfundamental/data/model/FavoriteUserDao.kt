package com.example.androidfundamental.data.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteUserDao {
    @Insert
    fun addFavorite(favoriteUser: FavoriteUser)

    @Query("SELECT * FROM favorite_user")
    fun getFavorite(): LiveData<List<FavoriteUser>>

    @Query("DELETE FROM favorite_user where favorite_user.id = :id")
    fun removeFavorite(id: Int): Int

    @Query("SELECT count(*) FROM favorite_user where favorite_user.id = :id")
    fun checkUser(id: Int):Int

}