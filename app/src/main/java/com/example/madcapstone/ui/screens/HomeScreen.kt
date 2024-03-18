package com.example.madcapstone.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.madcapstone.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen() {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text(stringResource(R.string.screen_label_home)) })
    }) {
        Column(Modifier.padding(it)) {
            Text(text = "home Screen")
        }
    }
}