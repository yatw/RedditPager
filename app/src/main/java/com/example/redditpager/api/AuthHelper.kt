package com.example.redditpager.api

import android.content.Context
import androidx.annotation.WorkerThread
import com.example.redditpager.BuildConfig
import com.kirkbushman.auth.RedditAuth
import com.kirkbushman.auth.managers.SharedPrefsStorageManager
import com.kirkbushman.auth.models.TokenBearer
import java.lang.IllegalStateException


object AuthHelper{


    @Volatile
    private var INSTANCE: RedditClient? = null

    @WorkerThread
    fun authenticate(context: Context){

        val auth = RedditAuth.Builder()
            .setUserlessCredentials(
                clientId = BuildConfig.reddit_clientId,
                deviceId = null
            )
            // the api endpoints scopes this client will need
            .setScopes("read")
            .setStorageManager(SharedPrefsStorageManager(context))
            .setLogging(false)
            .build()

        val tokenBearer: TokenBearer = auth.getTokenBearer()?: throw IllegalStateException("Error in library")
        INSTANCE = RedditClient(tokenBearer)
    }

    /**
     * If this return null, we should redirect user to signIn
     */
    fun getClient(): RedditClient {
        return INSTANCE?: throw IllegalStateException("Not authenticated")
    }
}