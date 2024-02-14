package pl.bobinski.demo.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import pl.bobinski.demo.AppConfig
import pl.bobinski.demo.network.interceptor.common.NetworkInterceptor

class AuthTokenInterceptor : NetworkInterceptor() {

    override fun intercept(chain: Interceptor.Chain): Response {
        val header = AppConfig.AccessToken ?: ""
        val response: Response = chain.proceed(chain.request())
        return response.newBuilder().run {
            if (header.isNotBlank()) {
                header("access_token", header)
            } else this
        }.build()
    }
}