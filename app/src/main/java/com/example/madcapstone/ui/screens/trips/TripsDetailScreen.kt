package com.example.madcapstone.ui.screens.trips

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.madcapstone.R
import com.example.madcapstone.ui.components.BackNavigationTopBar
import com.example.madcapstone.ui.components.utils.CustomDatePicker
import com.example.madcapstone.utils.Utils
import com.example.madcapstone.viewmodels.TripViewModel

@Composable
fun TripsDetailScreen(navigateUp: () -> Unit, tripViewModel: TripViewModel) {
    Scaffold(
        topBar = {
            BackNavigationTopBar(navigateUp)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add activity to trip")
            }
        }
    ) {
        ScreenContent(modifier = Modifier.padding(it), viewModel = tripViewModel)
    }
}

@Composable
private fun ScreenContent(modifier: Modifier, viewModel: TripViewModel) {
    val trip = viewModel.selectedTrip!!
    var selectedDate by remember { mutableStateOf(trip.startDate) }
    val activities by viewModel.getTripActivities(trip.tripId, selectedDate).observeAsState()
    Column(modifier) {
        AsyncImage(
            model = trip.imageUrl,
            contentDescription = "Trip image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 6f),
            fallback = painterResource(id = R.drawable.canada_flag)
        )
        Row(modifier = Modifier.padding(16.dp)) {
            Column(Modifier.weight(0.65f)) {
                Text(text = trip.title, style = MaterialTheme.typography.headlineMedium)
                Text(
                    "${Utils.formatLocaleDate(trip.startDate)} → ${Utils.formatLocaleDate(trip.endDate)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            CustomDatePicker(
                modifier = Modifier.weight(0.35f),
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it },
                minDate = trip.startDate,
                maxDate = trip.endDate
            )
        }
        if (!activities.isNullOrEmpty()) {
            Text(
                text = "Activities",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
            activities?.forEach { activity ->
                Text(
                    text = activity.name,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }

}