package pl.bobinski.demo.image

import android.content.Context
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import pl.bobinski.demo.AppConfig
import pl.bobinski.demo.DemoApp
import pl.bobinski.demo.core.Initializer

class CoilInitializer : Initializer {

    override fun onInitialize(applicationContext: Context) {
        Coil.setImageLoader(
            ImageLoader.Builder(applicationContext)
                .memoryCache {
                    MemoryCache.Builder(applicationContext)
                        .maxSizePercent(AppConfig.ImageCacheMemoryPercent)
                        .build()
                }
                .diskCache {
                    DiskCache.Builder()
                        .directory(applicationContext.cacheDir.resolve(DemoApp.CACHE_FILE_NAME))
                        .maxSizeBytes(AppConfig.ImageCacheMaxSizeBytes)
                        .build()
                }
                .respectCacheHeaders(false)
                .build()
        )
    }
}