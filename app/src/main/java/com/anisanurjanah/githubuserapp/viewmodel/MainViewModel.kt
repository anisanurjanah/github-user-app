package com.anisanurjanah.githubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anisanurjanah.githubuserapp.data.remote.response.GithubResponse
import com.anisanurjanah.githubuserapp.data.remote.response.ItemsItem
import com.anisanurjanah.githubuserapp.data.repository.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: Repository) : ViewModel() {
    private val _githubUser = MutableLiveData<List<ItemsItem>>()
    val githubUser: LiveData<List<ItemsItem>> = _githubUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        findGithubUser()
    }

    fun findUserByUsername(name: String) {
        findGithubUser(name)
    }

    private fun findGithubUser(query: String = "") {
        _isLoading.value = true
        val client = if (query.isEmpty()) {
            repository.getGithubUser(GITHUB_USERNAME)
        } else {
            repository.getGithubUser(query)
        }
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _githubUser.value = response.body()?.items
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object{
        private const val TAG = "MainViewModel"
        private const val GITHUB_USERNAME = "arif"
    }
}