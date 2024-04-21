package com.example.rollthedice.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rollthedice.LocalNavController
import com.example.rollthedice.ui.components.DropdownListItem
import com.example.rollthedice.ui.components.ExpandableList
import com.example.rollthedice.ui.components.OutlinedDropdown
import com.example.rollthedice.ui.components.OutlinedDropdownItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView() {
    val nav = LocalNavController.current
    val settings = SettingsViewModel.get(LocalContext.current)
    val currentTheme by settings.theme.collectAsState()
    val vibrationsEnabled by settings.vibrations.collectAsState()

    val themes = listOf(
        OutlinedDropdownItem(Icons.Filled.AutoAwesome, "automatyczny", "auto"),
        OutlinedDropdownItem(Icons.Filled.LightMode, "jasny", "light"),
        OutlinedDropdownItem(Icons.Filled.DarkMode, "ciemny", "dark")
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Ustawienia") },
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
        Column(
            Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            DropdownListItem(icon = Icons.Filled.FormatPaint, label = "Motyw") {
                OutlinedDropdown(
                    list = themes,
                    setValue = { nv -> settings.changeTheme(nv) },
                    value = themes.find { to -> to.value == currentTheme }!!.value,
                    label = ""
                )
            }
            Divider(Modifier.padding(top = 12.dp))
            DropdownListItem(icon = Icons.Filled.Vibration, label = "Wibracje") {
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = vibrationsEnabled,
                    onCheckedChange = { on -> settings.toggleVibrations(on) })
            }
            Divider()
            ExpandableList(
                leadingIcon = Icons.Filled.Info,
                label = { Text("O aplikacji", fontSize = 20.sp) }
            ) { padding ->
                Column {
                    ListItem(
                        headlineContent = { Text("Autorzy", fontWeight = FontWeight.Bold) },
                        leadingContent = {
                            Icon(imageVector = Icons.Filled.People, contentDescription = "Autorzy")
                        },
                        modifier = Modifier.padding(padding)
                    )
                    Text(
                        "Wiktor Golicz,\nMikołaj Gaweł-Kucab",
                        modifier = Modifier
                            .padding(padding)
                            .padding(start = 56.dp, bottom = 8.dp)
                            .offset(0.dp, (-8).dp)
                    )
                    Divider(Modifier.offset(56.dp, 0.dp))
                }

                Column {
                    ListItem(
                        headlineContent = { Text("Opis", fontWeight = FontWeight.Bold) },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Filled.Description,
                                contentDescription = "Autorzy"
                            )
                        },
                        modifier = Modifier.padding(padding)
                    )
                    Text(
                        "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
                        modifier = Modifier
                            .padding(padding)
                            .padding(start = 56.dp, bottom = 8.dp, end = 16.dp)
                            .offset(0.dp, (-8).dp)
                    )
                }
            }
            Divider()
        }
    }
}