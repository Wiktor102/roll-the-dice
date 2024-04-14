package com.example.rollthedice

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.print.PrintAttributes.Margins
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getSystemService
import dev.ricknout.composesensors.accelerometer.rememberAccelerometerSensorValueAsState
import dev.ricknout.composesensors.getSensor
import dev.ricknout.composesensors.getSensorManager
import dev.ricknout.composesensors.gyroscope.rememberGyroscopeSensorValueAsState
import dev.ricknout.composesensors.isSensorAvailable
import dev.ricknout.composesensors.linearacceleration.getLinearAccelerationSensor
import dev.ricknout.composesensors.linearacceleration.isLinearAccelerationSensorAvailable
import dev.ricknout.composesensors.linearacceleration.rememberLinearAccelerationSensorValueAsState
import dev.ricknout.composesensors.rememberSensorValueAsState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.round
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dice() {
    var selectedDice by rememberSaveable { mutableStateOf<String?>(null) }
    var rolling by rememberSaveable { mutableStateOf(false) }
    var result by remember { mutableStateOf<Int?>(null) }

    fun onDiceSelected(newDice: String?) {
        selectedDice = newDice
        result = null
    }

    fun onRollingStart() {
        rolling = true
        result = null
    }

    fun onRollingEnd() {
        rolling = false
        if (selectedDice == null) return

        val sides = selectedDice!!.substring(1).toInt()
        result = Random.nextInt(1, sides + 1)
    }

    Scaffold (
        topBar = {
            TopAppBar(title = { Text("Wybierz rodziej kostki") })
        }
    ) {
        Column(Modifier.padding(it)) {
            DiceSelector(selectedDice, ::onDiceSelected)
            Column(
                modifier = Modifier
                    .padding(Dp(30.0F))
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    if (rolling) Text("Rzucanie...")
                    if (result != null) Text(result.toString(), fontSize = 48.sp)
                }
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
                onClick = { onDiceSelected(if (selectedDice == item) null else item) },
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