package com.example.rollthedice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
@Preview
@Composable
fun NewCharacterView() {
    val nav = LocalNavController.current
    var characterName by rememberSaveable { mutableStateOf("") }
    val nameErrorText = @Composable{ Text("To pole jest wymagane") }

    var characterType by rememberSaveable { mutableStateOf<CharacterRace?>(null) }
    var characterClass by rememberSaveable { mutableStateOf<CharacterClass?>(null) }
    var characterAlignment by rememberSaveable { mutableStateOf<CharacterAlignment?>(null) }

    var submitted by rememberSaveable { mutableStateOf(false) }

    fun submit() {
        submitted = true;
        if (characterName == "") return;
        if (listOf(characterType, characterClass, characterAlignment).contains(null)) return;

        nav.navigateUp();
    }

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
        },
        floatingActionButton = {
            FloatingActionButton(onClick = ::submit) {
                Icon(imageVector = Icons.Outlined.Done, contentDescription = "Zakończ")
            }
        }
    ) { padding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
        ) {
            TextField(
                value = characterName,
                onValueChange = { characterName = it },
                label = { Text("Nazwa postaci") },
                isError = submitted && characterName.isEmpty(),
                supportingText = if (submitted && characterName.isEmpty()) nameErrorText else null,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth()
            )
            Dropdown(
                enum = CharacterRace::class.java,
                value = characterType,
                setValue = {characterType = it},
                label = "Rasa postaci",
                isSubmitted = submitted
            )
            Dropdown(
                enum = CharacterClass::class.java,
                value = characterClass,
                setValue = {characterClass = it},
                label = "Klasa postaci",
                isSubmitted = submitted
            )
            Dropdown(
                enum = CharacterAlignment::class.java,
                value = characterAlignment,
                setValue = {characterAlignment = it},
                label = "Nastawienie postaci",
                isSubmitted = submitted
            )

        }
    }
}