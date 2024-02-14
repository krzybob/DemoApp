package pl.bobinski.demo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import pl.bobinski.demo.core.Initializer
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class DemoApp : Application() {

    @Inject
    lateinit var inializers: Set<@JvmSuppressWildcards Initializer>

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        inializers.forEach {
            it.onInitialize(applicationContext)
        }
    }

    companion object {
        const val CACHE_FILE_NAME = "imageCache"
    }
}