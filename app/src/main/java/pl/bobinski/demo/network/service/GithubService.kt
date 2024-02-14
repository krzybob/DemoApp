package pl.bobinski.demo.network.service

import pl.bobinski.demo.data.GetUserResponse
import pl.bobinski.demo.data.ListUsersResponseItem
import pl.bobinski.demo.data.SearchUsersResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {

    @GET("/users")
    suspend fun listUsers(
        @Query("since") since: Int? = null,
        @Query("per_page") pageSize: Int? = null
    ): List<ListUsersResponseItem>

    @GET("/search/users")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("page") page: Int? = null,
        @Query("per_page") pageSize: Int? = null
    ): SearchUsersResponse

    @GET("/users/{username}")
    suspend fun getUser(@Path("username") name: String): GetUserResponse
}