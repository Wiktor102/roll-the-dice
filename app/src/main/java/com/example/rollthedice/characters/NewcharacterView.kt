package com.example.rollthedice.characters

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.get
import com.example.rollthedice.LocalNavController
import com.example.rollthedice.characters.ui.StatBox
import com.example.rollthedice.characters.ui.Stats
import com.example.rollthedice.ui.components.Dropdown

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun NewCharacterView() {
    val nav = LocalNavController.current
    var characterName by rememberSaveable { mutableStateOf("") }
    val nameErrorText = @Composable { Text("To pole jest wymagane") }

    var characterType by rememberSaveable { mutableStateOf<CharacterRace?>(null) }
    var characterClass by rememberSaveable { mutableStateOf<CharacterClass?>(null) }
    var characterStats by remember { mutableStateOf<CharacterStats?>(null)}

    var submitted by rememberSaveable { mutableStateOf(false) }
    val characterViewModel =
        ViewModelProvider(LocalContext.current as ViewModelStoreOwner).get<CharacterViewModel>()


    fun submit() {
        submitted = true
        if (characterName == "") return
        if (listOf(characterType, characterClass, characterStats).contains(null)) return

        characterViewModel.addCharacter(
            Character(
                name = characterName,
                race = characterType!!,
                characterClass = characterClass!!,
                stats = characterStats!!,
                health = 100
            )
        )

        nav.navigateUp()
    }

    @Composable
    fun generateRandomFAB() {
        FloatingActionButton(onClick = { characterStats = CharacterStats.generate() }) {
            Icon(imageVector = Icons.Outlined.Casino, contentDescription = "Losuj statystyki")
        }
    }

    @Composable
    fun doneFAB() {
        FloatingActionButton(onClick = ::submit) {
            Icon(imageVector = Icons.Outlined.Done, contentDescription = "Zakończ")
        }
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
        floatingActionButton = { if (characterStats == null) generateRandomFAB() else doneFAB() }
    ) { padding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
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

            Row(Modifier.padding(top = 10.dp)) {
                Stats(characterStats)

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Dropdown(
                        enum = CharacterRace::class.java,
                        value = characterType,
                        setValue = { characterType = it },
                        label = "Rasa postaci",
                        isSubmitted = submitted
                    )
                    Dropdown(
                        enum = CharacterClass::class.java,
                        value = characterClass,
                        setValue = { characterClass = it },
                        label = "Klasa postaci",
                        isSubmitted = submitted
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp)
                    ) {
                        StatBox(label = "Zbroja", value = "1")
                        StatBox(label = "Incjatywa", value = "4")
                        StatBox(label = "Szybkość", value = "5")
                    }
                }
            }
        }
    }
}