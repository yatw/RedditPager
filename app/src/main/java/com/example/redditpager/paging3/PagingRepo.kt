package com.example.redditpager.paging3

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.redditpager.api.SubmissionClient
import com.example.redditpager.models.EnvelopedSubmission
import kotlinx.coroutines.flow.Flow

class PagingRepository(private val client: SubmissionClient) {


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
}