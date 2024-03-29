package com.example.madcapstone.ui.components.utils

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.madcapstone.R
import com.example.madcapstone.utils.Utils
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    modifier: Modifier = Modifier,
    selectedDate: Date,
    onDateSelected: (Date) -> Unit,
    hasError: Boolean = false,
    minDate: Date,
    maxDate: Date,
    enabled: Boolean = true
) {
    val state = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate.time
    )
    var showDialog by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = Utils.formatLocaleDate(selectedDate),
        onValueChange = {},
        readOnly = true,
        enabled = false,
        suffix = {
            Icon(
                Icons.Default.DateRange,
                contentDescription = "Calender",
            )
        },
        modifier = modifier
            .clickable { showDialog = true },
        colors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = determineTextFieldColor(hasError = hasError, enabled = enabled),
            disabledTextColor = determineTextFieldColor(hasError = hasError, enabled = enabled),
            disabledSuffixColor = determineTextFieldColor(
                hasError = hasError,
                enabled = enabled
            ),
        )
    )
    if (showDialog && enabled) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    onDateSelected(Date(state.selectedDateMillis!!))
                    showDialog = false
                }) {
                    Text(stringResource(id = R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(id = R.string.cancel))
                }
            },
        ) {
            DatePicker(state = state, showModeToggle = false, dateValidator = { date ->
                date in minDate.time..maxDate.time
            })
        }

    }
}

@Composable
private fun determineTextFieldColor(hasError: Boolean, enabled: Boolean): Color {
    return if (hasError) {
        MaterialTheme.colorScheme.error
    } else if (!enabled) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
}