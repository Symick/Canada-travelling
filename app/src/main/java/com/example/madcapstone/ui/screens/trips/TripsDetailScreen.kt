package com.example.madcapstone.ui.screens.trips

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.madcapstone.R
import com.example.madcapstone.ui.components.BackNavigationTopBar
import com.example.madcapstone.ui.theme.customTopAppBarColor
import com.example.madcapstone.viewmodels.TripViewModel

@Composable
fun TripsDetailScreen(navigateUp: () -> Unit, tripViewModel: TripViewModel) {
    Scaffold(topBar = {
        BackNavigationTopBar(navigateUp)
    }) {
        Column(Modifier.padding(it)) {
            Text(text = "trips Detail")
        }
    }
}