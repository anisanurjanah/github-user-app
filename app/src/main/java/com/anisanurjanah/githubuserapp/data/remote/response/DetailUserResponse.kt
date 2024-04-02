package com.anisanurjanah.githubuserapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailUserResponse(
    @field:SerializedName("bio")
    val bio: Any,
    @field:SerializedName("created_at")
    val createdAt: String,
    @field:SerializedName("login")
    val login: String,
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("email")
    val email: Any,
    @field:SerializedName("html_url")
    val htmlUrl: String,
    @field:SerializedName("url")
    val url: String,
    @field:SerializedName("followers")
    val followers: Int,
    @field:SerializedName("avatar_url")
    val avatarUrl: String,
    @field:SerializedName("following")
    val following: Int,
    @field:SerializedName("name")
    val name: String,
)