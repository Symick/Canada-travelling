package com.example.madcapstone.ui.components.modals

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.madcapstone.R
import com.example.madcapstone.data.models.roomModels.Trip
import com.example.madcapstone.utils.Utils
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsBottomSheet(onDismissRequest: () -> Unit, createTrip: (Trip) -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 32.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.create_trip), style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(8.dp))

            var tripName by remember { mutableStateOf("") }
            var tripError by remember { mutableStateOf(false) }
            OutlinedTextField(
                value = tripName,
                onValueChange = { tripName = it },
                label = { Text(stringResource(R.string.trip_name_label)) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        painterResource(R.drawable.baseline_card_travel_24),
                        contentDescription = "suitcase"
                    )
                },
                singleLine = true,
                isError = tripError,
            )

            var showDatePickerDialog by remember { mutableStateOf(false) }
            var startDate by remember { mutableStateOf<Date?>(null) }
            var endDate by remember { mutableStateOf<Date?>(null) }
            var dateError by remember { mutableStateOf(false) }
            val context = LocalContext.current

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = "${
                    Utils.formatLocaleDate(
                        startDate,
                        context.getString(R.string.start_date)
                    )
                } - ${Utils.formatLocaleDate(endDate, context.getString(R.string.end_date))}",
                onValueChange = {},
                readOnly = true,
                enabled = false,
                trailingIcon = {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = "Calender",
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePickerDialog = true },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = if (dateError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTextColor = if (dateError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = if (dateError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Divider()

            Spacer(modifier = Modifier.height(16.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(onClick = {
                    if (tripName.isEmpty()) {
                        tripError = true
                        Utils.showToast(context, R.string.trip_name_error)
                        return@Button
                    } else {
                        tripError = false
                    }
                    if (startDate == null || endDate == null) {
                        dateError = true
                        Utils.showToast(context, R.string.date_error)
                        return@Button
                    } else {
                        dateError = false
                    }
                    createTrip(
                        Trip(
                            title = tripName,
                            startDate = startDate!!,
                            endDate = endDate!!
                        )
                    )
                }) {
                    Text(stringResource(R.string.create_trip))
                }
            }


            val weekInMillis = 604800000
            val dateRangeState = rememberDateRangePickerState(
                initialSelectedStartDateMillis = System.currentTimeMillis(),
                initialSelectedEndDateMillis = System.currentTimeMillis()
                    .plus(weekInMillis),
            )
            if (showDatePickerDialog) {
                Dialog(
                    onDismissRequest = { showDatePickerDialog = false },
                    properties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        DateRangePicker(
                            state = dateRangeState,
                            dateValidator = { date ->
                                date >= System.currentTimeMillis()
                            },
                            showModeToggle = false,
                            title = {
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(onClick = { showDatePickerDialog = false }) {
                                        Icon(Icons.Default.Close, contentDescription = "Close")
                                    }
                                    Text(stringResource(R.string.travel_date_label))
                                    TextButton(onClick = {
                                        if (dateRangeState.selectedStartDateMillis != null) {
                                            startDate =
                                                Date(dateRangeState.selectedStartDateMillis!!)
                                        }
                                        if (dateRangeState.selectedEndDateMillis != null) {
                                            endDate = Date(dateRangeState.selectedEndDateMillis!!)
                                        }
                                        showDatePickerDialog = false
                                    }) {
                                        Text(stringResource(R.string.save))
                                    }
                                }
                            },
                        )

                    }
                }
            }
        }
    }
}

