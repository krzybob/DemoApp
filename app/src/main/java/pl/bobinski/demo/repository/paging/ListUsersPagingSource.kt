package pl.bobinski.demo.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import pl.bobinski.demo.domain.ListedUser
import pl.bobinski.demo.repository.UserRepository

class ListUsersPagingSource(
    private val userRepository: UserRepository
) : PagingSource<Int, ListedUser>() {

    override fun getRefreshKey(state: PagingState<Int, ListedUser>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListedUser> {
        val since = params.key ?: 0
        val pageSize = params.loadSize
        return userRepository.listUsers(since = since, pageSize = pageSize)
            .map<List<ListedUser>, LoadResult<Int, ListedUser>> {
                LoadResult.Page(
                    data = it,
                    prevKey = (since - pageSize).run { if (this < 0) null else this },
                    nextKey = if (it.size < pageSize) null else since + pageSize
                )
            }
            .catch { emit(LoadResult.Error(it)) }
            .first()
    }
}