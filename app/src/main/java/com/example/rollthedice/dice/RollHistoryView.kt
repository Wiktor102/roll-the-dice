package com.example.rollthedice.dice

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rollthedice.LocalNavController
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RollHistoryView() {
    val nav = LocalNavController.current
    val rollHistoryViewModel = RollHistoryViewModel.get(LocalContext.current)
    val rolls by rollHistoryViewModel.rolls.collectAsState()
    val df = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historia rzutów") },
                navigationIcon = {
                    IconButton(onClick = { nav.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Wróć",
                        )
                    }
                }
            )
        }
    ) {
        if (rolls.isEmpty()) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(it).fillMaxSize()
            ) {
                Text("Brak historii")
            }
            return@Scaffold
        }

        LazyColumn(Modifier.padding(it)) {
            items(rolls) { roll ->
                val drawableId = LocalContext.current.resources.getIdentifier(
                    roll.dice.lowercase(Locale.ROOT),
                    "drawable",
                    LocalContext.current.packageName
                )

                ListItem(
                    leadingContent = {
                        Image(
                            painter = painterResource(drawableId),
                            contentDescription = "Kostka",
                            modifier = Modifier.size(56.dp)
                        )
                    },
                    headlineContent = { Text(roll.dice) },
                    supportingContent = { Text(roll.getDate().format(df)) },
                    trailingContent = { Text(roll.result.toString(), fontSize = 32.sp) }
                )
            }
        }
    }
}