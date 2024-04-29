package com.ltu.m7019e.moviedb.v24

import android.app.Application
import com.ltu.m7019e.moviedb.v24.database.AppContainer
import com.ltu.m7019e.moviedb.v24.database.DefaultAppContainer

class MovieDBApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}