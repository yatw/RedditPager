package com.example.redditpager.paging3

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
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
                pageSize = 15,
                enablePlaceholders = true,
                maxSize = 200
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
                enablePlaceholders = false,
                maxSize = 20
            ),
            remoteMediator = PostMediator(subReddit, client, database),
            pagingSourceFactory = { database.postDao().getPosts()}
        ).flow

    }
}