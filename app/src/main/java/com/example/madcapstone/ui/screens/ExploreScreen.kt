package com.example.madcapstone.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.madcapstone.R
import com.example.madcapstone.data.models.firebaseModels.City
import com.example.madcapstone.data.models.firebaseModels.FirestoreActivity
import com.example.madcapstone.data.util.ModelConverter
import com.example.madcapstone.data.util.Resource
import com.example.madcapstone.state.SearchFilterState
import com.example.madcapstone.state.rememberSearchFilterState
import com.example.madcapstone.ui.components.ExploreActivityCard
import com.example.madcapstone.ui.components.modals.AddActivityBottomSheet
import com.example.madcapstone.ui.components.modals.TripsBottomSheet
import com.example.madcapstone.ui.components.utils.CustomRangeSlider
import com.example.madcapstone.ui.components.utils.RatingBar
import com.example.madcapstone.ui.theme.customTopAppBarColor
import com.example.madcapstone.viewmodels.ActivityViewModel
import com.example.madcapstone.viewmodels.TripViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

/**
 * Explore screen.
 *
 * @param viewModel The activity view model
 * @param navigateTo The function to navigate to a screen
 * @param tripViewModel The trip view model
 * @author Julian Kruithof
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ExploreScreen(
    viewModel: ActivityViewModel,
    navigateTo: (String) -> Unit,
    tripViewModel: TripViewModel
) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title =
            {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painterResource(R.drawable.baseline_search_24), contentDescription = null)
                    Text(stringResource(R.string.screen_label_explore))
                }
            },
            colors = customTopAppBarColor()
        )
    }) {
        ScreenContent(
            modifier = Modifier.padding(it),
            viewModel,
            navigateTo = navigateTo,
            tripViewModel
        )
    }
}

/**
 * The content of the explore screen.
 *
 * @param modifier The modifier
 * @param viewModel The activity view model
 * @param navigateTo The function to navigate to a screen
 * @param tripViewModel The trip view model
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier,
    viewModel: ActivityViewModel,
    navigateTo: (String) -> Unit,
    tripViewModel: TripViewModel
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    var searchActive by remember { mutableStateOf(false) }
    var filterDialogVisible by remember { mutableStateOf(false) }
    var filterState by rememberSearchFilterState()


    if (filterDialogVisible) {
        FilterDialog(
            onDismissRequest = { filterDialogVisible = false },
            onConfirm = {
                filterState = it
                filterDialogVisible = false
                viewModel.getActivities(searchQuery, filterState)
            },
            filterState = filterState
        )
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {

        Text(
            stringResource(R.string.find_your_activities),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        val cities by viewModel.cities.observeAsState(initial = Resource.Initial())

        DockedSearchBar(
            query = searchQuery,
            onQueryChange = viewModel::onQueryChange,
            onSearch = {
                searchActive = false
            },
            active = searchActive,
            onActiveChange = { searchActive = it },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search")
            },
            placeholder = { Text(stringResource(R.string.search_hint)) },
            trailingIcon = {
                if (!searchActive) {
                    IconButton(onClick = { filterDialogVisible = true }) {
                        Icon(
                            painterResource(R.drawable.baseline_filter_alt_24),
                            "Filter",
                            tint = if (filterState.activeFilters()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                } else {
                    IconButton(onClick = {
                        if (searchQuery.isNotEmpty()) {
                            viewModel.onQueryChange("")
                            viewModel.resetActivities()
                        } else {
                            searchActive = false
                        }

                    }) {
                        Icon(Icons.Default.Close, contentDescription = "close")
                    }
                }
            },
        ) {
            SearchMenuList(cities, onClick = {
                viewModel.onQueryChange(it)
                viewModel.getActivities(it, filterState)
                viewModel.savePlaceRecommendation(it)
                searchActive = false
            }, searchQuery)
        }

        Spacer(modifier = Modifier.height(16.dp))

        val activities by viewModel.activities.observeAsState(initial = Resource.Initial())
        if (!searchActive && activities !is Resource.Initial) {
            ActivityList(
                activities,
                onClick = {
                    viewModel.setSelectedActivity(it)
                    viewModel.addActivityRecommendation(it.id)
                    navigateTo(Screens.ActivityDetailScreen.route)
                },
                tripViewModel,
                navigateTo = navigateTo
            )
        }
    }
}

/**
 * The search menu list.
 * @param cities The cities which were found
 * @param onClick The on click function fired when a list item is clicked
 * @param query The query which is searched for
 *
 */
@Composable
private fun SearchMenuList(
    cities: Resource<List<City>>,
    onClick: (cityName: String) -> Unit,
    query: String
) {
    LazyColumn(Modifier.padding(16.dp)) {
        if (cities is Resource.Success) {
            items(cities.data!!) { city ->
                ListItem(
                    headlineContent = {
                        Text(text = city.name!!)
                    },
                    supportingContent = {
                        Text(text = "${city.stateName}, ${city.countryName}")
                    },
                    leadingContent = {
                        Icon(Icons.Default.Place, contentDescription = "City")
                    },
                    modifier = Modifier
                        .clickable {
                            onClick(city.name!!)
                        },
                )
            }
        }
        if (cities is Resource.Loading) {
            item {
                CircularProgressIndicator()
            }
        }
        if (cities is Resource.Initial) {
            item {
                Text(stringResource(R.string.search_for_city))
            }
        }
        if (cities is Resource.Empty) {
            item {
                Text(stringResource(R.string.empty_search, query))
            }
        }
    }
}


/**
 * The filter dialog.
 *
 * @param onDismissRequest The function to dismiss the dialog
 * @param onConfirm The function to confirm the dialog
 * @param filterState The filter state
 */
@Composable
private fun FilterDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (SearchFilterState) -> Unit,
    filterState: SearchFilterState
) {

    val localFilterState = remember { mutableStateOf(filterState) }
    var range by remember {
        mutableStateOf(
            if (localFilterState.value.minPrice != null && localFilterState.value.maxPrice != null)
                localFilterState.value.minPrice!!..localFilterState.value.maxPrice!!
            else
                0f..0f
        )
    }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.filter))
                TextButton(onClick = {
                    localFilterState.value = SearchFilterState()
                    range = 0f..0f
                }) {
                    Text(stringResource(R.string.clear))
                }
            }
        },
        text = {
            FilterForm(localFilterState, range) {
                range = it
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(localFilterState.value)
            }) {
                Text(stringResource(R.string.apply_filter))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

/**
 * The filter form.
 *
 * @param filterState The filter state
 * @param range The range
 * @param onRangeChange The function to change the range
 */
@Composable
private fun FilterForm(
    filterState: MutableState<SearchFilterState>,
    range: ClosedFloatingPointRange<Float>,
    onRangeChange: (ClosedFloatingPointRange<Float>) -> Unit
) {
    Column {
        Text(stringResource(R.string.price_range), style = MaterialTheme.typography.titleMedium)
        CustomRangeSlider(
            range = range,
            onValueChange = onRangeChange,
            onValueChangeFinished = {
                filterState.value =
                    filterState.value.copy(
                        minPrice = range.start,
                        maxPrice = range.endInclusive
                    )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(stringResource(R.string.min_rating), style = MaterialTheme.typography.titleMedium)
        RatingBar(
            rating = filterState.value.minRating ?: 0,
            onRatingChange = {
                filterState.value = filterState.value.copy(minRating = it)
            },
            iconSize = 20.dp
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = filterState.value.minAmountOfVisitors?.toString() ?: "",
            onValueChange = {
                filterState.value =
                    filterState.value.copy(minAmountOfVisitors = it.toIntOrNull())
            },
            label = { Text(stringResource(R.string.minimum_visitors)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

/**
 * The activity list.
 *
 * @param activityList The activity list
 * @param onClick The on click function
 * @param tripViewModel The trip view model
 * @param navigateTo The function to navigate to a screen
 */
@Composable
fun ActivityList(
    activityList: Resource<List<FirestoreActivity>>,
    onClick: (FirestoreActivity) -> Unit,
    tripViewModel: TripViewModel,
    navigateTo: (String) -> Unit
) {
    LazyColumn {
        if (activityList is Resource.Success) {
            items(activityList.data!!) { activity ->
                val tripCount by tripViewModel.tripsCount.observeAsState(0)
                val tripsWithoutActivity by tripViewModel.getTripsWithoutActivity(activity.id)
                    .observeAsState()
                val isHearted = tripCount > 0 && tripsWithoutActivity.isNullOrEmpty()
                var showTripBottomSheet by remember { mutableStateOf(false) }
                var showAddActivityBottomSheet by remember { mutableStateOf(false) }
                val currentUser = Firebase.auth.currentUser
                ExploreActivityCard(
                    activity = activity, onClick = {
                        onClick(activity)
                    },
                    onHearted = {
                        if (currentUser == null) {
                            navigateTo(Screens.SignInScreen.route)
                            return@ExploreActivityCard
                        }
                        if (isHearted) return@ExploreActivityCard
                        if (tripCount > 0) {
                            showAddActivityBottomSheet = true
                        } else {
                            showTripBottomSheet = true
                        }
                    },
                    isHearted = isHearted
                )
                if (showTripBottomSheet) {
                    TripsBottomSheet(
                        onDismissRequest = { showTripBottomSheet = false },
                        createTrip = {
                            tripViewModel.insertTrip(it)
                            showTripBottomSheet = false
                            showAddActivityBottomSheet = true
                        }

                    )
                }
                if (showAddActivityBottomSheet) {
                    AddActivityBottomSheet(
                        onDismissRequest = { showAddActivityBottomSheet = false },
                        trips = tripsWithoutActivity ?: emptyList(),
                        onActivityAdd = { trip, date ->
                            tripViewModel.addActivityToTrip(
                                trip,
                                ModelConverter.convertToRoomActivity(activity),
                                date
                            )
                            showAddActivityBottomSheet = false
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        if (activityList is Resource.Loading) {
            item {
                CircularProgressIndicator()
            }
        }

        if (activityList is Resource.Empty) {
            item {
                Text(stringResource(R.string.no_activities_found))
            }
        }
    }
}
