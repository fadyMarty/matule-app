package com.fadymarty.matule

import android.app.Application
import com.fadymarty.matule.di.appModule
import com.fadymarty.matule_network.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MatuleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MatuleApp)
            modules(appModule, networkModule)
        }
    }
}