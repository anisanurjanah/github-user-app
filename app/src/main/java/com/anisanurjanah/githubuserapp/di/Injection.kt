package com.anisanurjanah.githubuserapp.di

import android.content.Context
import com.anisanurjanah.githubuserapp.data.local.room.FavoriteRoomDatabase
import com.anisanurjanah.githubuserapp.data.remote.retrofit.ApiConfig
import com.anisanurjanah.githubuserapp.data.repository.Repository

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()

        val database = FavoriteRoomDatabase.getDatabase(context)
        val favoriteDao = database.favoriteDao()

        return Repository.getInstance(apiService, favoriteDao)
    }
}