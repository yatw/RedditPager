package com.example.redditpager.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post_keys")
data class PostKeys(
    @PrimaryKey val postId: Long,
    val prevKey: String?,
    val nextKey: String?
)
