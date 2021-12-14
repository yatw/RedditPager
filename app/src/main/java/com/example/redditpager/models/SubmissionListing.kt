package com.example.redditpager.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SubmissionListing(

    @Json(name = "modhash")
    val modhash: String?,
    @Json(name = "dist")
    val dist: Int?,

    @Json(name = "children")
    val envelopedSubmissions: List<EnvelopedSubmission>,

    @Json(name = "after")
    val after: String?,
    @Json(name = "before")
    val before: String?

)