package com.example.madcapstone.ui.components.modals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.madcapstone.R
import com.example.madcapstone.data.models.firebaseModels.FirestoreActivity
import com.example.madcapstone.data.models.roomModels.RoomActivity
import com.example.madcapstone.data.models.roomModels.Trip
import com.example.madcapstone.data.util.ModelConverter
import com.example.madcapstone.data.util.Resource
import com.example.madcapstone.ui.components.SmallActivityCard
import com.example.madcapstone.ui.components.utils.CustomDatePicker
import com.example.madcapstone.ui.components.utils.TripsDropDown
import com.example.madcapstone.utils.Utils
import com.example.madcapstone.viewmodels.TripDetailViewModel
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddActivityBottomSheet(
    onDismissRequest: () -> Unit,
    trips: List<Trip>,
    onActivityAdd: (Trip, Date) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.add_activity),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            var expandedDropDown by remember { mutableStateOf(false) }
            var selectedTrip by remember { mutableStateOf<Trip?>(null) }
            var selectedDate by remember { mutableStateOf(Date()) }
            var nonSelected by remember { mutableStateOf(false) }
            val context = LocalContext.current
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TripsDropDown(
                    modifier = Modifier.weight(0.5f, false),
                    selectedTrip = selectedTrip,
                    trips = trips,
                    isExpanded = expandedDropDown,
                    onExpandedChange = { expandedDropDown = it },
                    onTripSelected = {
                        selectedTrip = it
                        selectedDate = it.startDate
                    },
                    hasError = nonSelected
                )

                CustomDatePicker(
                    modifier = Modifier.weight(0.35f, false),
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it },
                    minDate = selectedTrip?.startDate ?: Date(),
                    maxDate = selectedTrip?.endDate ?: Date(),
                    enabled = selectedTrip != null,
                )
            }

            Button(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(bottom = 32.dp)
                    .align(Alignment.End),
                onClick = {
                    nonSelected = if (selectedTrip != null) {
                        onActivityAdd(selectedTrip!!, selectedDate)
                        false
                    } else {
                        Utils.showToast(context = context, message = R.string.select_trip)
                        true
                    }
                }
            ) {
                Text(text = stringResource(R.string.add_activity))
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddActivityBottomSheet(
    onDismissRequest: () -> Unit,
    tripDetailViewModel: TripDetailViewModel,
    onActivityAdd: (RoomActivity) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.add_activity),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            var selectedActivity by remember { mutableStateOf<RoomActivity?>(null) }
            var searchBarExpanded by remember { mutableStateOf(false) }
            val searchQuery by tripDetailViewModel.searchQuery.collectAsState()
            val activities by tripDetailViewModel.foundActivities.observeAsState(Resource.Initial())

            DockedSearchBar(
                query = searchQuery,
                onQueryChange = tripDetailViewModel::onQueryChange,
                onSearch = {},
                active = searchBarExpanded,
                onActiveChange = { searchBarExpanded = !searchBarExpanded },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                },
                trailingIcon = {
                    IconButton(onClick = {
                        if (searchQuery.isNotEmpty()) {
                            tripDetailViewModel.onQueryChange("")
                        } else {
                            searchBarExpanded = false
                        }

                    }) {
                        Icon(Icons.Default.Close, contentDescription = "close")
                    }
                },
            ) {
                SearchMenuList(
                    activities = activities,
                    onActivitySelected = {
                        selectedActivity = ModelConverter.convertToRoomActivity(it)
                        tripDetailViewModel.onQueryChange(it.name)
                        searchBarExpanded = false
                    },
                    query = searchQuery
                )

            }

            Button(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(bottom = 32.dp)
                    .align(Alignment.End),
                onClick = {
                    if (selectedActivity != null) {
                        onActivityAdd(selectedActivity!!)
                        tripDetailViewModel.onQueryChange("")
                        selectedActivity = null
                    }
                }
            ) {
                Text(text = stringResource(R.string.add_activity))
            }
        }
    }
}

@Composable
private fun SearchMenuList(
    activities: Resource<List<FirestoreActivity>>,
    onActivitySelected: (FirestoreActivity) -> Unit,
    query: String
) {
    LazyColumn(Modifier.padding(16.dp)) {
        if (activities is Resource.Success) {
            items(activities.data!!) { activity ->
                SmallActivityCard(activity = activity, onClick = {onActivitySelected(activity)})
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        if (activities is Resource.Error) {
            item {
                Text(text = stringResource(R.string.search_error))
            }
        }
        if (activities is Resource.Loading) {
            item {
               CircularProgressIndicator()
            }
        }

        if (activities is Resource.Empty) {
            item {
                Text(text = stringResource(R.string.empty_search, query))
            }
        }
    }

}


