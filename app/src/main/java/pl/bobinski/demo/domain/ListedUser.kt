package pl.bobinski.demo.domain

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import pl.bobinski.demo.data.ListUsersResponseItem
import pl.bobinski.demo.data.SearchUsersResponse

@Parcelize
data class ListedUser(val login: String, val avatar: Uri) : Parcelable

fun ListUsersResponseItem.toListedUserOrNull(): ListedUser? {
    return ListedUser(
        login = login ?: return null,
        avatar = avatarUrl?.let { Uri.parse(it) } ?: return null
    )
}

fun SearchUsersResponse.Item.toListedUserOrNull(): ListedUser? {
    return ListedUser(
        login = login ?: return null,
        avatar = avatarUrl?.let { Uri.parse(it) } ?: return null
    )
}