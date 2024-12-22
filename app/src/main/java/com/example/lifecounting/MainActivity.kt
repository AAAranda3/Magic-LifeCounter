package com.example.lifecounting

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.example.lifecounting.composables.CircularSettingsButton
import com.example.lifecounting.composables.Layout
import com.example.lifecounting.composables.PlayerSection
import com.example.lifecounting.composables.PlayerSettingsScreen
import com.example.lifecounting.ui.theme.LifeCountingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        setContent {
            LifeCountingTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    CommanderLifeCounterApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CommanderLifeCounterApp(modifier: Modifier = Modifier) {
    var numberOfPlayers by remember { mutableStateOf(3) } // Change based on the number of players
    var startingLife by remember { mutableStateOf(PlayerState().life) }
    var currentLayout by remember { mutableStateOf(Layout.GRID) }

    // List of player states with MutableState for each player
    val playerStates = remember {
        List(numberOfPlayers) {
            mutableStateOf(PlayerState())
        }
    }

    var showSettings by remember { mutableStateOf(false) }

    // Use BoxWithConstraints to get the screen size
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        // Access screen size
        val screenWidth = maxWidth
        val screenHeight = maxHeight

        // Logic to determine the number of columns depending on the number of players
        val columns = when (numberOfPlayers) {
            in 2..4 -> 2 // If there are 2, 3, or 4 players, use 2 columns
            else -> 3    // More than 4 players, use 3 columns
        }

        // Adjust grid orientation when there are exactly 5 players
        val fivePlayers = numberOfPlayers == 5
        val threePlayers = numberOfPlayers == 3

        val cellWidth = screenWidth / columns


        // Use LazyVerticalGrid for other numbers of players
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns), // Fixed number of columns
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp), // Vertical spacing
            horizontalArrangement = Arrangement.spacedBy(16.dp) // Horizontal spacing
        ) {
            // Extract the value from each MutableState<PlayerState> and use it
            items(playerStates) { playerState ->
                Box(
                    modifier = Modifier
                        .then(
                            if (threePlayers && playerStates.indexOf(playerState) == 1 || fivePlayers && playerStates.indexOf(
                                    playerState
                                ) == 2
                            ) {
                                Modifier
                                    .rotate(90f)
                            } else {
                                Modifier
                            }
                                .align(Alignment.Center)
                        )
                        .width(cellWidth) // Adjust width of each cell
                        .padding(8.dp) // Padding around each cell
                ) {
                    PlayerSection(
                        state = playerState.value,  // Access the value of MutableState
                        onLifeChange = { change ->
                            playerState.value = playerState.value.copy(
                                life = (playerState.value.life + change).coerceAtLeast(0)
                            )
                        },
                        onCounterChange = { change ->
                            playerState.value = playerState.value.copy(
                                poisonCounters = (playerState.value.poisonCounters + change).coerceAtLeast(
                                    0
                                )
                            )
                        },
                        modifier = Modifier.fillMaxSize() // Ensure player section fills the cell
                    )
                }
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


data class PlayerState(
    val life: Int = 40,
    val poisonCounters: Int = 0,
    val commanderDamage: Int = 0,
    val energy: Int = 0,
    val experience: Int = 0,
    val charge: Int = 0,
    val loyalty: Int = 0,
    val time: Int = 0
)
