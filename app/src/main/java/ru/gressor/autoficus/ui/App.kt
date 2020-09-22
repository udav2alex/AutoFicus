package ru.gressor.autoficus.ui

import android.app.Application
import org.koin.android.ext.android.startKoin
import ru.gressor.autoficus.di.appModule
import ru.gressor.autoficus.di.mainModule
import ru.gressor.autoficus.di.noteModule
import ru.gressor.autoficus.di.splashModule

@Suppress("unused")
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule, splashModule, mainModule, noteModule))
    }
}