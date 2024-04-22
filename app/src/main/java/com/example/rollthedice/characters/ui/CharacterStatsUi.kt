package com.example.rollthedice.characters.ui

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rollthedice.characters.CharacterRace
import com.example.rollthedice.characters.CharacterStats
import com.example.rollthedice.utilities.DragTarget
import com.example.rollthedice.utilities.DropTarget
import com.example.rollthedice.utilities.LocalDragTargetInfo
import com.example.rollthedice.utilities.conditional

@Composable
fun Stats(
    characterStats: CharacterStats,
    setCharacterStats: ((cs: CharacterStats) -> Unit)? = null,
    race: CharacterRace? = null
) {
    fun setValue(characterStats: CharacterStats) {
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
            chipValue = characterStats.strength,
            race = race
        )
        DroppableStatBoxWithChip(
            label = "zwinność",
            setValue = { setValue(characterStats.copy(dexterity = it)) },
            chipValue = characterStats.dexterity,
            race = race
        )
        DroppableStatBoxWithChip(
            label = "budowa",
            setValue = { setValue(characterStats.copy(constitution = it)) },
            chipValue = characterStats.constitution,
            race = race
        )
        DroppableStatBoxWithChip(
            label = "inteligencja",
            setValue = { setValue(characterStats.copy(intelligence = it)) },
            chipValue = characterStats.intelligence,
            race = race
        )
        DroppableStatBoxWithChip(
            label = "wiedza",
            setValue = { setValue(characterStats.copy(wisdom = it)) },
            chipValue = characterStats.wisdom,
            race = race
        )
        DroppableStatBoxWithChip(
            label = "charyzma",
            setValue = { setValue(characterStats.copy(charisma = it)) },
            chipValue = characterStats.charisma,
            race = race
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

enum class StatBoxWithChipStyle {
    DEFAULT(),
    OUTLINED(),
    OUTLINED_ACTIVE();
}

@Composable
fun StatBoxWithChip(
    label: String,
    value: String,
    chipValue: String,
    style: StatBoxWithChipStyle = StatBoxWithChipStyle.DEFAULT
) {
    val boxHeight = 70.dp
    val boxWidth = 62.dp
    val chipWidth = boxHeight / 2 * 1.05f
    val chipHeight = 25.dp
    val colorScheme = MaterialTheme.colorScheme
    val bg = remember { Animatable(Color.Gray) }

    LaunchedEffect(style) {
        if (style == StatBoxWithChipStyle.DEFAULT) {
            bg.animateTo(colorScheme.surfaceVariant, animationSpec = tween(200))
        } else {
            bg.animateTo(Color.Transparent, animationSpec = tween(200))
        }
    }

    Box(
        Modifier
            .size(boxWidth, boxHeight)
            .clip(RoundedCornerShape(5.dp))
            .background(bg.value)
            .conditional(style == StatBoxWithChipStyle.OUTLINED) {
                border(BorderStroke(1.dp, colorScheme.onSurface), RoundedCornerShape(5.dp))
            }
            .conditional(style == StatBoxWithChipStyle.OUTLINED_ACTIVE) {
                border(BorderStroke(1.dp, colorScheme.primary), RoundedCornerShape(5.dp))
            }
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

@Composable
fun DraggableStatBoxWithChip(label: String, chipValue: Int, modifier: Modifier = Modifier) {
    DragTarget(
        dataToDrop = chipValue,
        modifier = Modifier
            .wrapContentSize()
            .then(modifier)
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
fun DroppableStatBoxWithChip(label: String, chipValue: Int?, setValue: (value: Int) -> Unit, race: CharacterRace?) {
    DropTarget<Int>(modifier = Modifier)
    { isInBound, droppedData ->
        var value by remember { mutableStateOf(chipValue) }
        var style = if (value == null) StatBoxWithChipStyle.OUTLINED else StatBoxWithChipStyle.DEFAULT
        if (!LocalDragTargetInfo.current.itemDropped && isInBound && value == null) style = StatBoxWithChipStyle.OUTLINED_ACTIVE

        if (!LocalDragTargetInfo.current.itemDropped && isInBound && droppedData != null && value == null) {
            LocalDragTargetInfo.current.itemDropped = true
            LocalDragTargetInfo.current.dataToDrop = null
            value = droppedData

            when (race) {
                CharacterRace.HUMAN -> {
                    value = value!! + 1
                }
                CharacterRace.DWARF -> {
                    if (label == "budowa") value = value!! + 2
                }
                CharacterRace.ELF -> {
                    if (label == "zwinność") value = value!! + 2
                }
                CharacterRace.HALF_ELF -> {
                    if (label == "charyzma") value = value!! + 2 /* TODO */
                }
                CharacterRace.HALFLING -> {
                    if (label == "zwinność") value = value!! + 2
                }
                CharacterRace.GNOME -> {
                    if (label == "inteligencja") value = value!! + 2
                }
                CharacterRace.DRAGONBORN -> {
                    if (label == "siła") value = value!! + 2
                    if (label == "charyzma") value = value!! + 1
                }
                CharacterRace.HALF_ORC -> {
                    if (label == "siła") value = value!! + 2
                    if (label == "budowa") value = value!! + 1
                }
                CharacterRace.TIEFLING -> {
                    if (label == "charyzma") value = value!! + 2
                    if (label == "inteligencja") value = value!! + 1
                }
                else -> {}
            }

            setValue(value!!)
        }

        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            StatBoxWithChip(
                label,
                if (value != null) CharacterStats.getModifierAsString(value) else "?",
                value?.toString() ?: "?",
                style = style
            )
        }
    }
}