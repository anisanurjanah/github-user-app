package com.anisanurjanah.githubuserapp.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite_user")
@Parcelize
data class FavoriteUser(
    @PrimaryKey(autoGenerate = false)

    @field:ColumnInfo(name = "username")
    var username: String = "",

    @field:ColumnInfo(name = "avatar")
    var avatarUrl: String? = null
) : Parcelable