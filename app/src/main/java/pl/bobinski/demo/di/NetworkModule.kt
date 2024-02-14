package pl.bobinski.demo.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.bobinski.demo.AppConfig
import pl.bobinski.demo.BuildConfig
import pl.bobinski.demo.network.backend.GithubApi
import pl.bobinski.demo.network.cache.HttpCacheWrapper
import pl.bobinski.demo.network.interceptor.AuthTokenInterceptor
import pl.bobinski.demo.network.interceptor.CacheFallbackInterceptor
import pl.bobinski.demo.network.interceptor.CachingInterceptor
import pl.bobinski.demo.network.interceptor.common.AppInterceptor
import pl.bobinski.demo.network.interceptor.common.NetworkInterceptor
import pl.bobinski.demo.network.service.GithubService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Date
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @GithubApi
    fun provideApiUrl(): String {
        return AppConfig.GithubApiUrl
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().also {
            it.level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Singleton
    @Provides
    fun privateOkHttpClientBuilder(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient.Builder {
        return OkHttpClient.Builder().addInterceptor(loggingInterceptor)
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder().add(Date::class.java, Rfc3339DateJsonAdapter()).build()
    }

    @Singleton
    @Provides
    fun provideMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory {
        return MoshiConverterFactory.create(moshi)
    }

    @IntoSet
    @Singleton
    @Provides
    fun provideCachingInterceptor(): NetworkInterceptor {
        return CachingInterceptor()
    }

    @IntoSet
    @Singleton
    @Provides
    fun provideAuthTokenInterceptor(): NetworkInterceptor {
        return AuthTokenInterceptor()
    }

    @IntoSet
    @Singleton
    @Provides
    fun provideCacheFallbackInterceptor(): AppInterceptor {
        return CacheFallbackInterceptor()
    }

    @Singleton
    @Provides
    @GithubApi
    fun provideGithubApiClient(
        @GithubApi apiUrl: String,
        okHttpClientBuilder: OkHttpClient.Builder,
        converterFactory: MoshiConverterFactory,
        httpCacheWrapper: HttpCacheWrapper,
        networkInterceptors: Set<@JvmSuppressWildcards NetworkInterceptor>,
        appInterceptors: Set<@JvmSuppressWildcards AppInterceptor>
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(converterFactory)
            .baseUrl(apiUrl)
            .client(okHttpClientBuilder.cache(httpCacheWrapper.cache).apply {
                networkInterceptors.forEach { addNetworkInterceptor(it) }
                appInterceptors.forEach { addInterceptor(it) }
            }.build()).build()
    }

    @Singleton
    @Provides
    fun provideGithubService(@GithubApi retrofit: Retrofit): GithubService {
        return retrofit.create(GithubService::class.java)
    }
}