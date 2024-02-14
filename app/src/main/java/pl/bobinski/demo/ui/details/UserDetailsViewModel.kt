package pl.bobinski.demo.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import pl.bobinski.demo.core.BaseViewModel
import pl.bobinski.demo.domain.UserDetails
import pl.bobinski.demo.repository.UserRepository
import pl.bobinski.demo.ui.destinations.UserDetailsScreenDestination
import pl.bobinski.demo.ui.navArgs
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<Event, State, Effect>(State.Loading) {

    init {
        val user = savedStateHandle.navArgs<UserDetailsScreenDestination.NavArgs>().listedUser
        userRepository.getUser(user.login)
            .map { details -> details?.let { State.Success(it) } ?: State.Error }
            .onEach { result -> updateState { result } }
            .launchIn(viewModelScope)
    }

    override fun processEvent(event: Event) {
        super.processEvent(event)
        when (event) {
            Event.OnBackClicked -> {
                dispatchEffect(Effect.NavigateBack)
            }
        }
    }
}

sealed class Event {
    data object OnBackClicked : Event()
}

sealed class State {
    data object Loading : State()
    data class Success(val user: UserDetails) : State()
    data object Error : State()
}

sealed class Effect {
    data object NavigateBack : Effect()
}