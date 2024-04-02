package com.anisanurjanah.githubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anisanurjanah.githubuserapp.data.remote.response.ItemsItem
import com.anisanurjanah.githubuserapp.data.repository.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel(private val repository: Repository) : ViewModel() {
    private val _githubUserFollow = MutableLiveData<List<ItemsItem>>()
    val githubUserFollow: LiveData<List<ItemsItem>> = _githubUserFollow

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private fun findGithubUserFollow(client: Call<List<ItemsItem>>) {
        _isLoading.value = true
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _githubUserFollow.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun setUserFollowPosition(username: String, position: Int) {
        if (position == 1) {
            val client = repository.getGithubUserFollowers(username)
            findGithubUserFollow(client)
        } else {
            val client = repository.getGithubUserFollowing(username)
            findGithubUserFollow(client)
        }
    }

    companion object{
        private const val TAG = "FollowViewModel"
    }
}