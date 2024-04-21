package com.example.rollthedice.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

data class OutlinedDropdownItem <T> (val icon: ImageVector, val label: String, val value: T)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> OutlinedDropdown(
    list: List<OutlinedDropdownItem<T>>,
    value: T? = null,
    setValue: (T) -> Unit,
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
        OutlinedTextField(
            value =  list.find {it.value == value}?.label ?: "",
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
            list.map {
                DropdownMenuItem(
                    text = { Text(it.label) },
                    onClick = {
                        setValue(it.value)
                        expanded = false
                    },
                    leadingIcon = {
                        Icon(it.icon, contentDescription = it.label)
                    }
                )
            }
        }
    }
}