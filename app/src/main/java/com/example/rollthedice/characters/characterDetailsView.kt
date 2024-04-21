package com.example.rollthedice.characters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rollthedice.LocalNavController
import com.example.rollthedice.characters.ui.StatBox
import com.example.rollthedice.characters.ui.Stats
import com.example.rollthedice.utilities.LocalSnackbarController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailsView(characterName: String) {
    val nav = LocalNavController.current
    val characterViewModel = CharacterViewModel.get(LocalContext.current)
    val character by characterViewModel.getCharacter(characterName).collectAsState();

    if (character == null) {
        LocalSnackbarController.current.showSnackBar("Brak takiego id")
        nav.navigateUp()
        return
    }

    var showDeleteDialog by remember { mutableStateOf(false) }
    DeleteAlertDialog(characterName, showDeleteDialog) { showDeleteDialog = it }

    Scaffold(
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
//                    IconButton(onClick = { }) {
//                        Icon(
//                            imageVector = Icons.Filled.Edit,
//                            contentDescription = "Edytuj",
//                        )
//                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
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
                Icon(imageVector = Icons.Outlined.Casino, contentDescription = "Rzuć")
            }
        }
    ) {
        Row(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Stats(character!!.stats)

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        character!!.race.toString(),
                        fontSize = 20.sp,
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(8.dp)
                    )
                    Text(
                        character!!.characterClass.toString(),
                        fontSize = 20.sp,
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(8.dp)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    StatBox(label = "Zbroja", value = "1")
                    StatBox(label = "Incjatywa", value = "+" + character!!.stats.initiative)
                    StatBox(label = "Szybkość", value = "5")
                }
            }

        }
    }
}

@Composable
fun DeleteAlertDialog (characterName: String, show: Boolean, setShow: (newState: Boolean) -> Unit) {
    val nav = LocalNavController.current
    val characterViewModel = CharacterViewModel.get(LocalContext.current)

    if (show) {
        val spanStyles = listOf(
            AnnotatedString.Range(
                SpanStyle(fontWeight = FontWeight.Bold),
                start = 34,
                end = 34 + characterName.length
            )
        )

        AlertDialog(
            onDismissRequest = { setShow(false) },
            icon = { Icon(imageVector = Icons.Outlined.Warning, contentDescription = "Ostrzeżenie")},
            title = {
                Text(text = "Czy jesteś pewnien?")
            },
            text = {
                Text(
                    text = AnnotatedString(
                        "Czy na pewno chcesz usunąć postać ${characterName}?",
                        spanStyles = spanStyles
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        characterViewModel.deleteCharacter(characterName)
                        nav.navigateUp()
                        setShow(false)
                    }
                ) {
                    Text("Usuń")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { setShow(false) }
                ) {
                    Text("Anuluj")
                }
            }
        )
    }
}