package com.example.starwars.ui.screens.films

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwars.data.film.Film
import com.example.starwars.data.film.FilmRepository
import com.example.starwars.util.ErrorType
import com.example.starwars.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FilmsViewModel @Inject constructor(
    private val filmRepository: FilmRepository,
) : ViewModel() {

    sealed interface State {
        data object Loading : State
        data class Error(val error: ErrorType) : State
        data class Content(
            val films: List<Film>,
        ) : State
    }

    sealed interface Event {
        data class NavigateToFilmDetail(val filmId: String, val filmTitle: String) : Event
    }

    sealed interface Action {
        data class OnOpenFilm(val filmId: String, val filmTitle: String) : Action
        data class ToggleFavorite(val film: Film) : Action
        data object OnRefresh : Action
    }


    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    private val _events = Channel<Event>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val _refreshing = MutableStateFlow(true)
    val refreshing = _refreshing.asStateFlow()

    private var refreshJob: Job? = null

    init {
        viewModelScope.launch {
            filmRepository.syncFilms().collect { response ->
                when (response) {
                    is Resource.Failure -> _state.value = State.Error(response.error)
                    is Resource.Success -> refresh()
                }
            }
        }
    }

    fun applyAction(action: Action) {
        when (action) {
            is Action.OnOpenFilm -> navigateToFilmDetail(action)
            is Action.ToggleFavorite -> toggleFavorite(action)
            is Action.OnRefresh -> refresh()

        }
    }

    private fun refresh() {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            _refreshing.value = true
            try {
                filmRepository.getAllFilms().collect { response ->
                    println(response)
                    _state.value = State.Content(
                        films = response
                    )
                }
            } finally {
                _refreshing.value = false
            }
        }
    }


    private fun navigateToFilmDetail(action: Action.OnOpenFilm) {
        viewModelScope.launch {
            _events.send(Event.NavigateToFilmDetail(action.filmId, action.filmTitle))
        }
    }

    private fun toggleFavorite(action: Action.ToggleFavorite) {
        viewModelScope.launch {
            filmRepository.toggleFavorite(action.film)
            val films = (_state.value as State.Content).films.map {
                if (it.id == action.film.id) {
                    it.copy(favorite = !it.favorite)
                } else {
                    it
                }
            }
            _state.value = State.Content(
                films = films
            )
        }
    }
}