package com.example.rollthedice.characters

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
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
        FloatingActionButton(onClick = ::submit) {
            Icon(imageVector = Icons.Outlined.Done, contentDescription = "Zakończ")
        }
    }

    @Composable
    fun startGeneratorFAB() {
        if (characterRace == null) return
        FloatingActionButton(onClick = { generatorStarted = true }) {
            Icon(imageVector = Icons.Outlined.Casino, contentDescription = "Losuj")
        }
    }

    LaunchedEffect(characterRace) {
        characterStats = CharacterStats()
    }

    if (characterRace == CharacterRace.HALF_ELF) {
        HalfElfDialog(characterStats) { characterStats = it }
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
                    Stats(
                        characterStats,
                        setCharacterStats = { characterStats = it },
                        race = characterRace
                    )

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

                        StatsGenerator(characterStats, characterRace, generatorStarted)
                    }
                }
            }
        }
    }
}

@Composable
fun StatsGenerator(
    stats: CharacterStats,
    race: CharacterRace?,
    generatorStarted: Boolean
) {
    if (stats.allNotNull() || !generatorStarted || race == null) return

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

typealias CharacterStatsFunction = (CharacterStats) -> CharacterStats
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalfElfDialog(characterStats: CharacterStats, setCharacterStats: (nv: CharacterStats) -> Unit) {
    var dialogOpen by remember { mutableStateOf(false) }
    var dialogWasOpened by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(listOf<String>()) }

    if (characterStats.allNotNull()) {
        dialogOpen = true
    }

    val map = mapOf<String, CharacterStatsFunction>(
        "siła" to { s -> s.copy(strength = s.strength!! + 1) },
        "zwinność" to { s -> s.copy(dexterity = s.dexterity!! + 1) },
        "budowa" to { s -> s.copy(constitution = s.constitution!! + 1) },
        "inteligencja" to { s -> s.copy(intelligence = s.intelligence!! + 1) },
        "wiedza" to { s -> s.copy(wisdom = s.wisdom!! + 1) },
        "charyzma" to { s -> s.copy(charisma = s.charisma!! + 1) }
    )

    if (!dialogOpen || dialogWasOpened) return
    AlertDialog(
        onDismissRequest = { dialogOpen = false },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clip(RoundedCornerShape(28.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp)
        ) {
            Icon(imageVector = Icons.Outlined.Warning, contentDescription = "Ostrzeżenie")
            Text(
                "Dodatkowe umiejętności",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Divider()
            map.keys.map {
                ListItem(
                    headlineContent = { Text(it) },
                    leadingContent = {
                        Checkbox(
                            checked = selected.contains(it),
                            onCheckedChange = { o ->
                                selected = if (o) {
                                    selected + it
                                } else {
                                    val ml = selected.toMutableList()
                                    ml.remove(it)
                                    ml
                                }
                            },
                            enabled = selected.size < 2
                        )
                    }
                )
            }
            Divider()
            TextButton(
                onClick = {
                    setCharacterStats(map[selected[1]]!!(map[selected[0]]!!(characterStats)))
                    dialogOpen = false
                    dialogWasOpened = true
                },
                enabled = selected.size == 2
            ) {
                Text("Potwierdź")
            }
        }
    }
}