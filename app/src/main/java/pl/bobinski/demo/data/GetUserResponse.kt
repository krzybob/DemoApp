package pl.bobinski.demo.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class GetUserResponse(
    @Json(name = "login") var login: String? = null,
    @Json(name = "avatar_url") var avatarUrl: String? = null,
    @Json(name = "email") var email: String? = null,
    @Json(name = "company") var company: String? = null,
    @Json(name = "created_at") var createdAt: Date? = null,
    @Json(name = "public_repos") var publicRepos: Int? = null,
    @Json(name = "followers") var followers: Int? = null,
)