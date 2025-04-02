package com.example.starwars.ui.characterDetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.starwars.ui.characterDetail.CharacterDetailViewModel.Event
import com.example.starwars.ui.components.PrimaryErrorView
import com.example.starwars.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailView(
    viewModel: CharacterDetailViewModel,
    onBackClick: () -> Unit,
    title: String,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is Event.NavigateUp -> {
                    onBackClick()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(title)
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.applyAction(CharacterDetailViewModel.Action.NavigateUp) },
                        content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                            )
                        },
                    )
                }
            )
        },

        ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) {
            val _state = state.value
            when (_state) {
                is CharacterDetailViewModel.State.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                }

                is CharacterDetailViewModel.State.Error -> {
                    PrimaryErrorView(
                        title = R.string.error_title,
                        error = _state.error,
                    )
                }

                is CharacterDetailViewModel.State.Content -> {
                    CharacterDetailContent(
                        character = _state.character,
                    )
                }
            }
        }
    }

}

@Composable
private fun CharacterDetailContent(
    character: Character,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.verticalScroll(scrollState).fillMaxSize()
            .padding(MaterialTheme.spacing.medium)
    ) {
        Text(
            text = stringResource(R.string.character_details_name, character.name)
        )
        Text(
            text = stringResource(R.string.character_details_height, character.height)
        )
        Text(
            text = stringResource(R.string.character_details_weight, character.weight)
        )
        Text(
            text = stringResource(R.string.character_details_hair_color, character.hairColor)
        )
        Text(
            text = stringResource(R.string.character_details_skin_color, character.skinColor)
        )
        Text(
            text = stringResource(R.string.character_details_eye_color, character.eyeColor)
        )
    }
}