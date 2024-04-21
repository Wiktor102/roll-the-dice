package com.example.rollthedice.dice

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
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

    var showDeleteDialog by remember { mutableStateOf(false)}
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = { Icon(imageVector = Icons.Outlined.Warning, contentDescription = "Ostrzeżenie")},
            title = {
                Text(text = "Czy jesteś pewnien?")
            },
            text = {
                Text("Czy na pewno chcesz usunąć całą historię? Ta czynność jest nieodwracalna!")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        rollHistoryViewModel.delete()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Usuń")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Anuluj")
                }
            }
        )
    }

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
                },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Wyczyść historię")
                    }
                }
            )
        }
    ) {
        if (rolls.isEmpty()) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                Text("Brak historii")
            }
            return@Scaffold
        }

        LazyColumn(Modifier.padding(it)) {
            itemsIndexed(rolls) { i, roll ->
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
                if (i != rolls.size - 1) Divider()
            }
        }
    }
}