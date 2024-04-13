package com.example.rollthedice

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersListView() {
    val nav = LocalNavController.current;
    val viewModel = CharactersViewModel.get(LocalContext.current);
    val characters by viewModel.characters.collectAsState();

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
        if (characters.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Nic tu nie ma")
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
    val nav = LocalNavController.current;

    ListItem(
        headlineContent = { Text(character.name) },
        trailingContent = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "Opcje")
            }
        },
        modifier = Modifier.clickable {
            nav.navigate("character/${character.name}")
        }
    )
}