package app.so.roombasics

import android.app.Application
import app.so.roombasics.koin.appModule
import org.koin.android.ext.android.startKoin

class UserApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule))
    }
}