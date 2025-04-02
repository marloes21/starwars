package com.example.starwars.ui.screens.filmDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.starwars.data.film.FilmRepository
import com.example.starwars.domain.CombineFilmWithCharactersAndSpaceshipUseCase
import com.example.starwars.domain.CombinedFilmData
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
class FilmDetialViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val combineFilmWithCharactersAndSpaceshipUseCase: CombineFilmWithCharactersAndSpaceshipUseCase,
    private val filmRepository: FilmRepository,
) : ViewModel() {

    sealed interface State {
        data object Loading : State
        data class Error(val error: ErrorType) : State
        data class Content(
            val data: CombinedFilmData,
        ) : State
    }

    sealed interface Event {
        data class NavigateToCharacterDetail(val characterId: String, val name: String) : Event
        data class NavigateToSpaceshipDetail(val spaceschipId: String, val name: String) : Event
        data object NavigateUp : Event
    }

    sealed interface Action {
        data class OnOpenCharacter(val characterId: String, val name: String) : Action
        data class OnOpenSpaceShip(val spaceshipId: String, val name: String) : Action
        data object ToggleFavorite : Action
        data object NavigateUp : Action
    }


    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    private val _events = Channel<Event>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()


    init {
        val id = savedStateHandle.toRoute<MainScreen.FilmDetail>().filmId
        viewModelScope.launch {
            combineFilmWithCharactersAndSpaceshipUseCase.execute(id).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _state.value = State.Content(
                            data = response.data
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
            is Action.OnOpenCharacter -> navigateToCharacterDetail(action)
            is Action.OnOpenSpaceShip -> navigateToSpaceshipDetail(action)
            is Action.ToggleFavorite -> toggleFavorite()
            is Action.NavigateUp -> navigateUp()
        }
    }

    private fun navigateToCharacterDetail(action: Action.OnOpenCharacter) {
        viewModelScope.launch {
            _events.send(Event.NavigateToCharacterDetail(action.characterId, action.name))
        }
    }

    private fun navigateToSpaceshipDetail(action: Action.OnOpenSpaceShip) {
        viewModelScope.launch {
            _events.send(Event.NavigateToCharacterDetail(action.spaceshipId, action.name))
        }
    }

    private fun navigateUp() {
        viewModelScope.launch {
            _events.send(Event.NavigateUp)
        }
    }

    private fun toggleFavorite() {
        viewModelScope.launch {
            val currentFilm = (_state.value as State.Content).data.film
            filmRepository.toggleFavorite(currentFilm)
            val film = currentFilm.copy(
                favorite = !currentFilm.favorite
            )
            _state.value = State.Content(
                data = (_state.value as State.Content).data.copy(
                    film = film
                )
            )
        }
    }
}