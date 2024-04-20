package com.example.rollthedice.characters.ui

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rollthedice.characters.CharacterStats

@Composable
fun Stats(characterStats: CharacterStats?) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.padding(end = 20.dp)
    ) {
        StatBoxWithChip(
            label = "siła",
            value = CharacterStats.getModifierAsString(characterStats?.strength),
            chipValue = characterStats?.strength?.toString() ?: "?"
        )
        StatBoxWithChip(
            label = "zwinność",
            value = CharacterStats.getModifierAsString(characterStats?.dexterity),
            chipValue = characterStats?.dexterity?.toString() ?: "?"
        )
        StatBoxWithChip(
            label = "budowa?",
            value = CharacterStats.getModifierAsString(characterStats?.constitution),
            chipValue = characterStats?.constitution?.toString() ?: "?"
        )
        StatBoxWithChip(
            label = "inteligencja",
            value = CharacterStats.getModifierAsString(characterStats?.intelligence),
            chipValue = characterStats?.intelligence?.toString() ?: "?"
        )
        StatBoxWithChip(
            label = "wiedza",
            value = CharacterStats.getModifierAsString(characterStats?.wisdom),
            chipValue = characterStats?.wisdom?.toString() ?: "?"
        )
        StatBoxWithChip(
            label = "charyzma",
            value = CharacterStats.getModifierAsString(characterStats?.charisma),
            chipValue = characterStats?.charisma?.toString() ?: "?"
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
        Row (horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
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
