package com.example.madcapstone.ui.components.modals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.madcapstone.R
import com.example.madcapstone.data.models.firebaseModels.FirestoreActivity
import com.example.madcapstone.data.models.roomModels.RoomActivity
import com.example.madcapstone.data.models.roomModels.Trip
import com.example.madcapstone.data.util.ActivityConverter
import com.example.madcapstone.ui.components.utils.CustomDatePicker
import com.example.madcapstone.ui.components.utils.TripsDropDown
import com.example.madcapstone.viewmodels.TripViewModel
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddActivityBottomSheet(
    onDismissRequest: () -> Unit,
    tripViewModel: TripViewModel,
    activity: FirestoreActivity
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
            val trips by tripViewModel.trips.observeAsState()
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TripsDropDown(
                    modifier = Modifier.weight(0.5f, false),
                    selectedTrip = selectedTrip,
                    trips = trips ?: emptyList(),
                    isExpanded = expandedDropDown,
                    onExpandedChange = { expandedDropDown = it },
                    onTripSelected = {
                        selectedTrip = it
                        selectedDate = it.startDate
                    }
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
                    .padding(top = 16.dp).padding(bottom = 32.dp).align(Alignment.End),
                onClick = {
                    if (selectedTrip != null) {
                        tripViewModel.addActivityToTrip(
                            selectedTrip!!,
                            ActivityConverter.convertToRoomActivity(activity),
                            selectedDate
                        )
                        onDismissRequest()
                    }
                }) {
                Text(text = stringResource(R.string.add_activity))
            }
        }
    }
}


