package com.example.lifecounting.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
    player: MutableState<PlayerState>,
    onLifeChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        EditableText(
            initialText = player.value.name,
            onTextChange = { newName ->

            }
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp) // Add spacing between items
        ) {
            // Decrease life button
            FilledTonalButton(onClick = { onLifeChange(-1) }) { Text("-") }

            // Display life
            Text(
                text = "${player.value.life}",
                style = MaterialTheme.typography.displayLarge
            )

            // Increase life button
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
                counterValue = player.value.poisonCounters,
                onCounterChange = { change ->
                    player.value = player.value.copy(
                        poisonCounters = (player.value.poisonCounters + change).coerceAtLeast(0)
                    )
                }
            )

            FloatingIconCounter(
                icon = painterResource(id = R.drawable.commandercounter),
                contentDescription = "Commander Damage counters",
                counterValue = player.value.commanderDamage,
                onCounterChange = { change ->
                    player.value = player.value.copy(
                        commanderDamage = (player.value.commanderDamage + change).coerceAtLeast(0)
                    )
                }
            )

            FloatingIconCounter(
                icon = painterResource(id = R.drawable.energycounter),
                contentDescription = "Energy counters",
                counterValue = player.value.energy,
                onCounterChange = { change ->
                    player.value = player.value.copy( // Assign the result back to player.value
                        energy = (player.value.energy + change).coerceAtLeast(0)
                    )
                }
            )

            FloatingIconCounter(
                icon = painterResource(id = R.drawable.experiencecounter),
                contentDescription = "Experience counters",
                counterValue = player.value.experience,
                onCounterChange = { change ->
                    player.value = player.value.copy(
                        experience = (player.value.experience + change).coerceAtLeast(0)
                    )
                }
            )

            FloatingIconCounter(
                icon = painterResource(id = R.drawable.chargecounter),
                contentDescription = "Charge counters",
                counterValue = player.value.charge,
                onCounterChange = { change ->
                    player.value.copy(
                        charge = (player.value.charge + change).coerceAtLeast(0)
                    )
                }
            )

            FloatingIconCounter(
                icon = painterResource(id = R.drawable.loyaltycounter),
                contentDescription = "Loyalty counters",
                counterValue = player.value.loyalty,
                onCounterChange = { change ->
                    player.value = player.value.copy(
                        loyalty = (player.value.loyalty + change).coerceAtLeast(0)
                    )
                }
            )

            FloatingIconCounter(
                icon = painterResource(id = R.drawable.timecounter),
                contentDescription = "Time counters",
                counterValue = player.value.time,
                onCounterChange = { change ->
                    player.value = player.value.copy(
                        time = (player.value.time + change).coerceAtLeast(0)
                    )
                }
            )
        }
    }
}

@Composable
fun EditableText(
    initialText: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(initialText) }

    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        modifier = modifier.clickable { showDialog = true }
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Edit Text") },
            text = {
                TextField(
                    value = text,
                    onValueChange = { newText -> text = newText },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(onClick = {
                    onTextChange(text) // Trigger callback with updated text
                    showDialog = false
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
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