package com.example.lifecounting.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.lifecounting.PlayerState
import com.example.lifecounting.R

@Composable
fun PlayerSection(
    state: PlayerState,
    onLifeChange: (Int) -> Unit,
    onCounterChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Life section at the top
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Decrease life button on the left
            FilledTonalButton(
                onClick = { onLifeChange(-1) }) { Text("-") }

            // Display life in a large font
            Text(
                text = "${state.life}",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            // Increase life button on the right
            FilledTonalButton(onClick = { onLifeChange(1) }) { Text("+") }
        }

        // Counter section arranged at the bottom
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Center, // Centers items horizontally
            verticalAlignment = Alignment.CenterVertically // Centers items vertically
        ) {
            FloatingIconCounter(
                icon = painterResource(id = R.drawable.poisoncounter),
                contentDescription = "Poison counters",
                counterValue = state.poisonCounters,
                onCounterChange = onCounterChange
            )

            FloatingIconCounter(
                icon = painterResource(id = R.drawable.commandercounter),
                contentDescription = "Commander Damage counters",
                counterValue = state.commanderDamage,
                onCounterChange = onCounterChange
            )

            FloatingIconCounter(
                icon = painterResource(id = R.drawable.energycounter),
                contentDescription = "Energy counters",
                counterValue = state.energy,
                onCounterChange = onCounterChange
            )

            FloatingIconCounter(
                icon = painterResource(id = R.drawable.experiencecounter),
                contentDescription = "Experience counters",
                counterValue = state.poisonCounters,
                onCounterChange = onCounterChange
            )

            FloatingIconCounter(
                icon = painterResource(id = R.drawable.chargecounter),
                contentDescription = "Charge counters",
                counterValue = state.poisonCounters,
                onCounterChange = onCounterChange
            )

            FloatingIconCounter(
                icon = painterResource(id = R.drawable.loyaltycounter),
                contentDescription = "Loyalty counters",
                counterValue = state.poisonCounters,
                onCounterChange = onCounterChange
            )

            FloatingIconCounter(
                icon = painterResource(id = R.drawable.timecounter),
                contentDescription = "Time counters",
                counterValue = state.poisonCounters,
                onCounterChange = onCounterChange
            )
        }
    }
}



@Composable
fun PlayerSettingsScreen(
    initialNumberOfPlayers: Int,
    initialStartingLife: Int,
    onNumberOfPlayersChange: (Int) -> Unit,
    onStartingLifeChange: (Int) -> Unit,
    onLayoutChange: (Layout) -> Unit
) {
    var numberOfPlayers by remember { mutableIntStateOf(initialNumberOfPlayers) }
    var startingLife by remember { mutableIntStateOf(initialStartingLife) }
    var layout by remember { mutableStateOf(Layout.GRID) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Player Settings", style = MaterialTheme.typography.headlineSmall)

        // Number of players setting
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Number of Players:", modifier = Modifier.width(150.dp))
            TextField(
                value = numberOfPlayers.toString(),
                onValueChange = { newValue ->
                    numberOfPlayers = newValue.toIntOrNull() ?: numberOfPlayers
                    onNumberOfPlayersChange(numberOfPlayers)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        // Starting life setting
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Starting Life:", modifier = Modifier.width(150.dp))
            TextField(
                value = startingLife.toString(),
                onValueChange = { newValue ->
                    startingLife = newValue.toIntOrNull() ?: startingLife
                    onStartingLifeChange(startingLife)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        // Layout selection (Radio buttons)
        Row {
            Text("Layout:")
            RadioButton(
                selected = layout == Layout.GRID,
                onClick = {
                    layout = Layout.GRID
                    onLayoutChange(layout)
                }
            )
            Text("Grid")
            RadioButton(
                selected = layout == Layout.LIST,
                onClick = {
                    layout = Layout.LIST
                    onLayoutChange(layout)
                }
            )
            Text("List")
        }
    }
}

enum class Layout {
    GRID, LIST
}