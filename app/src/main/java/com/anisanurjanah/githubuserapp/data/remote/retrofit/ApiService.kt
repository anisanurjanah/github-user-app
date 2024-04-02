package com.anisanurjanah.githubuserapp.data.remote.retrofit

import com.anisanurjanah.githubuserapp.data.remote.response.DetailUserResponse
import com.anisanurjanah.githubuserapp.data.remote.response.GithubResponse
import com.anisanurjanah.githubuserapp.data.remote.response.ItemsItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getGithubUser(@Query("q") query: String): Call<GithubResponse>
    @GET("users/{username}")
    fun getGithubUserDetail(@Path("username") username: String): Call<DetailUserResponse>
    @GET("users/{username}/followers")
    fun getGithubUserFollowers(@Path("username") username: String): Call<List<ItemsItem>>
    @GET("users/{username}/following")
    fun getGithubUserFollowing(@Path("username") username: String): Call<List<ItemsItem>>
}