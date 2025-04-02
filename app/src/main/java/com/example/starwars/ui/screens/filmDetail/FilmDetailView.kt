package com.example.starwars.ui.screens.filmDetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.starwars.R
import com.example.starwars.data.character.Character
import com.example.starwars.data.film.Film
import com.example.starwars.data.spaceship.Spaceship
import com.example.starwars.domain.CombinedFilmData
import com.example.starwars.ui.components.ExpandableRow
import com.example.starwars.ui.components.ExpandedRowClickAbleCard
import com.example.starwars.ui.components.PrimaryErrorView
import com.example.starwars.ui.screens.filmDetail.FilmDetialViewModel.Action
import com.example.starwars.ui.screens.filmDetail.FilmDetialViewModel.Event
import com.example.starwars.ui.screens.filmDetail.FilmDetialViewModel.State
import com.example.starwars.ui.theme.spacing
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmDetailView(
    viewModel: FilmDetialViewModel,
    onBackClick: () -> Unit,
    navigateToCharacterDetail: (String, String) -> Unit,
    navigateToSpaceshipDetail: (String, String) -> Unit,
    title: String,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is Event.NavigateToCharacterDetail -> {
                    navigateToCharacterDetail(event.characterId, event.name)
                }

                is Event.NavigateToSpaceshipDetail -> {
                    navigateToSpaceshipDetail(event.spaceschipId, event.name)
                }

                is Event.NavigateUp -> {
                    onBackClick()
                }
            }
        }
    }
    //TODO make this a reusable component
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(title)
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.applyAction(Action.NavigateUp) },
                        content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                            )
                        },
                    )
                },
                actions = {
                    if (state.value is State.Content) {
                        IconButton(
                            onClick = { viewModel.applyAction(Action.ToggleFavorite) },
                            content = {
                                Icon(
                                    imageVector = if ((state.value as State.Content).data.film.favorite == true) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = null,
                                )
                            },
                        )
                    }
                }
            )
        },

        ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) {
            val _state = state.value
            when (_state) {
                is State.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                }

                is State.Error -> {
                    PrimaryErrorView(
                        title = R.string.error_title,
                        error = _state.error,
                    )
                }

                is State.Content -> {
                    ContentView(
                        data = _state.data,
                        onAction = viewModel::applyAction
                    )
                }
            }
        }
    }
}

@Composable
private fun ContentView(
    data: CombinedFilmData,
    onAction: (Action) -> Unit,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.verticalScroll(scrollState).fillMaxSize()
            .padding(MaterialTheme.spacing.medium)
    ) {
        FilmDetailContent(data.film)
        Spacer(Modifier.height(MaterialTheme.spacing.small))
        CharacterDetailContent(data.characters, onAction)
        Spacer(Modifier.height(MaterialTheme.spacing.small))
        SpaceShipDetailContent(data.spaceships, onAction)

    }
}

@Composable
private fun FilmDetailContent(
    film: Film,
) {
    Text(
        text = stringResource(R.string.film_details_film_title),
        style = MaterialTheme.typography.headlineSmall,
    )
    Column(
        modifier = Modifier.padding(MaterialTheme.spacing.small)
    ) {
        Text(stringResource(R.string.film_details_film_episode_number, film.episodeId.toString()))
        Text(stringResource(R.string.film_details_film_director, film.director))
        Text(
            stringResource(R.string.film_details_film_producer, film.producer)
        )
        Text(
            stringResource(
                R.string.film_details_film_release_date,
                film.releaseDate.format(dateFormatter)
            )
        )
    }
    Spacer(Modifier.height(MaterialTheme.spacing.small))
    ExpandableRow(
        title = R.string.film_details_film_opening_crawl
    ) {
        Text(film.openingCrawl)
    }
}

@Composable
private fun CharacterDetailContent(
    characters: List<Character>,
    onClick: (Action) -> Unit,
) {
    ExpandableRow(
        title = R.string.film_details_characters_title,
    ) {
        characters.forEach { character ->
            ExpandedRowClickAbleCard(
                title = character.name,
                onClick = {
                    onClick(Action.OnOpenCharacter(character.id, character.name))
                }
            )
        }
    }
}


@Composable
private fun SpaceShipDetailContent(
    spaceships: List<Spaceship>,
    onClick: (Action) -> Unit,
) {
    ExpandableRow(
        title = R.string.film_details_spaceships_title,
    ) {
        spaceships.forEach { spaceship ->
            ExpandedRowClickAbleCard(
                title = spaceship.name,
                onClick = {
                    onClick(Action.OnOpenSpaceShip(spaceship.id, spaceship.name))
                }
            )
        }
    }
}

private val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")