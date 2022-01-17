package com.example.redditpager.paging3

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.redditpager.api.SubmissionClient
import com.example.redditpager.db.PagerDatabase
import com.example.redditpager.db.Post
import com.example.redditpager.models.EnvelopedSubmission
import com.example.redditpager.remoteMediator.PostMediator
import kotlinx.coroutines.flow.Flow

class PagingRepository(
    private val client: SubmissionClient,
    private val database: PagerDatabase
) {

    fun getSubmissions(subReddit: String)
            : Flow<PagingData<EnvelopedSubmission>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                initialLoadSize = 15,
                enablePlaceholders = true,
                maxSize = 20
            ),
            pagingSourceFactory = { RedditPagingSource(subReddit, client)}
        ).flow
    }

    @ExperimentalPagingApi
    fun getSubmissionsWithMediator(subReddit: String)
            : Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                initialLoadSize = 15,
                enablePlaceholders = false,
                maxSize = 20
            ),
            remoteMediator = PostMediator(subReddit, client, database),
            pagingSourceFactory = { database.postDao().getPosts()}
        ).flow
    }

    fun getSubmissionsWithPager2(): DataSource.Factory<Int, Post> {
        val dataFactory : DataSource.Factory<Int, Post> = database.postDao().getPostsFactory()
        return dataFactory
    }
}