package com.example.redditpager.api

import androidx.annotation.WorkerThread
import com.kirkbushman.auth.models.TokenBearer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class RedditClient @JvmOverloads constructor (private val bearer: TokenBearer) {

    companion object {

        private var api: RedditApi? = null

        @Synchronized
        fun getApi(): RedditApi {
            return synchronized(this) {

                if (api == null) {
                    // logging Http request detail, may delete in prod environment, but useful for dev
                    val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(
                        HttpLoggingInterceptor.Level.BODY)
                    val client: OkHttpClient = OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .build()

                    api = Retrofit.Builder()
                        .baseUrl("https://oauth.reddit.com")
                        .client(client)
                        .addConverterFactory(MoshiConverterFactory.create())
                        .build()
                        .create(RedditApi::class.java)
                }

                api!!
            }
        }

    }

    private val redditApi by lazy { getApi() }

    val submissionClient by lazy { SubmissionClient(redditApi, ::getHeaderMap) }

    private fun getHeaderMap(): Map<String, String> {
        return mapOf("Authorization" to "bearer ".plus(bearer.getAccessToken()))
    }
}