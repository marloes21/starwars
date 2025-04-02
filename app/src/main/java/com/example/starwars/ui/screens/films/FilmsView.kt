package com.example.starwars.ui.screens.films

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.starwars.R
import com.example.starwars.data.film.Film
import com.example.starwars.ui.components.PrimaryErrorView
import com.example.starwars.ui.screens.films.FilmsViewModel.Action
import com.example.starwars.ui.theme.spacing
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmsView(
    viewModel: FilmsViewModel,
    navigateToFilmDetail: (String, String) -> Unit,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val isRefreshing = viewModel.refreshing.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is FilmsViewModel.Event.NavigateToFilmDetail -> {
                    navigateToFilmDetail(event.filmId, event.filmTitle)
                }
            }
        }
    }

    //TODO make this a reusable component
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.films_title)) },
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) {
            val _state = state.value
            when (_state) {
                FilmsViewModel.State.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                }

                is FilmsViewModel.State.Error -> {
                    PrimaryErrorView(
                        title = R.string.error_title,
                        error = _state.error,
                    )
                }

                is FilmsViewModel.State.Content -> {
                    ContentView(
                        films = _state.films.sortedBy { it.episodeId },
                        isRefreshing = isRefreshing.value,
                        onAction = viewModel::applyAction,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContentView(
    films: List<Film>,
    isRefreshing: Boolean,
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pullToRefreshState = rememberPullToRefreshState()
    var didPullToRefresh by rememberSaveable { mutableStateOf(false) }
    var isInitialRefresh by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(isRefreshing) {
        if (!isRefreshing) {
            didPullToRefresh = false
            isInitialRefresh = false
        }
    }

    PullToRefreshBox(
        state = pullToRefreshState,
        isRefreshing = isRefreshing && didPullToRefresh,
        onRefresh = {
            didPullToRefresh = true
            onAction(Action.OnRefresh)
        },
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.padding(MaterialTheme.spacing.small)
        ) {
            items(films.size) { index ->
                val film = films[index]
                Card(
                    onClick = {
                        onAction(
                            Action.OnOpenFilm(
                                film.id,
                                film.title
                            )
                        )
                    }) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(MaterialTheme.spacing.medium)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = film.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                            IconButton(
                                modifier = Modifier.weight(0.1f),
                                onClick = {
                                    onAction(
                                        Action.ToggleFavorite(
                                            film
                                        )
                                    )
                                },
                            ) {
                                Icon(
                                    imageVector = if (film.favorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = null,
                                )
                            }
                        }
                        Text(
                            text = "Release date: ${film.releaseDate.format(dateFormatter)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Director: ${film.director}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Producer: ${film.producer}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.tiny))
                    }
                }
                Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
            }
        }
    }
}

private val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")