package com.example.madcapstone.ui.screens

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import com.example.madcapstone.ui.components.utils.RatingBar
import com.example.madcapstone.ui.theme.customTopAppBarColor
import com.example.madcapstone.utils.Utils
import com.example.madcapstone.viewmodels.ActivityViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ActivityScreen(viewModel: ActivityViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                colors = customTopAppBarColor(),
                navigationIcon = {
                    IconButton(onClick = { }) {
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

                    var hearted by remember { mutableStateOf(false) }
                    IconButton(onClick = {
                        hearted = !hearted
                        /*TODO*/
                    }) {
                        Icon(
                            if (hearted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Filter",
                        )
                    }
                }
            )

        }
    )
    {
        ScreenContent(modifier = Modifier.padding(it), viewModel = viewModel)
    }
}

@Composable
private fun ScreenContent(modifier: Modifier, viewModel: ActivityViewModel) {
    val activity = viewModel.selectedActivity
    val openingHours = viewModel.selectedActivity.openingHours
    Column {
        AsyncImage(
            model = activity.imageUrl,
            contentDescription = activity.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
            Text(text = activity.name, style = MaterialTheme.typography.headlineMedium)
            Row {
                Icon(
                    painterResource(R.drawable.outline_globe_24),
                    contentDescription = "Location",
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = activity.websiteUrl,
                    style = MaterialTheme.typography.bodySmall,
                    textDecoration = TextDecoration.Underline
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            RatingBar(rating = activity.rating, reviewers = activity.amountOfReviews)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = activity.description)
            Spacer(modifier = Modifier.height(16.dp))

            if (openingHours.isNotEmpty()) {
                DisplayOpeningHours(openingHours)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(id = R.string.prices), style = MaterialTheme.typography.titleLarge)
            Text(text = getPriceText(activity))

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DisplayOpeningHours(openingHours: Map<String, OpeningHours>) {
    // TODO make a dropdown menu for each day of the week
    var isExpanded by remember { mutableStateOf(false) }
    val valueText = stringResource(id = R.string.opening_hours)
    val context = LocalContext.current
    ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = { isExpanded = !isExpanded }) {
        OutlinedTextField(
            value = valueText,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            modifier = Modifier.menuAnchor(),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
            )
        ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false}) {
            openingHours.forEach { (day, hours) ->
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween){
                    Text(text = day)
                    if (hours.isClosed) {
                        Text(text = stringResource(id = R.string.closed))
                    } else {
                        Text(
                            text = "${Utils.formatLocaleTime(hours.openingTime, context)} - ${Utils.formatLocaleTime(hours.closingTime, context)}")
                    }
                }
            }

        }
    }
}

private fun getPriceText(activity: FirestoreActivity): String {
    return if (activity.isFree) "Free" else "€${Utils.formatLocalePrice(activity.minPrice!!)} - €${
        Utils.formatLocalePrice(
            activity.maxPrice!!
        )
    }"
}