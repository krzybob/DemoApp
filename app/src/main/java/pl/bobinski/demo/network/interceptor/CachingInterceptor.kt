package pl.bobinski.demo.network.interceptor

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import pl.bobinski.demo.AppConfig
import pl.bobinski.demo.network.interceptor.common.NetworkInterceptor
import java.util.concurrent.TimeUnit

class CachingInterceptor : NetworkInterceptor() {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())
        val cacheControl = CacheControl.Builder()
            .maxAge(AppConfig.HttpCacheValidity.inWholeSeconds.toInt(), TimeUnit.SECONDS)
            .build()
        return response.newBuilder()
            .header("Cache-Control", cacheControl.toString())
            .build()
    }
}