package pl.bobinski.demo.core

import androidx.annotation.CallSuper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber

abstract class BaseViewModel<Event : Any, State : Any, Effect : Any>(
    initialState: State,
) : ViewModel() {

    private val state = MutableStateFlow(initialState)

    private val effects = Channel<Effect>(Channel.BUFFERED)

    @CallSuper
    protected open fun processEvent(event: Event) {
        log(event)
    }

    protected fun updateState(update: (State) -> State) {
        state.update(update)
        log(state.value)
    }

    protected fun dispatchEffect(effect: Effect) {
        log(effect)
        effects.trySend(effect)
    }

    private fun log(any: Any) {
        Timber.d("${this@BaseViewModel::class.simpleName} $any")
    }

    @Composable
    fun use(): ViewModelContract<Event, State, Effect> {

        val state by state.collectAsState()

        return ViewModelContract(
            processEvent = ::processEvent,
            state = state,
            effects = effects.receiveAsFlow()
        )
    }
}

data class ViewModelContract<Event, State, Effect>(
    val processEvent: (Event) -> Unit,
    val state: State,
    val effects: Flow<Effect>
)