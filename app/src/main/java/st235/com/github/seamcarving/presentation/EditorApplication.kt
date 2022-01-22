package st235.com.github.seamcarving.presentation

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import st235.com.github.seamcarving.di.applicationModules

class EditorApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@EditorApplication)
            modules(applicationModules)
        }
    }
}