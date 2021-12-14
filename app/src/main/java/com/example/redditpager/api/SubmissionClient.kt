package com.example.redditpager.api

import androidx.annotation.WorkerThread
import com.example.redditpager.models.EnvelopedSubmissionListing
import java.lang.IllegalStateException


class SubmissionClient(
    private val api: RedditApi,
    private inline val getHeader: () -> Map<String, String>
) {

    private var loaded: Long = 0 // how many submission has loaded

    @WorkerThread
    fun getSubmission(
        subreddit: String,
        limit: Long,
        previousToken: String?,
        nextToken: String?
    ): EnvelopedSubmissionListing {

        val req = api.fetchSubmissions(
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


        val res = req.execute()

        if (!res.isSuccessful) {
            throw IllegalStateException("Unsuccessful API call")
        }

        val body = res.body() ?: throw IllegalStateException("Null body response")
        loaded += limit
        return body
    }
}
