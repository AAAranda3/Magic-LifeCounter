package com.example.lifecounting

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.lifecounting.composables.PlayerSection
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
    var numberOfPlayers by remember { mutableIntStateOf(3) }
    val playerStates = remember {
        List(numberOfPlayers) { index ->
            mutableStateOf(
                PlayerState(
                    name = "Player ${index + 1}",
                    playerBackground = when (index % 3) {
                        0 -> PlayerBackground.DrawableBackground(R.drawable.redmage)
                        1 -> PlayerBackground.DrawableBackground(R.drawable.eldrazis)
                        else -> PlayerBackground.DrawableBackground(R.drawable.hydra)
                    }
                )
            )
        }
    }

    PlayerLayout(modifier, numberOfPlayers, playerStates)
}

@Composable
private fun PlayerLayout(
    modifier: Modifier,
    numberOfPlayers: Int,
    playerStates: List<MutableState<PlayerState>>
) {
    var showSettingsDialog by remember { mutableStateOf(false) } // State to manage dialog visibility
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {

        val screenWidth = maxWidth
        val screenHeight = maxHeight

        // Define the layout depending on the number of players
        val columns = when (numberOfPlayers) {
            in 2..4 -> 2
            else -> 3
        }

        val cellWidth = screenWidth / columns
        val cellHeight = screenHeight / 2

        when (numberOfPlayers) {
            2 -> TwoPlayersLayout(playerStates, cellWidth, screenHeight)
            3 -> ThreePlayerLayout(playerStates, cellWidth, cellHeight)
            5 -> FivePlayerLayout(playerStates, cellWidth, cellHeight, screenHeight)
            else -> GridPlayerLayout(columns, playerStates, cellWidth, cellHeight)
        }

        // Centered Settings Icon
        Box(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            Icon(
                imageVector = Icons.Default.Settings, // Replace with your desired icon
                contentDescription = "Centered Settings Icon",
                modifier = Modifier
                    .size(35.dp)
                    .background(MaterialTheme.colorScheme.secondary, shape = CircleShape)
                    .clickable {
                        showSettingsDialog = true // Open the dialog when clicked
                    }
            )
        }

        // Settings Dialog
        if (showSettingsDialog) {
            Dialog(
                onDismissRequest = { showSettingsDialog = false }
            ) {
                // Dialog content
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(
                            MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(24.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Sensor Settings", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        // Add your settings content here (e.g., sliders, buttons)
                        Button(onClick = { showSettingsDialog = false }) {
                            Text("Close")
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun BoxWithConstraintsScope.GridPlayerLayout(
    columns: Int,
    playerStates: List<MutableState<PlayerState>>,
    cellWidth: Dp,
    cellHeight: Dp
) {
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
                PlayerStatsView(
                    playerState = playerStates[index],
                    cellWidth = cellWidth,
                    cellHeight = cellHeight
                )
            }
        }
    }
}

@Composable
private fun FivePlayerLayout(
    playerStates: List<MutableState<PlayerState>>,
    cellWidth: Dp,
    cellHeight: Dp,
    screenHeight: Dp
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(1f)) {
            PlayerStatsView(
                playerState = playerStates[0],
                cellWidth = cellWidth,
                cellHeight = cellHeight
            )
            PlayerStatsView(
                playerState = playerStates[3],
                cellWidth = cellWidth,
                cellHeight = cellHeight
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            PlayerStatsView(
                playerState = playerStates[1],
                cellWidth = cellWidth,
                cellHeight = cellHeight
            )
            PlayerStatsView(
                playerState = playerStates[4],
                cellWidth = cellWidth,
                cellHeight = cellHeight
            )
        }

        PlayerStatsView(
            playerState = playerStates[2],
            cellWidth = cellWidth,
            cellHeight = screenHeight / 1.5f,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .fillMaxWidth()
                .graphicsLayer(
                    rotationZ = -90f,
                    transformOrigin = TransformOrigin.Center
                ),
            isRotated = true
        )
    }
}

@Composable
private fun ThreePlayerLayout(
    playerStates: List<MutableState<PlayerState>>,
    cellWidth: Dp,
    cellHeight: Dp
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(1f)) {

            PlayerStatsView(
                playerState = playerStates[0],
                cellWidth = cellWidth,
                cellHeight = cellHeight
            )
            PlayerStatsView(
                playerState = playerStates[2],
                cellWidth = cellWidth,
                cellHeight = cellHeight
            )
        }

        PlayerStatsView(
            playerState = playerStates[1],
            cellWidth = cellWidth,
            cellHeight = cellHeight,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .fillMaxWidth()
                .graphicsLayer(
                    rotationZ = -90f,
                    transformOrigin = TransformOrigin.Center
                ),
            isRotated = true
        )

    }
}

@Composable
private fun TwoPlayersLayout(
    playerStates: List<MutableState<PlayerState>>,
    cellWidth: Dp,
    screenHeight: Dp
) {
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

            PlayerStatsView(
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
            PlayerStatsView(
                playerState = playerStates[1],
                cellWidth = cellWidth,
                cellHeight = screenHeight
            )
        }
    }
}

@Composable
fun PlayerStatsView(
    playerState: MutableState<PlayerState>,
    cellWidth: Dp,
    cellHeight: Dp,
    modifier: Modifier = Modifier,
    isRotated: Boolean = false
) {
    var isDialogVisible by remember { mutableStateOf(false) }
    var selectedBackground by remember { mutableStateOf<PlayerBackground>(playerState.value.playerBackground) }

    Box(
        modifier = modifier
            .size(cellWidth, cellHeight)
    ) {
        // Background logic
        when (val background = selectedBackground) {
            is PlayerBackground.ColorBackground -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(background.color)
                )
            }

            is PlayerBackground.DrawableBackground -> {
                Image(
                    painter = painterResource(id = background.resId),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .then(
                            if (isRotated) Modifier
                                .padding(horizontal = 35.dp)
                                .absoluteOffset(y = (-37).dp) else Modifier
                        ),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    alpha = 0.6f
                )
            }
        }

        // Content (PlayerSection) remains the same
        PlayerSection(
            player = playerState,
            onLifeChange = { change ->
                playerState.value = playerState.value.copy(
                    life = (playerState.value.life + change).coerceAtLeast(0)
                )
            },
            modifier = Modifier.fillMaxSize()
        )

        // Icon to open the background selection dialog
        IconButton(
            onClick = {
                isDialogVisible = true // Show the dialog when clicked
            },
            modifier = Modifier
                .align(Alignment.TopStart)  // Position icon at the top-left
                .padding(16.dp)  // Adjust padding if necessary
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_layers_24),  // Your icon
                contentDescription = "Change Background",
                tint = MaterialTheme.colorScheme.tertiary
            )
        }

        // Background selection dialog
        if (isDialogVisible) {
            BackgroundSelectionDialog(
                onDismissRequest = { isDialogVisible = false },
                onBackgroundSelected = { newBackground ->
                    selectedBackground = newBackground
                }
            )
        }
    }
}

@Composable
fun BackgroundSelectionDialog(
    onDismissRequest: () -> Unit,
    onBackgroundSelected: (PlayerBackground) -> Unit
) {
    val backgroundOptions = listOf(
        PlayerBackground.ColorBackground(Color.Red),
        PlayerBackground.ColorBackground(Color.Green),
        PlayerBackground.ColorBackground(Color.Blue),
        PlayerBackground.DrawableBackground(resId = R.drawable.demon),
        PlayerBackground.DrawableBackground(resId = R.drawable.hydra),
        PlayerBackground.DrawableBackground(resId = R.drawable.redmage),
        PlayerBackground.DrawableBackground(resId = R.drawable.angelserra),
        PlayerBackground.DrawableBackground(resId = R.drawable.blackdragon),
        PlayerBackground.DrawableBackground(resId = R.drawable.reddarkmage),
        PlayerBackground.DrawableBackground(resId = R.drawable.scificity),
        PlayerBackground.DrawableBackground(resId = R.drawable.tetriccity),
        PlayerBackground.DrawableBackground(resId = R.drawable.eldrazis),
        PlayerBackground.DrawableBackground(resId = R.drawable.greenmage),
        PlayerBackground.DrawableBackground(resId = R.drawable.whitemages)
    )

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Select Background") },
        text = {
            LazyColumn {
                items(count = backgroundOptions.size, itemContent = { index ->
                    val background = backgroundOptions[index]

                    Row(
                        modifier = Modifier
                            .clickable {
                                onBackgroundSelected(background)
                                onDismissRequest()
                            }
                            .padding(8.dp)
                    ) {
                        // Display a preview of the background option
                        when (background) {
                            is PlayerBackground.ColorBackground -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .size(height = 80.dp, width = 120.dp)
                                        .background(background.color)
                                )
                            }

                            is PlayerBackground.DrawableBackground -> {
                                Image(
                                    painter = painterResource(id = background.resId),
                                    contentDescription = "Preview",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .size(height = 80.dp, width = 120.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                })
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Close")
            }
        }
    )
}


data class PlayerState(
    val name: String = "Player",
    val life: Int = 40,
    val playerBackground: PlayerBackground,
    val poisonCounters: Int = 0,
    val commanderDamage: Int = 0,
    val energy: Int = 0,
    val experience: Int = 0,
    val charge: Int = 0,
    val loyalty: Int = 0,
    val time: Int = 0
)

sealed class PlayerBackground {
    data class ColorBackground(val color: Color) : PlayerBackground()
    data class DrawableBackground(val resId: Int) : PlayerBackground()
}
