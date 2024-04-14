package com.example.rollthedice

import android.print.PrintAttributes.Margins
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.Dp

@Composable
fun Dice() {
    val itemList = listOf("D4","D6","D8","D10","D12","D20")
    Column {
        Spacer(modifier = Modifier.size(Dp(10.0F)))
        LazyRow(modifier = Modifier.padding(Dp(10.0F))) {

            items(itemList) { item ->
                Button(onClick = {},modifier = Modifier
                    .defaultMinSize(Dp(120.0F), Dp(80.0F)),
                    shape = RoundedCornerShape(10)
                ) {
                    Text(text = item)
                }
                Spacer(modifier = Modifier.padding(horizontal = Dp(5.0F)))
            }
        }
        Column(modifier = Modifier
            .padding(Dp(30.0F))
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
        ){
            Text(text = "Kostka here")
        }
    }

}
