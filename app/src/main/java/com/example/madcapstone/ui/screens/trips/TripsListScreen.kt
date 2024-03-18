package com.example.madcapstone.ui.screens.trips

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.madcapstone.R
import com.example.madcapstone.ui.screens.Screens
import com.example.madcapstone.ui.theme.customTopAppBarColor

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TripsListScreen(navigateTo: (String) -> Unit) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text(stringResource(R.string.screen_label_trips)) },
            colors = customTopAppBarColor()
        )
    }) {
        Column(Modifier.padding(it)) {
            Text(text = "trips Screen")
            Button(onClick = { navigateTo(Screens.TripsDetailScreen.route) }) {
                Text("Go to trips detail")
            }
        }
    }
}