package com.example.redditpager.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<Post>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: Post): Long

    // todo select

    @Query("DELETE FROM post")
    suspend fun clearPosts()
}
