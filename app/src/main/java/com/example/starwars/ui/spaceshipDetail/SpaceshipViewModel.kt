package com.example.starwars.ui.spaceshipDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.starwars.data.character.Character
import com.example.starwars.data.character.CharacterRepository
import com.example.starwars.data.spaceship.Spaceship
import com.example.starwars.data.spaceship.SpaceshipRepository
import com.example.starwars.ui.MainScreen
import com.example.starwars.util.ErrorType
import com.example.starwars.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SpaceshipDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val spaceshipRepository: SpaceshipRepository
) : ViewModel() {

    sealed interface State {
        data object Loading : State
        data class Error(val error: ErrorType) : State
        data class Content(
            val spaceship: Spaceship,
        ) : State
    }

    sealed interface Event {
        data object NavigateUp : Event
    }

    sealed interface Action {
        data object NavigateUp : Action
    }


    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    private val _events = Channel<Event>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()


    init {
        val id = savedStateHandle.toRoute<MainScreen.CharacterDetail>().characterId
        viewModelScope.launch {
            spaceshipRepository.getSpaceshipById(id).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _state.value = State.Content(
                            spaceship = response.data
                        )
                    }
                    is Resource.Failure -> {
                        _state.value = State.Error(response.error)
                    }
                }
            }
        }
    }


    fun applyAction(action: Action) {
        when (action) {
            is Action.NavigateUp -> navigateUp()
        }
    }

    private fun navigateUp() {
        viewModelScope.launch {
            _events.send(Event.NavigateUp)
        }
    }
}
