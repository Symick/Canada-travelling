package com.example.madcapstone.ui.screens.trips

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.madcapstone.R
import com.example.madcapstone.data.models.roomModels.Trip
import com.example.madcapstone.ui.components.modals.TripsBottomSheet
import com.example.madcapstone.ui.theme.customTopAppBarColor
import com.example.madcapstone.viewmodels.TripViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TripsListScreen(navigateTo: (String) -> Unit, viewModel: TripViewModel) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = { Text(stringResource(R.string.screen_label_trips)) },
            colors = customTopAppBarColor()
        )
    }) {
        ScreenContent(modifier = Modifier.padding(it), viewModel = viewModel)
    }
}

@Composable
private fun ScreenContent(modifier: Modifier, viewModel: TripViewModel) {
    val trips by viewModel.trips.observeAsState()
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween

    ) {
        Text(stringResource(id = R.string.trips), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.padding(8.dp))

        if (trips.isNullOrEmpty()) {
            Text(stringResource(id = R.string.no_trips))
        } else {
            DisplayTrips(trips = trips!!)
        }

        var modalVisible by remember { mutableStateOf(false) }
        Button(onClick = { modalVisible = true }, modifier.fillMaxWidth()) {
            Text(stringResource(id = R.string.create_trip))
        }

        if (modalVisible) {
            TripsBottomSheet(
                onDismissRequest = { modalVisible = false }
            )
        }

    }
}

@Composable
private fun DisplayTrips(trips: List<Trip>) {
    LazyColumn(Modifier.fillMaxWidth()) {
        items(items = trips) {
            TripItem(trip = it)
        }
    }
}

@Composable
private fun TripItem(trip: Trip) {
    Column(Modifier.fillMaxWidth()) {
        if (trip.imageUrl != null) {
            AsyncImage(
                model = trip.imageUrl,
                contentDescription = "Trip image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            )
        } else {
            Image(
                painterResource(R.drawable.canada_flag),
                contentDescription = "Trip image",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
                contentScale = ContentScale.Crop
            )
        }
        Column(Modifier.padding(16.dp)) {
            Text(text = trip.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.padding(4.dp))
            Text("${trip.startDate} → ${trip.endDate}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}