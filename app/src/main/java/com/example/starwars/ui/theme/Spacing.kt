package com.example.starwars.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Spacing(
    val tiny: Dp,
    val small: Dp,
    val medium: Dp,
    val large: Dp,
    val xlarge: Dp,
    val xxlarge: Dp,
    val xxxlarge: Dp,
)

@get:Composable
val MaterialTheme.spacing: Spacing
    get() = Spacing(
        tiny = 4.dp,
        small = 8.dp,
        medium = 12.dp,
        large = 16.dp,
        xlarge = 24.dp,
        xxlarge = 32.dp,
        xxxlarge = 48.dp,
    )