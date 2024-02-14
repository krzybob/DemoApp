package pl.bobinski.demo.network.interceptor

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import pl.bobinski.demo.network.interceptor.common.AppInterceptor

class CacheFallbackInterceptor : AppInterceptor() {

    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            chain.proceed(chain.request())
        } catch (_: Exception) {
            val builder: Request.Builder = chain.request().newBuilder()
            builder.cacheControl(CacheControl.FORCE_CACHE)
            chain.proceed(builder.build())
        }
    }
}