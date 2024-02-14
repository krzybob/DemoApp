package pl.bobinski.demo.ui.list

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import pl.bobinski.demo.core.BaseViewModel
import pl.bobinski.demo.domain.ListedUser
import pl.bobinski.demo.repository.UserRepository
import pl.bobinski.demo.repository.paging.ListUsersPagingSource
import pl.bobinski.demo.repository.paging.SearchUsersPagingSource
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class UsersListViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel<Event, State, Effect>(State()) {

    private val query = MutableStateFlow("")
    private val data = query
        .debounce(0.5.seconds)
        .distinctUntilChanged()
        .mapLatest { query ->
            Pager(
                config = PagingConfig(pageSize = PAGE_SIZE),
                pagingSourceFactory = {
                    if (query.isNotBlank()) {
                        SearchUsersPagingSource(query = query, userRepository = userRepository)
                    } else {
                        ListUsersPagingSource(userRepository = userRepository)
                    }
                }
            ).flow.cachedIn(viewModelScope)
        }

    init {
        combine(query, data) { q, d ->
            updateState { it.copy(query = q, data = d) }
        }.launchIn(viewModelScope)
    }

    override fun processEvent(event: Event) {
        super.processEvent(event)
        when (event) {
            is Event.OnQueryChanged -> {
                query.update { event.query }
            }

            is Event.OnUserClicked -> {
                dispatchEffect(Effect.ShowDetails(event.user))
            }
        }
    }

    companion object {
        private const val PAGE_SIZE = 30
    }
}

sealed class Event {
    data class OnQueryChanged(val query: String) : Event()
    data class OnUserClicked(val user: ListedUser) : Event()
}

data class State(
    val query: String = "",
    val data: Flow<PagingData<ListedUser>> = emptyFlow()
)

sealed class Effect {
    data class ShowDetails(val user: ListedUser) : Effect()
}