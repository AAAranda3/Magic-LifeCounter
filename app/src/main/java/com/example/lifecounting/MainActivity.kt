package com.example.lifecounting

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lifecounting.composables.CircularSettingsButton
import com.example.lifecounting.ui.theme.LifeCountingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        setContent {
            LifeCountingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LifeCountingTheme {
        CommanderLifeCounterApp()
    }
}


@Preview
@Composable
fun CommanderLifeCounterApp() {
    val initialLife = 40
    var numberOfPlayers by remember { mutableStateOf(4) }
    var startingLife by remember { mutableStateOf(initialLife) }
    var currentLayout by remember { mutableStateOf(Layout.GRID) }

    val playerStates = remember {
        List(numberOfPlayers) { mutableStateOf(PlayerState("Player ${it + 1}", startingLife)) }
    }

    var showSettings by remember { mutableStateOf(false) }

    val gridColumns = 2

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Commander Life Counter",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(8.dp)
            )

            // Grid Layout: Adjust columns based on screen orientation
            LazyVerticalGrid(
                columns = GridCells.Fixed(gridColumns), // Dynamically set columns
                modifier = Modifier.fillMaxSize()
            ) {
                items(playerStates) { playerState ->
                    PlayerSection(
                        state = playerState.value,
                        onLifeChange = { change ->
                            playerState.value = playerState.value.copy(
                                life = (playerState.value.life + change).coerceAtLeast(0)
                            )
                        },
                        onPoisonChange = { change ->
                            playerState.value = playerState.value.copy(
                                poisonCounters = (playerState.value.poisonCounters + change).coerceAtLeast(
                                    0
                                )
                            )
                        }
                    )
                }
            }
        }

        // Floating Settings Button
        CircularSettingsButton(onClick = { showSettings = !showSettings })

        // Show Settings Screen
        if (showSettings) {
            PlayerSettingsScreen(
                initialNumberOfPlayers = numberOfPlayers,
                initialStartingLife = startingLife,
                onNumberOfPlayersChange = { newNumberOfPlayers ->
                    numberOfPlayers = newNumberOfPlayers
                },
                onStartingLifeChange = { newStartingLife ->
                    startingLife = newStartingLife
                },
                onLayoutChange = { newLayout ->
                    currentLayout = newLayout
                }
            )
        }
    }
}

@Composable
fun PlayerSection(
    state: PlayerState,
    onLifeChange: (Int) -> Unit,
    onPoisonChange: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
            .fillMaxWidth() // Ensure it fills the width in the grid
    ) {

        // Life section with large font
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Decrease life on the left
            Row {
                Button(onClick = { onLifeChange(-1) }) { Text("-") }
            }

            // Display life in a larger font
            Text(
                text = "${state.life}",
                style = MaterialTheme.typography.headlineLarge, // Make life text larger
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            // Increase life on the right
            Row {
                Button(onClick = { onLifeChange(1) }) { Text("+") }
            }
        }

        // Display poison count with icon
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.poisoncounter),
                contentDescription = "Poison counters",
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = state.poisonCounters.toString(),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // Increase poison on the right
        Row {
            Button(onClick = { onPoisonChange(1) }) { Text("+") }
        }
    }
}


@Composable
fun PlayerSettingsScreen(
    initialNumberOfPlayers: Int,
    initialStartingLife: Int,
    onNumberOfPlayersChange: (Int) -> Unit,
    onStartingLifeChange: (Int) -> Unit,
    onLayoutChange: (Layout) -> Unit // Add this callback
) {
    var numberOfPlayers by remember { mutableStateOf(initialNumberOfPlayers) }
    var startingLife by remember { mutableStateOf(initialStartingLife) }
    var layout by remember { mutableStateOf(Layout.GRID) } // State for layout

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


data class PlayerState(
    val name: String,
    val life: Int,
    val poisonCounters: Int = 0,
    val commanderDamage: MutableMap<String, Int> = mutableMapOf() // Key: Opponent, Value: Damage
)

