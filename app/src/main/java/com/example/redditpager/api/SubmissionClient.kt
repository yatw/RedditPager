package com.example.redditpager.api

import androidx.annotation.WorkerThread
import com.example.redditpager.models.EnvelopedSubmissionListing

class SubmissionClient(
    private val api: RedditApi,
    private inline val getHeader: () -> Map<String, String>
) {

    private var loaded: Int = 0 // how many submission has loaded

    @WorkerThread
    suspend fun getSubmission(
        subreddit: String,
        limit: Int,
        previousToken: String?,
        nextToken: String?
    ): EnvelopedSubmissionListing {

        val res = api.fetchSubmissions(
            subreddit = subreddit,
            sorting = "hot",
            timePeriod = "day",
            limit = limit,
            count = loaded,
            before = previousToken,
            after = nextToken,
            rawJson = 1,
            header = getHeader()
        )

        loaded += limit
        return res
    }
}