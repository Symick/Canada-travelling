package com.example.madcapstone.ui.components.utils

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.madcapstone.R
import com.example.madcapstone.data.models.roomModels.Trip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsDropDown(modifier: Modifier = Modifier, selectedTrip: Trip?, trips: List<Trip>, isExpanded: Boolean, onExpandedChange: (Boolean) -> Unit, onTripSelected: (Trip) -> Unit) {

    ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = onExpandedChange, modifier = modifier) {
        OutlinedTextField(
            value = selectedTrip?.title ?: stringResource(R.string.select_trip),
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            modifier = Modifier.menuAnchor(),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )
        ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = {onExpandedChange(false)}) {
            trips.forEach { trip ->
                DropdownMenuItem(
                    text = { Text(trip.title) },
                    onClick = {
                        onTripSelected(trip)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}
