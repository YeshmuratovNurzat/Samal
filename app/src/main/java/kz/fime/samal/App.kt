package kz.fime.samal

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kz.fime.samal.data.SessionManager
import timber.log.Timber

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        SessionManager.inject(this)
    }

}