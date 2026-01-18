package com.fadymarty.matule

import android.app.Application
import com.fadymarty.matule.common.util.Constants
import com.fadymarty.matule.di.appModule
import com.fadymarty.network.di.networkModule
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig
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

        val config = AppMetricaConfig
            .newConfigBuilder(Constants.APPMETRICA_API_KEY)
            .build()
        AppMetrica.activate(this, config)
    }
}