package com.example.madcapstone.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.madcapstone.ui.theme.customTopAppBarColor

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BackNavigationTopBar(onNavigationIconClick: () -> Unit){
    TopAppBar(
        title = { /*TODO*/ },
        navigationIcon = {
            IconButton(onClick = { onNavigationIconClick() }) {
                Icon(
                    Icons.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    modifier = Modifier.size(32.dp),
                )
            }
        },
        colors = customTopAppBarColor(),
    )
}