package pl.bobinski.demo.domain

import android.net.Uri
import pl.bobinski.demo.data.GetUserResponse
import java.util.Date

data class UserDetails(
    val login: String,
    val avatar: Uri,
    val email: String?,
    val company: String?,
    val since: Date,
    val publicRepos: Int,
    val followers: Int
)

fun GetUserResponse.toUserDetailsOrNull(): UserDetails? {
    return UserDetails(
        login = login ?: return null,
        avatar = avatarUrl?.let { Uri.parse(it) } ?: return null,
        email = email,
        company = company,
        since = createdAt ?: return null,
        publicRepos = publicRepos ?: return null,
        followers = followers ?: return null
    )
}