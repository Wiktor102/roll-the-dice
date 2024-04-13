package com.example.rollthedice.characters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.rollthedice.LocalNavController
import com.example.rollthedice.utilities.LocalSnackbarController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailsView(characterName: String) {
    val nav = LocalNavController.current
    val characterViewModel = CharacterViewModel.get(LocalContext.current)
    val character by characterViewModel.getCharacter(characterName).collectAsState();

    if (character == null) {
        LocalSnackbarController.current.showSnackBar("Brak takiego id");
        nav.navigateUp()
        return;
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(characterName) },
                navigationIcon = {
                    IconButton(onClick = { nav.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Wróć",
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {  }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edytuj",
                        )
                    }
                    IconButton(onClick = {  }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Usuń",
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.Casino, contentDescription = "Rzuć")
            }
        }
    ) {
        Column (Modifier.padding(it)) {
            Text(character!!.race.toString())
        }
    }
}