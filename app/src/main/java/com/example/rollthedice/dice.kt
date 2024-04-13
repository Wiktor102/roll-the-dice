package com.example.rollthedice


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.rollthedice.cube.RotatingCube
import com.example.rollthedice.cube.rememberCubeState

@Composable
fun Dice() {
    val coroutineScope = rememberCoroutineScope()
    val cubeState = rememberCubeState(coroutineScope)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            RotatingCube(
                modifier = Modifier.align(Alignment.Center),
                cubeState = cubeState,
                scaleFactor = 1.55f
            )
        }
        IconButton(onClick = { cubeState.playPause() }) {
            if (cubeState.isPaused) {
                PlaybackControlIcon(
                    imageVector = Icons.Default.PlayArrow,
                    tint = Color.Black
                )
            } else {
                PlaybackControlIcon(
                    imageVector = Icons.Default.Pause,
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
fun PlaybackControlIcon(imageVector: ImageVector, tint: Color) {
    Icon(
        modifier = Modifier.size(50.dp),
        imageVector = imageVector,
        contentDescription = null,
        tint = tint
    )
}