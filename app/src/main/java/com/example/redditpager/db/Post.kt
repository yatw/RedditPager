package com.example.redditpager.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "post")
data class Post(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String,
    @SerializedName("ups") val ups: Int
)