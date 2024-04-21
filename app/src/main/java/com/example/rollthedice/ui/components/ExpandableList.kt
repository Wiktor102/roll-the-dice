package com.example.rollthedice.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.rollthedice.utilities.conditional

@Composable
fun ExpandableList(
    leadingIcon: ImageVector,
    label: @Composable () -> Unit,
    content: @Composable (padding: PaddingValues) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var currentRotation by remember { mutableFloatStateOf(0f) }
    val rotation = remember { Animatable(currentRotation) }

    LaunchedEffect (expanded) {
        currentRotation = if (expanded) 180f else 0f
        rotation.animateTo(targetValue = currentRotation)
    }

    Column (
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.animateContentSize()
    ) {
        ListItem(
            leadingContent = { Icon(imageVector = leadingIcon, contentDescription = "") },
            headlineContent = label,
            trailingContent = {
                Icon(
                    imageVector = Icons.Filled.ExpandMore,
                    contentDescription = "Więcej",
                    modifier = Modifier.rotate(rotation.value)
                )
            },
            modifier = Modifier
                .height(64.dp)
                .clickable { expanded = !expanded }
        )
        if (expanded) content(PaddingValues(start = 40.dp))
    }
}
