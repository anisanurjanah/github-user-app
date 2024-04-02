package com.anisanurjanah.githubuserapp.data.repository

import androidx.lifecycle.LiveData
import com.anisanurjanah.githubuserapp.data.local.entity.FavoriteUser
import com.anisanurjanah.githubuserapp.data.local.room.FavoriteDao
import com.anisanurjanah.githubuserapp.data.remote.retrofit.ApiService
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Repository private constructor(
    private val apiService: ApiService,
    private val favoriteDao: FavoriteDao,
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
) {
    // retrofit
    fun getGithubUser(query: String) = apiService.getGithubUser(query)
    fun getGithubUserDetail(username: String) = apiService.getGithubUserDetail(username)
    fun getGithubUserFollowers(username: String) = apiService.getGithubUserFollowers(username)
    fun getGithubUserFollowing(username: String) = apiService.getGithubUserFollowing(username)


    // room
    fun getAllFavorites(): LiveData<List<FavoriteUser>> = favoriteDao.getAllFavorites()
    fun setFavoriteUser(username: String) = favoriteDao.getFavoriteUserByUsername(username)
    fun insert(favorite: FavoriteUser) {
        executorService.execute { favoriteDao.insert(favorite) }
    }
    fun delete(favorite: FavoriteUser) {
        executorService.execute { favoriteDao.delete(favorite) }
    }

    companion object {
        @Volatile
        private var INSTANCE: Repository? = null

        fun getInstance(apiService: ApiService, favoriteDao: FavoriteDao): Repository = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Repository(apiService, favoriteDao)
        }.also { INSTANCE = it }
    }
}