package com.example.rollthedice.characters.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rollthedice.characters.CharacterStats
import com.example.rollthedice.utilities.DragTarget
import com.example.rollthedice.utilities.DropTarget
import com.example.rollthedice.utilities.LocalDragTargetInfo

@Composable
fun Stats(
    characterStats: CharacterStats,
    setCharacterStats: ((cs: CharacterStats) -> Unit)? = null
) {
    fun setValue (characterStats: CharacterStats) {
        if (setCharacterStats == null) return
        setCharacterStats(characterStats)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.padding(end = 20.dp)
    ) {
        DroppableStatBoxWithChip(
            label = "siła",
            setValue = { setValue(characterStats.copy(strength = it)) },
            chipValue = characterStats.strength
        )
        DroppableStatBoxWithChip(
            label = "zwinność",
            setValue = { setValue(characterStats.copy(dexterity = it)) },
            chipValue = characterStats.dexterity
        )
        DroppableStatBoxWithChip(
            label = "budowa?",
            setValue = { setValue(characterStats.copy(constitution = it)) },
            chipValue = characterStats.constitution
        )
        DroppableStatBoxWithChip(
            label = "inteligencja",
            setValue = { setValue(characterStats.copy(intelligence = it)) },
            chipValue = characterStats.intelligence
        )
        DroppableStatBoxWithChip(
            label = "wiedza",
            setValue = { setValue(characterStats.copy(wisdom = it)) },
            chipValue = characterStats.wisdom
        )
        DroppableStatBoxWithChip(
            label = "charyzma",
            setValue = { setValue(characterStats.copy(charisma = it)) },
            chipValue = characterStats.charisma
        )
    }
}

@Composable
fun StatBox(label: String, value: String) {
    val boxHeight = 70.dp
    val boxWidth = 77.dp

    Column {
        Box(
            Modifier
                .width(boxWidth)
                .clip(RoundedCornerShape(5.dp, 5.dp, 0.dp, 0.dp))
                .background(MaterialTheme.colorScheme.inversePrimary)
        ) {
            Text(
                label, fontSize = 14.sp, textAlign = TextAlign.Center, modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
        }
        Box(
            Modifier
                .size(boxWidth, boxHeight)
                .clip(RoundedCornerShape(0.dp, 0.dp, 5.dp, 5.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text(value, fontSize = 26.sp, modifier = Modifier.align(Alignment.Center))
        }
    }

}

@Composable
fun StatBoxWithChip(label: String, value: String, chipValue: String) {
    val boxHeight = 70.dp
    val boxWidth = 62.dp
    val chipWidth = boxHeight / 2 * 1.05f
    val chipHeight = 25.dp

    Box(
        Modifier
            .size(boxWidth, boxHeight)
            .clip(RoundedCornerShape(5.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Text(
                label,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .offset(0.dp, 3.dp)
            )
        }
        Text(
            value,
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }

    Box(
        Modifier
            .offset((boxWidth - chipWidth) / 2, chipHeight / -2)
            .size(chipWidth, chipHeight)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.inversePrimary)
    ) {
        Text(chipValue, modifier = Modifier.align(Alignment.Center))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DraggableStatBoxWithChip(label: String, chipValue: Int, modifier: Modifier = Modifier) {
    DragTarget(
        context = LocalContext.current,
        pagerSize = 3,
        dataToDrop = chipValue,
        modifier = Modifier.wrapContentSize().then(modifier)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            StatBoxWithChip(
                label,
                CharacterStats.getModifierAsString(chipValue),
                chipValue.toString()
            )
        }
    }
}

@Composable
fun DroppableStatBoxWithChip(label: String, chipValue: Int?, setValue: (value: Int) -> Unit) {
    DropTarget<Int>(modifier = Modifier)
    { isInBound, droppedData ->
        var value by remember { mutableStateOf(chipValue) }
        if (!LocalDragTargetInfo.current.itemDropped && isInBound && droppedData != null) {
            LocalDragTargetInfo.current.itemDropped = true
            LocalDragTargetInfo.current.dataToDrop = null
            value = droppedData
            setValue(droppedData)
        }

        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            StatBoxWithChip(
                label,
                if (value != null) CharacterStats.getModifierAsString(value) else "?",
                value?.toString() ?: "?"
            )
        }
    }
}