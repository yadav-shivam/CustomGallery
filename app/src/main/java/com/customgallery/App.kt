package com.customgallery

import android.app.Application

class App : Application() {

    private val applicationContext: App? = null


    fun getInstance(): App? {
        return applicationContext
    }


    override fun onCreate() {
        super.onCreate()
        instance = this

    }


    companion object {
        var instance: App? = null
            private set
    }
}