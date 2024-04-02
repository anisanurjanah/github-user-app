package com.anisanurjanah.githubuserapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anisanurjanah.githubuserapp.data.local.entity.FavoriteUser

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_user")
    fun getAllFavorites(): LiveData<List<FavoriteUser>>
    @Query("SELECT * FROM favorite_user WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favorite: FavoriteUser)
    @Delete
    fun delete(favorite: FavoriteUser)
}