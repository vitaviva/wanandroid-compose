package com.wanandroid.compose

import android.app.Application

class WanandroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}
