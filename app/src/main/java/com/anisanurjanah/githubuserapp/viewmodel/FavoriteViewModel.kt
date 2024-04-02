package com.anisanurjanah.githubuserapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anisanurjanah.githubuserapp.data.local.entity.FavoriteUser
import com.anisanurjanah.githubuserapp.data.repository.Repository

class FavoriteViewModel(private val repository: Repository) : ViewModel() {
    private val _favorite = MutableLiveData<Boolean>()

    val isFavorite: LiveData<Boolean> = _favorite

    fun getAllFavorite() = repository.getAllFavorites()

    fun isFavoriteUser(username: String) = repository.setFavoriteUser(username)

    fun insert(favorite: FavoriteUser) {
        repository.insert(favorite)
        _favorite.value = true
    }

    fun delete(favorite: FavoriteUser) {
        repository.delete(favorite)
        _favorite.value = true
    }
}