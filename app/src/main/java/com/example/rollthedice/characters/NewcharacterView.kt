package com.example.rollthedice.characters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.get
import com.example.rollthedice.LocalNavController
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

    var submitted by rememberSaveable { mutableStateOf(false) }
    val characterViewModel =
        ViewModelProvider(LocalContext.current as ViewModelStoreOwner).get<CharacterViewModel>();

    fun submit() {
        submitted = true;
        if (characterName == "") return;
        if (listOf(characterType, characterClass).contains(null)) return;

        characterViewModel.addCharacter(
            Character(
                name = characterName,
                race = characterType!!,
                characterClass = characterClass!!,
                health = 100
            )
        )

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

            Row(Modifier.padding(top = 10.dp)) {
                Stats()

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
                        StatBox(label = "Zbroja", value = 1)
                        StatBox(label = "Incjatywa", value = 4)
                        StatBox(label = "Szybkość", value = 5)
                    }
                }
            }

        }
    }
}

@Composable
private fun Stats() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.padding(end = 20.dp)
    ) {
        items(6) {
            StatBoxWithChip(label = "podpis", value = it, chipValue = it / 2)
        }
    }
}


@Composable
private fun StatBox(label: String, value: Int) {
    val boxHeight = 70.dp
    val boxWidth = 77.dp

    Column {
        Box(
            Modifier
                .width(boxWidth)
                .clip(RoundedCornerShape(5.dp, 5.dp, 0.dp, 0.dp))
                .background(MaterialTheme.colorScheme.inversePrimary)
        ) {
            Text(label, fontSize = 14.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(5.dp))
        }
        Box(
            Modifier
                .size(boxWidth, boxHeight)
                .clip(RoundedCornerShape(0.dp, 0.dp, 5.dp, 5.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text(value.toString(), fontSize = 26.sp, modifier = Modifier.align(Alignment.Center))
        }
    }

}

@Composable
private fun StatBoxWithChip(label: String, value: Int, chipValue: Int) {
    val boxHeight = 70.dp
    val boxWidth = 60.dp
    val chipWidth = boxHeight / 2 * 1.05f
    val chipHeight = 25.dp

    Box(
        Modifier
            .size(boxWidth, boxHeight)
            .clip(RoundedCornerShape(5.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Text(label, fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().offset(0.dp, 3.dp))
        Text(
            value.toString(),
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }

    Box(
        Modifier
            .offset((boxWidth - chipWidth) / 2, chipHeight / -2)
            .size(chipWidth, chipHeight)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.inversePrimary)
    ) {
        Text(chipValue.toString(), modifier = Modifier.align(Alignment.Center))
    }
}