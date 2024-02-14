package pl.bobinski.demo.network.cache

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Cache
import pl.bobinski.demo.AppConfig
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HttpCacheWrapper @Inject constructor(@ApplicationContext context: Context) {

    val cache = Cache(File(context.cacheDir, CACHE_FILE_NAME), AppConfig.HttpCacheMaxSizeBytes)

    companion object {
        const val CACHE_FILE_NAME = "httpCache"
    }
}