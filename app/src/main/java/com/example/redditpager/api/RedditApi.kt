package com.example.redditpager.api

import com.example.redditpager.models.EnvelopedSubmissionListing
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path
import retrofit2.http.Query


interface RedditApi {

    @GET("/r/{subreddit}/{sorting}/.json")
    suspend fun fetchSubmissions(
        @Path("subreddit") subreddit: String,
        @Path("sorting") sorting: String,
        @Query("t") timePeriod: String?,
        @Query("limit") limit: Int,
        @Query("count") count: Int,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,
        @Query("raw_json") rawJson: Int? = null,
        @HeaderMap header: Map<String, String>
    ): EnvelopedSubmissionListing

}