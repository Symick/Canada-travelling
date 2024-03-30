package com.example.madcapstone.ui.screens.trips

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.madcapstone.R
import com.example.madcapstone.data.models.roomModels.Trip
import com.example.madcapstone.ui.components.modals.SimpleDeleteDialog
import com.example.madcapstone.ui.components.modals.TripsBottomSheet
import com.example.madcapstone.ui.screens.Screens
import com.example.madcapstone.ui.theme.customTopAppBarColor
import com.example.madcapstone.utils.Utils
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
        ScreenContent(
            modifier = Modifier.padding(it),
            viewModel = viewModel,
            navigateTo = navigateTo
        )
    }
}

@Composable
private fun ScreenContent(
    modifier: Modifier,
    viewModel: TripViewModel,
    navigateTo: (String) -> Unit
) {
    val trips by viewModel.trips.observeAsState()
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(stringResource(id = R.string.trips), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.padding(8.dp))

        if (trips.isNullOrEmpty()) {
            Text(stringResource(id = R.string.no_trips))
        } else {
            var deleteModalVisible by remember { mutableStateOf(false) }
            var tripToDelete: Trip? by remember { mutableStateOf(null) }
            DisplayTrips(modifier = Modifier.weight(1f), trips = trips!!,
                onTripDelete = {
                    tripToDelete = it
                    deleteModalVisible = true
                },
                onTripSelect = {
                    viewModel.selectTrip(it)
                    navigateTo(Screens.TripsDetailScreen.route)
                }
            )
            if (deleteModalVisible) {
                SimpleDeleteDialog(
                    onDismissRequest = { deleteModalVisible = false },
                    title = stringResource(id = R.string.delete_trip),
                    message = stringResource(R.string.delete_trip_message, tripToDelete?.title!!),
                    onConfirm = {
                        viewModel.deleteTrip(tripToDelete!!)
                        deleteModalVisible = false
                    })
            }
        }

        var createSheetVisible by remember { mutableStateOf(false) }
        Button(onClick = { createSheetVisible = true }, Modifier.fillMaxWidth()) {
            Text(stringResource(id = R.string.create_trip))
        }

        if (createSheetVisible) {
            TripsBottomSheet(
                onDismissRequest = { createSheetVisible = false },
                createTrip = { trip ->
                    viewModel.insertTrip(trip)
                    createSheetVisible = false
                }
            )
        }
    }
}

@Composable
private fun DisplayTrips(
    modifier: Modifier,
    trips: List<Trip>,
    onTripDelete: (Trip) -> Unit,
    onTripSelect: (Trip) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(items = trips) {
            TripItem(trip = it, onDelete = { onTripDelete(it) }, onSelect = { onTripSelect(it) })
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun TripItem(trip: Trip, onDelete: () -> Unit, onSelect: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable { onSelect() }) {
        Box(contentAlignment = Alignment.TopEnd) {
            AsyncImage(
                model = trip.imageUrl,
                contentDescription = "Trip image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
                fallback = painterResource(id = R.drawable.canada_flag)
            )

            FilledIconButton(
                onClick = onDelete,
                colors = IconButtonDefaults.filledIconButtonColors(
                    contentColor = Color.DarkGray,
                    containerColor = Color.White
                ),
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete trip")
            }
        }
        Column {
            Text(text = trip.title, style = MaterialTheme.typography.titleMedium)
            Text(
                "${Utils.formatLocaleDate(trip.startDate)} → ${Utils.formatLocaleDate(trip.endDate)}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}