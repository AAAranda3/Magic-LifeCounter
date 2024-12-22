package com.example.lifecounting.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp


@Composable
fun FloatingIconCounter(
    icon: Painter,
    contentDescription: String,
    counterValue: Int,
    onCounterChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showButtons by remember { mutableStateOf(false) }

    Box(modifier = modifier.padding(horizontal = 8.dp)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Conditionally add space for buttons
            if (showButtons) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    FloatingActionButton(
                        onClick = { onCounterChange(1) },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Text("+")
                    }

                    FloatingActionButton(
                        onClick = { onCounterChange(-1) },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Text("-")
                    }
                }
            }

            Icon(
                painter = icon,
                contentDescription = contentDescription,
                modifier = Modifier
                    .size(30.dp)
                    .clickable { showButtons = !showButtons }
            )
            Spacer(modifier = Modifier.height(4.dp)) // Adjust height between elements
            Text(
                text = counterValue.toString(),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

