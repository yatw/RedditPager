package com.example.redditpager.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PostKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<PostKeys>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: PostKeys): Long

    @Query("SELECT * FROM post_keys WHERE postId = :id")
    suspend fun remoteKeysId(id: Long): PostKeys?

    @Query("DELETE FROM post_keys")
    suspend fun clearRemoteKeys()
}
