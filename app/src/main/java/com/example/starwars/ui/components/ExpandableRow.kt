package com.example.starwars.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.starwars.ui.theme.spacing

@Composable
fun ExpandableRow(
    title: Int,
    intialExpandend: Boolean = false,
    content: @Composable () -> Unit,
) {
    val expanded = remember { mutableStateOf(intialExpandend) }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().clickable {
            expanded.value = !expanded.value
        }
    ) {
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.headlineSmall,
        )

        Icon(
            imageVector = if (expanded.value) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = null,
        )

    }
    if (expanded.value) {
        content()
    }
}

@Composable
fun ExpandedRowClickAbleCard(
    title: String,
    onClick: () -> Unit,
) {
    OutlinedCard(
        modifier = Modifier.fillMaxSize().padding(MaterialTheme.spacing.small)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(MaterialTheme.spacing.medium).clickable {
                onClick()

            },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Icon(
                modifier = Modifier.weight(0.1f),
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
            )
        }

    }
}