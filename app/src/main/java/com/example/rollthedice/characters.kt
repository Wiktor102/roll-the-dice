package com.example.rollthedice

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersView() {
    val nav = LocalNavController.current;

    Scaffold(
        topBar = { TopAppBar(title = { Text("Roll The Dice") }) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { nav.navigate("characters/create") },
                icon = { Icon(Icons.Filled.Add, "Stwórz postać") },
                text = { Text(text = "Stwórz postać") },
            )
        }
    ) {
        Text("cgdfrhr", Modifier.padding(it))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCharacterView() {
    val nav = LocalNavController.current;

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Stwórz postać") },
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
        Text("Tu coś będzie", Modifier.padding(it))
    }
}