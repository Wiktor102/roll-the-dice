package com.example.rollthedice.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <E : Enum<E>> Dropdown(
    enum: Class<E>,
    value: E?,
    setValue: (E) -> Unit,
    isSubmitted: Boolean = false,
    label: String,
) {
    var expanded by remember { mutableStateOf(false) }
    val isError = isSubmitted && value == null;
    val errorText = @Composable{
        if (isError) {
            Text("Wybierz z listy")
        }
    };

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth(),
    ) {
        TextField(
            value = value?.toString() ?: "",
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded);
            },
            isError = isError,
            supportingText = if (isError) errorText else null,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        );

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            enum.enumConstants?.map {
                DropdownMenuItem(
                    text = { Text(it.toString()) },
                    onClick = {
                        setValue(it);
                        expanded = false;
                    },
                    leadingIcon = {
                        Icon(Icons.Filled.Person, contentDescription = "Nauczyciel")
                    }
                )
            }
        }
    }
}