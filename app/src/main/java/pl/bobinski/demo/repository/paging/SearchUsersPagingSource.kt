package pl.bobinski.demo.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import pl.bobinski.demo.domain.ListedUser
import pl.bobinski.demo.repository.UserRepository

class SearchUsersPagingSource(
    private val query: String,
    private val userRepository: UserRepository
) : PagingSource<Int, ListedUser>() {

    override fun getRefreshKey(state: PagingState<Int, ListedUser>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListedUser> {
        val pageNum = params.key ?: 1
        val pageSize = params.loadSize
        return userRepository.searchUsers(query, pageNum, pageSize)
            .map<List<ListedUser>, LoadResult<Int, ListedUser>> {
                LoadResult.Page(
                    data = it,
                    prevKey = (pageNum - 1).run { if (this < 1) null else this },
                    nextKey = if (it.size < pageSize) null else pageNum + 1
                )
            }
            .catch { emit(LoadResult.Error(it)) }
            .first()
    }
}