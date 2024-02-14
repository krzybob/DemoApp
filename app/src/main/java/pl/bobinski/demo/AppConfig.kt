package pl.bobinski.demo

import kotlin.time.Duration.Companion.days

object AppConfig {
    const val GithubApiUrl = "https://api.github.com"
    val AccessToken: String? = null
    val HttpCacheValidity = 1.days
    const val HttpCacheMaxSizeBytes = 5L * 1024 * 1024
    const val ImageCacheMemoryPercent = 0.5
    const val ImageCacheMaxSizeBytes = 50L * 1024 * 1024
}