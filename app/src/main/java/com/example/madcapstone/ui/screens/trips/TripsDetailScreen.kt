package com.example.madcapstone.ui.screens.trips

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.madcapstone.R
import com.example.madcapstone.data.models.roomModels.TripActivity
import com.example.madcapstone.ui.components.BackNavigationTopBar
import com.example.madcapstone.ui.components.TripActivityCard
import com.example.madcapstone.ui.components.modals.AddActivityBottomSheet
import com.example.madcapstone.ui.components.modals.EditActivityBottomSheet
import com.example.madcapstone.ui.components.modals.SimpleDeleteDialog
import com.example.madcapstone.ui.components.utils.CustomDatePicker
import com.example.madcapstone.ui.screens.Screens
import com.example.madcapstone.utils.Utils
import com.example.madcapstone.viewmodels.TripDetailViewModel
import com.example.madcapstone.viewmodels.TripViewModel

@Composable
fun TripsDetailScreen(
    navigateUp: () -> Unit,
    tripViewModel: TripViewModel,
    detailViewModel: TripDetailViewModel,
    navigateTo: (String) -> Unit
) {
    var openAddActivityDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            BackNavigationTopBar(navigateUp)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                          openAddActivityDialog = true
                },
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add activity to trip")
            }
        }
    ) {
        ScreenContent(
            modifier = Modifier.padding(it),
            viewModel = tripViewModel,
            detailViewModel = detailViewModel,
            navigateTo = navigateTo,
            modelOpened = openAddActivityDialog,
            closeDialog = { openAddActivityDialog = false }
        )
    }
}

@Composable
private fun ScreenContent(
    modifier: Modifier,
    viewModel: TripViewModel,
    detailViewModel: TripDetailViewModel,
    navigateTo: (String) -> Unit,
    modelOpened: Boolean = false,
    closeDialog: () -> Unit
) {
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
        LazyColumn(Modifier.padding(16.dp)) {
            if (activities != null) {
                items(activities!!) { activity ->
                    var showDeleteDialog by remember { mutableStateOf(false) }
                    var showEditDialog by remember { mutableStateOf(false) }
                    val tripActivity by detailViewModel.getTripActivity(trip.tripId, activity.activityId).observeAsState()
                    TripActivityCard(activity = activity,
                        onClick = {
                            detailViewModel.setSelectedTripActivity(activity)
                                  navigateTo(Screens.TripActivitiesScreen.route)
                        },
                        onDelete = {
                            showDeleteDialog = true
                        },
                        onEdit = {
                            showEditDialog = true
                        })
                    Spacer(modifier = Modifier.height(16.dp))
                    if (showDeleteDialog) {
                        SimpleDeleteDialog(onDismissRequest = { showDeleteDialog = false },
                            title = stringResource(id = R.string.delete_activity),
                            message = stringResource(
                                id = R.string.delete_activity_message,
                                activity.name,
                                trip.title
                            ),
                            onConfirm = {
                                detailViewModel.deleteTripActivity(
                                    tripActivity!!
                                )
                                showDeleteDialog = false
                            })
                    }
                    if (showEditDialog) {
                        EditActivityBottomSheet(
                            onDismissRequest = { showEditDialog = false },
                            trip = trip,
                            initialDate = selectedDate,
                            activity = tripActivity!!,
                            onActivityEdit = {
                                detailViewModel.updateTripActivity(it)
                                showEditDialog = false
                            })
                    }
                }
            } else {
                item {
                    Text(stringResource(R.string.no_activities_on_this_day))
                }
            }
        }
    }
    if (modelOpened) {
        AddActivityBottomSheet(
            onDismissRequest = closeDialog,
            tripDetailViewModel = detailViewModel,
            onActivityAdd = {
                viewModel.addActivityToTrip(trip, it, selectedDate)
                closeDialog()
            }
        )
    }
}