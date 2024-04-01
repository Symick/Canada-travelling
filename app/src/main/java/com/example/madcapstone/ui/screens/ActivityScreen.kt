package com.example.madcapstone.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.madcapstone.R
import com.example.madcapstone.data.models.firebaseModels.FirestoreActivity
import com.example.madcapstone.data.models.firebaseModels.OpeningHours
import com.example.madcapstone.data.util.ModelConverter
import com.example.madcapstone.ui.components.modals.AddActivityBottomSheet
import com.example.madcapstone.ui.components.modals.TripsBottomSheet
import com.example.madcapstone.ui.components.utils.RatingBar
import com.example.madcapstone.ui.theme.customTopAppBarColor
import com.example.madcapstone.utils.Utils
import com.example.madcapstone.viewmodels.ActivityViewModel
import com.example.madcapstone.viewmodels.TripDetailViewModel
import com.example.madcapstone.viewmodels.TripViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ActivityScreen(
    tripViewModel: TripViewModel,
    viewModel: ActivityViewModel,
    navigateUp: () -> Unit
) {
    var showAddActivityModel by remember { mutableStateOf(false) }
    var showCreateTripModel by remember { mutableStateOf(false) }
    val activity = viewModel.selectedActivity
    val tripCount by tripViewModel.tripsCount.observeAsState()
    val trips by tripViewModel.getTripsWithoutActivity(activity.id).observeAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                colors = customTopAppBarColor(),
                navigationIcon = {
                    IconButton(onClick = { navigateUp() }) {
                        Icon(
                            Icons.Filled.KeyboardArrowLeft,
                            contentDescription = "Back",
                            modifier = Modifier.size(32.dp),
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painterResource(id = R.drawable.size_32__weight_regular),
                            contentDescription = "Review",
                            modifier = Modifier.size(24.dp),
                        )
                    }

                    val hearted = trips.isNullOrEmpty()
                    IconButton(onClick = {
                        if (hearted) return@IconButton
                        if (tripCount == 0) {
                            showCreateTripModel = true
                        } else {
                            showAddActivityModel = true
                        }
                    }) {
                        Icon(
                            if (hearted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Filter",
                            tint = Color.Red
                        )
                    }
                }
            )

        }
    )
    {
        ScreenContent(modifier = Modifier.padding(it), activity)
        if (showAddActivityModel) {

            AddActivityBottomSheet(onDismissRequest = { showAddActivityModel = false },
                trips!!,
                onActivityAdd = { trip, date ->
                    tripViewModel.addActivityToTrip(
                        trip,
                        ModelConverter.convertToRoomActivity(activity),
                        date
                    )
                    showAddActivityModel = false
                }
            )
        }
        if (showCreateTripModel) {
            TripsBottomSheet(
                onDismissRequest = { showCreateTripModel = false },
                createTrip = { trip ->
                    tripViewModel.insertTrip(trip)
                    showCreateTripModel = false
                    showAddActivityModel = true
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen(
    tripDetailViewModel: TripDetailViewModel,
    navigateUp: () -> Unit
) {
    val activity = tripDetailViewModel.selectedTripActivity!!
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    /*TODO*/
                },
                colors = customTopAppBarColor(),
                navigationIcon = {
                    IconButton(onClick = { navigateUp() }) {
                        Icon(
                            Icons.Filled.KeyboardArrowLeft,
                            contentDescription = "Back",
                            modifier = Modifier.size(32.dp),
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        openGoogleMaps(activity.address, context)
                    }) {
                        Icon(
                            painterResource(R.drawable.baseline_assistant_navigation_24),
                            contentDescription = "Navigate",
                        )
                    }
                }

            )
        }
    )
    {
        ScreenContent(
            modifier = Modifier.padding(it),
            activity = ModelConverter.convertToFirestoreActivity(activity),
            fromTrip = true
        )

    }
}

@Composable
private fun ScreenContent(
    modifier: Modifier,
    activity: FirestoreActivity,
    fromTrip: Boolean = false
) {

    val openingHours = activity.openingHours
    val context = LocalContext.current
    Column(modifier = modifier) {
        AsyncImage(
            model = activity.imageUrl,
            contentDescription = activity.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = activity.name, style = MaterialTheme.typography.headlineMedium)

            if (!fromTrip) {
                Spacer(modifier = Modifier.height(16.dp))
                RatingBar(rating = activity.rating, reviewers = activity.amountOfReviews)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = activity.description)
            Spacer(modifier = Modifier.height(16.dp))

            if (openingHours.isNotEmpty()) {
                DisplayOpeningHours(openingHours)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.prices),
                style = MaterialTheme.typography.titleLarge
            )
            Text(text = getPriceText(activity))

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.general_information),
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = stringResource(id = R.string.website),
                style = MaterialTheme.typography.labelMedium
            )
            Row(Modifier.clickable {
                val intent = CustomTabsIntent.Builder().build()
                intent.launchUrl(context, Uri.parse(activity.websiteUrl))
            }) {
                Icon(
                    painterResource(R.drawable.outline_globe_24),
                    contentDescription = "website",
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = activity.websiteUrl,
                    style = MaterialTheme.typography.bodySmall,
                    textDecoration = TextDecoration.Underline
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(id = R.string.address),
                style = MaterialTheme.typography.labelMedium
            )
            Row(
                Modifier.clickable
                {
                    openGoogleMaps(activity.address, context)
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = "navigate")
                Text(
                    activity.address,
                    textDecoration = TextDecoration.Underline,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DisplayOpeningHours(openingHours: Map<String, OpeningHours>) {
    // TODO make a dropdown menu for each day of the week
    var isExpanded by remember { mutableStateOf(false) }
    val valueText = stringResource(id = R.string.opening_hours)
    ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = { isExpanded = !isExpanded }) {
        OutlinedTextField(
            value = valueText,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            modifier = Modifier.menuAnchor(),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )
        ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
            DisplayOpeningDay(day = DayOfWeek.MONDAY, hours = openingHours[DayOfWeek.MONDAY.day]!!)
            DisplayOpeningDay(
                day = DayOfWeek.TUESDAY,
                hours = openingHours[DayOfWeek.TUESDAY.day]!!
            )
            DisplayOpeningDay(
                day = DayOfWeek.WEDNESDAY,
                hours = openingHours[DayOfWeek.WEDNESDAY.day]!!
            )
            DisplayOpeningDay(
                day = DayOfWeek.THURSDAY,
                hours = openingHours[DayOfWeek.THURSDAY.day]!!
            )
            DisplayOpeningDay(day = DayOfWeek.FRIDAY, hours = openingHours[DayOfWeek.FRIDAY.day]!!)
            DisplayOpeningDay(
                day = DayOfWeek.SATURDAY,
                hours = openingHours[DayOfWeek.SATURDAY.day]!!
            )
            DisplayOpeningDay(day = DayOfWeek.SUNDAY, hours = openingHours[DayOfWeek.SUNDAY.day]!!)
        }
    }
}

private fun getPriceText(activity: FirestoreActivity): String {
    return if (activity.isFree) "Free" else "€${Utils.formatLocalePrice(activity.minPrice)} - €${
        Utils.formatLocalePrice(
            activity.maxPrice
        )
    }"
}

@Composable
private fun DisplayOpeningDay(day: DayOfWeek, hours: OpeningHours) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = day.day)
        if (hours.isClosed) {
            Text(text = stringResource(id = R.string.closed))
        } else {
            Text(
                text = "${
                    Utils.formatLocaleTime(
                        hours.openingTime,
                        context
                    )
                } - ${Utils.formatLocaleTime(hours.closingTime, context)}"
            )
        }
    }
}

private fun openGoogleMaps(address: String, context: Context) {
    val gmmIntentUri = Uri.parse("google.navigation:q=$address")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    context.startActivity(mapIntent)
}

private enum class DayOfWeek(val day: String) {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday")
}