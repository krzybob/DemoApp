package pl.bobinski.demo.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ListUsersResponseItem(
    @Json(name = "login") var login: String? = null,
    @Json(name = "avatar_url") var avatarUrl: String? = null
)