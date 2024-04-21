package com.example.rollthedice.characters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.example.rollthedice.LocalNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListView() {
    val nav = LocalNavController.current
    val viewModel = CharacterViewModel.get(LocalContext.current)
    val characters by viewModel.characters.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Roll The Dice") },
                actions = {
                    IconButton(onClick = { nav.navigate("settings") }) {
                        Icon(imageVector = Icons.Filled.Settings, contentDescription = "Ustawienia")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { nav.navigate("characters/create") },
                icon = { Icon(Icons.Filled.Add, "Stwórz postać") },
                text = { Text(text = "Stwórz postać") },
            )
        }
    ) {
        if (characters.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Brak zapisanych postaci")
            }
            return@Scaffold;
        }

        LazyColumn(Modifier.padding(it)) {
            itemsIndexed(characters) { i: Int, it: Character ->
                CharactersListViewItem(it)
                if (i != characters.size - 1) Divider()
            }
        }
    }
}

@Composable
private fun CharactersListViewItem(character: Character) {
    val nav = LocalNavController.current
    val viewModel = CharacterViewModel.get(LocalContext.current)

    var showDeleteDialog by remember { mutableStateOf(false) }
    DeleteAlertDialog(character.name, showDeleteDialog) { showDeleteDialog = it }

    ListItem(
        headlineContent = { Text(character.name) },
        trailingContent = {
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = "Usuń")
            }
        },
        modifier = Modifier.clickable {
            nav.navigate("character/${character.name}")
        }
    )
}