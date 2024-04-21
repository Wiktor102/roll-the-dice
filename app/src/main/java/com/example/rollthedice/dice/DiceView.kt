package com.example.rollthedice.dice

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rollthedice.LocalNavController
import dev.ricknout.composesensors.linearacceleration.isLinearAccelerationSensorAvailable
import dev.ricknout.composesensors.linearacceleration.rememberLinearAccelerationSensorValueAsState
import kotlinx.coroutines.delay
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import kotlin.math.abs
import kotlin.math.round
import kotlin.random.Random


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiceView() {
    val nav = LocalNavController.current
    val haptic = LocalHapticFeedback.current

    var selectedDice by rememberSaveable { mutableStateOf<String?>("D6") }
    var rolling by rememberSaveable { mutableStateOf(false) }
    var result by rememberSaveable { mutableStateOf<Int?>(null) }

    val rollHistoryViewModel = RollHistoryViewModel.get(LocalContext.current)

    val timer = Timer()
    LaunchedEffect (rolling) {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (!rolling) {
                    timer.cancel()
                }

                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        }, 0, 300)
    }

    fun onDiceSelected(newDice: String?) {
        selectedDice = newDice
        result = null
    }

    fun onRollingStart() {
        if (selectedDice == null) return
        rolling = true
        result = null
    }

    fun onRollingEnd() {
        rolling = false
        val sides = selectedDice!!.substring(1).toInt()
        result = Random.nextInt(1, sides + 1)
        rollHistoryViewModel.addRoll(Roll(selectedDice!!, result!!))
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Wybierz rodziej kostki") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { nav.navigate("history") }) {
                Icon(imageVector = Icons.Outlined.History, contentDescription = "Poprzednie rzuty")
            }
        }
    ) {
        Column(Modifier.padding(it)) {
            DiceSelector(selectedDice, ::onDiceSelected)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(30.dp)
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
                    .weight(1f)
            ) {
                if (rolling) {
                    CircularProgressIndicator(Modifier.padding(bottom = 10.dp))
                    Text("Rzucanie...")
                }
//                    if (result != null) Text(result.toString(), fontSize = 48.sp)
                if (selectedDice != null && !rolling) DiceVisual(selectedDice, result)
            }

            Box (
                Modifier
                    .padding(16.dp)
                    .padding(end = 72.dp)) {
                DiceTrigger(rolling, ::onRollingStart, ::onRollingEnd);
            }
        }
    }
}

@Composable
fun DiceSelector(selectedDice: String?, onDiceSelected: (String?) -> Unit) {
    val itemList = listOf("D4", "D6", "D8", "D10", "D12", "D20")

    LazyRow(modifier = Modifier.padding(10.dp)) {

        items(itemList) { item ->
            Surface(
                onClick = { onDiceSelected(item) },
                color = if (selectedDice == item) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(10),
                modifier = Modifier
                    .size(120.dp, 80.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(text = item)
                }
            }
            Spacer(modifier = Modifier.padding(horizontal = Dp(5.0F)))
        }
    }
}

@Composable
fun DiceTrigger(rolling: Boolean, startRoll: () -> Unit, endRoll: () -> Unit) {
    val sensorAvailable = isLinearAccelerationSensorAvailable()

    if (sensorAvailable) {
        val sensorValue by rememberLinearAccelerationSensorValueAsState()
        val roundedSensorAvgValue = abs(round(sensorValue.value.toList().sum() / 3))

        LaunchedEffect(roundedSensorAvgValue > 1) {
            delay(200);
            if (roundedSensorAvgValue > 1 && !rolling) startRoll()
        }

        LaunchedEffect(roundedSensorAvgValue.toInt() == 0) {
            delay(200);
            if (roundedSensorAvgValue.toInt() == 0 && rolling) endRoll()
        }

        if (rolling) return
        Text("Potrząśnij urządzeniem aby rzucić", fontSize = 24.sp, textAlign = TextAlign.Center)
        return
    }

    if (!rolling) {
        Button(onClick = startRoll) {
            Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "Rzuć")
            Text("Rzuć", Modifier.padding(start = 8.dp))
        }
    } else {
        Button(onClick = endRoll) {
            Icon(imageVector = Icons.Filled.Stop, contentDescription = "Zatrzymaj")
            Text("Zatrzymaj", Modifier.padding(start = 8.dp))
        }
    }
}


data class DiceOffset(val x: Dp, val y: Dp, val font: TextUnit)
@Composable
fun DiceVisual(selectedDice: String?, result: Int?) {
    val context = LocalContext.current
    val drawableId = remember(selectedDice) {
        context.resources.getIdentifier(
            selectedDice!!.lowercase(Locale.ROOT),
            "drawable",
            context.packageName
        )
    }

    val numberOffset = mapOf(
        "D4" to DiceOffset(0.dp, 20.dp, 90.sp),
        "D6" to DiceOffset(0.dp, 0.dp, 90.sp),
        "D8" to DiceOffset(0.dp, 0.dp, 90.sp),
        "D10" to DiceOffset(0.dp, (-25).dp, 90.sp),
        "D12" to DiceOffset(0.dp, 0.dp, 90.sp),
        "D20" to DiceOffset(0.dp, 0.dp, 70.sp),
    )

    var sizeImage by remember { mutableStateOf(IntSize.Zero) }
    val gradient =
        Brush.radialGradient(0f to Color(0x00000000), 1f to MaterialTheme.colorScheme.surface)

    Box {
        Image(painter = painterResource(drawableId), contentDescription = "Kostka")
        Text(
            result?.toString() ?: "?",
            color = MaterialTheme.colorScheme.primary,
            style = TextStyle.Default.copy(
                fontSize = numberOffset[selectedDice]!!.font,
                drawStyle = Stroke(
                    miter = 10f,
                    width = 7f,
                    join = StrokeJoin.Round
                )
            ),
            modifier = Modifier
                .align(Alignment.Center)
                .offset(numberOffset[selectedDice]!!.x, numberOffset[selectedDice]!!.y)
                .onGloballyPositioned {
                    sizeImage = it.size
                }
        )
//        Box(modifier = Modifier.matchParentSize().background(gradient))
    }
}