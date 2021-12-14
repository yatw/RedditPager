package com.example.redditpager

import android.app.Application
import timber.log.Timber


class PagerApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        setUpTimberLog()
        logDeviceInfo()
    }

    private fun setUpTimberLog(){
        if (BuildConfig.DEBUG){
            // https://medium.com/androiddevnotes/customize-your-android-timber-logger-setup-to-add-a-global-tag-and-a-method-name-to-the-logs-for-e7f23acd844f
            Timber.plant(object : Timber.DebugTree() {
                // Override [log] to modify the tag and add a "global tag" prefix to it.
                override fun log(
                    priority: Int, tag: String?, message: String, t: Throwable?
                ) {
                    super.log(priority, "Log=>$tag", message, t)
                }
            })
        }
    }

    private fun logDeviceInfo(){
        Timber.i("Device API level is ${android.os.Build.VERSION.SDK_INT}")
        Timber.i("Device dpi is ${resources.displayMetrics.densityDpi}")
    }
}