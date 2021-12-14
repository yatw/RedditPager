package com.example.redditpager.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class EnvelopedSubmission(

    @Json(name = "kind")
    val kind: EnvelopeKind,

    @Json(name = "data")
    val submission: Submission
)