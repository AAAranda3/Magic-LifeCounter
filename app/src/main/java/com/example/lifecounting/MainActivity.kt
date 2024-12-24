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
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
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
    var numberOfPlayers by remember { mutableIntStateOf(4) }
    var startingLife by remember { mutableIntStateOf(PlayerState().life) }
    var currentLayout by remember { mutableStateOf(Layout.GRID) }


    val playerStates = remember {
        List(numberOfPlayers) {
            mutableStateOf(PlayerState())
        }
    }

    var showSettings by remember { mutableStateOf(false) }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {

        val screenWidth = maxWidth
        val screenHeight = maxHeight


        val columns = when (numberOfPlayers) {
            in 2..4 -> 2
            else -> 3
        }

        val cellWidth = screenWidth / columns
        val cellHeight = screenHeight / 2

        if (numberOfPlayers == 2) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .graphicsLayer(
                            rotationZ = 90f,
                            transformOrigin = TransformOrigin.Center
                        )
                ) {

                    PlayerItem(
                        playerState = playerStates[0],
                        cellWidth = cellWidth,
                        cellHeight = screenHeight
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .graphicsLayer(
                            rotationZ = -90f,
                            transformOrigin = TransformOrigin.Center
                        )
                ) {
                    PlayerItem(
                        playerState = playerStates[1],
                        cellWidth = cellWidth,
                        cellHeight = screenHeight
                    )
                }
            }
        } else if (numberOfPlayers == 3) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {

                    PlayerItem(
                        playerState = playerStates[0],
                        cellWidth = cellWidth,
                        cellHeight = cellHeight
                    )
                    PlayerItem(
                        playerState = playerStates[2],
                        cellWidth = cellWidth,
                        cellHeight = cellHeight
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .graphicsLayer(
                            rotationZ = -90f,
                            transformOrigin = TransformOrigin.Center
                        )
                ) {
                    PlayerItem(
                        playerState = playerStates[1],
                        cellWidth = cellWidth,
                        cellHeight = cellHeight,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        } else if (numberOfPlayers == 5) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    PlayerItem(
                        playerState = playerStates[0],
                        cellWidth = cellWidth,
                        cellHeight = cellHeight
                    )
                    PlayerItem(
                        playerState = playerStates[3],
                        cellWidth = cellWidth,
                        cellHeight = cellHeight
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    PlayerItem(
                        playerState = playerStates[1],
                        cellWidth = cellWidth,
                        cellHeight = cellHeight
                    )
                    PlayerItem(
                        playerState = playerStates[4],
                        cellWidth = cellWidth,
                        cellHeight = cellHeight
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .graphicsLayer(
                            rotationZ = -90f,
                            transformOrigin = TransformOrigin.Center
                        )
                        .offset(y = 60.dp)
                ) {
                    PlayerItem(
                        playerState = playerStates[2],
                        cellWidth = cellWidth,
                        cellHeight = screenHeight / 1.5f
                    )
                }


            }
        } else {
            // Layout for 4 o 6 players, using LazyVerticalGrid
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(playerStates) { index, playerState ->
                    Box(
                        modifier = Modifier
                            .size(cellWidth, cellHeight)
                            .align(Alignment.Center)
                            .padding(8.dp)
                    ) {
                        PlayerItem(
                            playerState = playerStates[index],
                            cellWidth = cellWidth,
                            cellHeight = cellHeight
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlayerItem(
    playerState: MutableState<PlayerState>,
    cellWidth: Dp,
    cellHeight: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(cellWidth, cellHeight)
            .padding(8.dp)
    ) {
        PlayerSection(
            player = playerState.value,
            onLifeChange = { change ->
                playerState.value = playerState.value.copy(
                    life = (playerState.value.life + change).coerceAtLeast(0)
                )
            },
            onCounterChange = { change ->
                playerState.value = playerState.value.copy(
                    poisonCounters = (playerState.value.poisonCounters + change).coerceAtLeast(0)
                )
            },
            modifier = Modifier.fillMaxSize()
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
