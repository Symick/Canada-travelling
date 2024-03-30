package com.example.madcapstone.ui.components.modals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.madcapstone.R
import com.example.madcapstone.data.models.roomModels.RoomActivity
import com.example.madcapstone.data.models.roomModels.Trip
import com.example.madcapstone.data.models.roomModels.TripActivity
import com.example.madcapstone.ui.components.utils.CustomDatePicker
import java.util.Date


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun EditActivityBottomSheet(
    onDismissRequest: () -> Unit,
    trip: Trip,
    initialDate: Date,
    activity: RoomActivity,
    onActivityEdit: (TripActivity) -> Unit,
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
                text = stringResource(R.string.edit_activity),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            var selectedDate by remember { mutableStateOf(initialDate) }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                OutlinedTextField(
                    value = trip.title,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = "Expand trips dropdown"
                        )
                    },
                    modifier = Modifier.weight(0.65f, false)
                )

                CustomDatePicker(
                    modifier = Modifier.weight(0.35f, false),
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it },
                    minDate = trip.startDate,
                    maxDate = trip.endDate,
                )
            }

            Button(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(bottom = 32.dp)
                    .align(Alignment.End),
                onClick = {
                    if (selectedDate == initialDate) {
                        onDismissRequest()
                    } else {
                        onActivityEdit(TripActivity(trip.tripId, activity.activityId, selectedDate))
                    }
                }
            ) {
                Text(text = stringResource(R.string.edit_activity))
            }
        }
    }
}