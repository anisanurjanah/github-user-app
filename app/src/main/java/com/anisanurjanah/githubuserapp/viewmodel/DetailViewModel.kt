package com.anisanurjanah.githubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anisanurjanah.githubuserapp.data.remote.response.DetailUserResponse
import com.anisanurjanah.githubuserapp.data.repository.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val repository: Repository) : ViewModel() {
    private val _githubUserDetail= MutableLiveData<DetailUserResponse>()
    val githubUserDetail: LiveData<DetailUserResponse> =_githubUserDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun findGithubUserDetail(username: String){
        _isLoading.value=true
        val client= repository.getGithubUserDetail(username)
        client.enqueue(object: Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value=false
                if (response.isSuccessful){
                    _githubUserDetail.value=response.body()
                } else {
                    Log.e(TAG,"onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t:Throwable){
                _isLoading.value=false
                Log.e(TAG,"onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object{
        private const val TAG = "DetailViewModel"
    }
}