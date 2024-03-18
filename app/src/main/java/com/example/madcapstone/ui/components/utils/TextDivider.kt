package com.example.madcapstone.ui.components.utils

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TextDivider(text: String, modifier: Modifier = Modifier, thickness: Dp = 1.dp) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Divider(thickness = thickness, modifier = Modifier.weight(1f).clip(
            RoundedCornerShape(topStartPercent = 50, bottomStartPercent = 50)
        ))
        Text(text = text, modifier = Modifier.padding(horizontal = 8.dp))
        Divider(thickness = thickness, modifier = Modifier.weight(1f).clip(
            RoundedCornerShape(topEndPercent = 50, bottomEndPercent = 50)
        ))
    }
}