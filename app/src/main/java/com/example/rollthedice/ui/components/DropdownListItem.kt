package com.example.rollthedice.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DropdownListItem(icon: ImageVector?, label: String, content: @Composable () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 16.dp).height(64.dp)
    ) {
        if (icon != null) {
            Icon(
                icon,
                contentDescription = "Motyw",
                modifier = Modifier.padding(end = 16.dp)
            )
        }
        Text(label, fontSize = 20.sp, modifier = Modifier.padding(end = 32.dp))
        content()
    }
}