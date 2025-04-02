package com.example.starwars.ui


import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.starwars.ui.characterDetail.CharacterDetailView
import com.example.starwars.ui.screens.filmDetail.FilmDetailView
import com.example.starwars.ui.screens.films.FilmsView
import com.example.starwars.ui.spaceshipDetail.SpaceshipDetailView


@Composable
fun MainNavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = MainScreen.startDestination,
    ) {

        composable<MainScreen.Films> {

            FilmsView(
                viewModel = hiltViewModel(),
                navigateToFilmDetail = { id, title ->
                    navController.navigate(
                        route = MainScreen.FilmDetail(
                            filmId = id,
                            filmTitle = title,
                        )
                    )
                }
            )
        }

        composable<MainScreen.FilmDetail> { backStackEntry ->
            val arg = backStackEntry.toRoute<MainScreen.FilmDetail>()
            FilmDetailView(
                viewModel = hiltViewModel(),
                onBackClick = { navController.popBackStack() },
                title = arg.filmTitle,
                navigateToCharacterDetail = { id, name ->
                    navController.navigate(
                        route = MainScreen.CharacterDetail(
                            characterId = id,
                            name = name,
                        )
                    )
                },
                navigateToSpaceshipDetail = { id, name ->
                    navController.navigate(
                        route = MainScreen.SpaceshipDetail(
                            spaceshipId = id,
                            name = name,
                        )
                    )
                }
            )
        }

        composable<MainScreen.CharacterDetail> { backStackEntry ->
            val arg = backStackEntry.toRoute<MainScreen.CharacterDetail>()

            CharacterDetailView(
                viewModel = hiltViewModel(),
                onBackClick = { navController.popBackStack() },
                title = arg.name
            )
        }

        composable<MainScreen.SpaceshipDetail> { backStackEntry ->
            val arg = backStackEntry.toRoute<MainScreen.SpaceshipDetail>()
            SpaceshipDetailView(
                viewModel = hiltViewModel(),
                onBackClick = { navController.popBackStack() },
                title = arg.name
            )
        }

    }

}