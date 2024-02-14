package pl.bobinski.demo.repository

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pl.bobinski.demo.domain.ListedUser
import pl.bobinski.demo.domain.UserDetails
import pl.bobinski.demo.domain.toListedUserOrNull
import pl.bobinski.demo.domain.toUserDetailsOrNull
import pl.bobinski.demo.network.service.GithubService
import javax.inject.Inject

@Reusable
class UserRepository @Inject constructor(
    private val githubService: GithubService
) {
    fun listUsers(
        since: Int,
        pageSize: Int
    ): Flow<List<ListedUser>> = flow {
        githubService.listUsers(since = since, pageSize = pageSize)
            .mapNotNull { it.toListedUserOrNull() }
            .let { emit(it) }
    }

    fun searchUsers(
        query: String,
        page: Int,
        pageSize: Int
    ): Flow<List<ListedUser>> = flow {
        githubService.searchUsers(query = query, page = page, pageSize = pageSize)
            .items
            .mapNotNull { it.toListedUserOrNull() }
            .let { emit(it) }
    }

    fun getUser(login: String): Flow<UserDetails?> = flow {
        githubService.getUser(login)
            .toUserDetailsOrNull()
            .let { emit(it) }
    }
}