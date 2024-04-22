package com.example.rollthedice.characters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.get
import com.example.rollthedice.LocalNavController
import com.example.rollthedice.characters.ui.DraggableStatBoxWithChip
import com.example.rollthedice.characters.ui.StatBox
import com.example.rollthedice.characters.ui.Stats
import com.example.rollthedice.ui.components.EnumDropdown
import com.example.rollthedice.utilities.DraggableProvider
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun NewCharacterView() {
    val nav = LocalNavController.current
    var characterName by rememberSaveable { mutableStateOf("") }

    var nameError by rememberSaveable { mutableStateOf<String?>(null) }
    val nameErrorText by remember {
        derivedStateOf<@Composable () -> Unit> {
            @Composable {
                Text(
                    nameError!!
                )
            }
        }
    }

    var characterRace by rememberSaveable { mutableStateOf<CharacterRace?>(null) }
    var characterClass by rememberSaveable { mutableStateOf<CharacterClass?>(null) }
    var characterStats by remember { mutableStateOf(CharacterStats()) }

    var generatorStarted by remember { mutableStateOf(false) }
    var submitted by rememberSaveable { mutableStateOf(false) }

    val characterViewModel =
        ViewModelProvider(LocalContext.current as ViewModelStoreOwner).get<CharacterViewModel>()
    val characters by characterViewModel.characters.collectAsState()

    fun submit() {
        submitted = true
        if (characterName == "") {
            nameError = "To pole jest wymagane"
            return
        }

        if (!characterStats.allNotNull()) return
        if (listOf(characterRace, characterClass, characterStats).contains(null)) return
        if (characters.find { it.name == characterName } != null) {
            nameError = "Postać o takiej nazwie już istnieje"
            return
        }

        nameError = null
        characterViewModel.addCharacter(
            Character(
                name = characterName,
                race = characterRace!!,
                characterClass = characterClass!!,
                stats = characterStats,
                health = 100
            )
        )

        nav.navigateUp()
    }

    @Composable
    fun doneFAB() {
        if (!characterStats.allNotNull()) return
        FloatingActionButton( onClick = ::submit) {
            Icon(imageVector = Icons.Outlined.Done, contentDescription = "Zakończ")
        }
    }

    @Composable
    fun startGeneratorFAB() {
        FloatingActionButton(onClick = { generatorStarted = true }) {
            Icon(imageVector = Icons.Outlined.Casino, contentDescription = "Losuj")
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
        floatingActionButton = { if (generatorStarted) doneFAB() else startGeneratorFAB() }
    ) { padding ->
        DraggableProvider(Modifier.padding(padding)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                TextField(
                    value = characterName,
                    onValueChange = { characterName = it },
                    label = { Text("Nazwa postaci") },
                    isError = submitted && characterName.isEmpty(),
                    supportingText = if (submitted && nameError != null) nameErrorText else null,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    Modifier
                        .padding(top = 10.dp)
                        .height(IntrinsicSize.Min)
                ) {
                    Stats(characterStats, setCharacterStats = { characterStats = it })

                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        EnumDropdown(
                            enum = CharacterRace::class.java,
                            value = characterRace,
                            setValue = { characterRace = it },
                            label = "Rasa postaci",
                            isSubmitted = submitted
                        )
                        EnumDropdown(
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
                            StatBox(
                                label = "Zbroja",
                                value = characterStats.armorClass?.toString() ?: "?"
                            )
                            StatBox(
                                label = "Incjatywa",
                                value = if (characterStats.initiative != null) "+" + characterStats.initiative else "?"
                            )
                            StatBox(
                                label = "Szybkość",
                                value = if (characterRace != null) characterRace!!.speed.toString() else "?"
                            )
                        }

                        StatsGenerator(characterStats, generatorStarted)
                    }
                }
            }
        }
    }
}

@Composable
fun StatsGenerator(
    characterStats: CharacterStats,
    generatorStarted: Boolean
) {
    if (characterStats.allNotNull() || !generatorStarted) return

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .offset(0.dp, (-15).dp)
    ) {
        val randomD20 = Random.nextInt(1, 21)
        Text(
            "Przeciągnij poniższą liczbę do jednej ze statystyk po lewej",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 15.dp)
        )
        DraggableStatBoxWithChip("", randomD20)
    }
}