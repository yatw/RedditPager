package com.example.redditpager.api

import android.content.Context
import com.example.redditpager.BuildConfig
import com.kirkbushman.auth.RedditAuth
import com.kirkbushman.auth.managers.SharedPrefsStorageManager
import com.kirkbushman.auth.models.TokenBearer
import java.lang.IllegalStateException


class AuthHelper(context: Context){

    private val auth by lazy {

        RedditAuth.Builder()
            .setUserlessCredentials(
                clientId = BuildConfig.reddit_clientId,
                deviceId = null
            )
            // the api endpoints scopes this client will need
            .setScopes("read")
            .setStorageManager(SharedPrefsStorageManager(context))
            .setLogging(false)
            .build()
    }

    @Volatile
    private var INSTANCE: RedditClient? = null


    /**
     * If this return null, we should redirect user to signIn
     */
    fun getClient(): RedditClient {

        // only create INSTANCE is not exist to ensure there is only 1 instance
        return INSTANCE ?: synchronized(this) {
            val tokenBearer: TokenBearer = auth.getTokenBearer()?: throw IllegalStateException("Error in library")
            INSTANCE = RedditClient(tokenBearer)
            return INSTANCE!!
        }
    }
}
