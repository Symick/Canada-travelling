package com.example.madcapstone.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.madcapstone.R
import com.example.madcapstone.data.models.City
import com.example.madcapstone.data.util.Resource
import com.example.madcapstone.ui.components.CanadaTripsBottomBar
import com.example.madcapstone.ui.theme.customTopAppBarColor
import com.example.madcapstone.viewmodels.ActivityViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ExploreScreen(viewModel: ActivityViewModel) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title =
            {
                Icon(painterResource(R.drawable.baseline_search_24), contentDescription = null)
                Text(stringResource(R.string.screen_label_explore))
            },
            colors = customTopAppBarColor()
        )
    }) {
        ScreenContent(modifier = Modifier.padding(it), viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(modifier: Modifier, viewModel: ActivityViewModel) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            stringResource(R.string.find_your_activities),
            style = MaterialTheme.typography.headlineMedium
        )
        Row {
            val searchQuery by viewModel.searchQuery.collectAsState()
            var searchActive by remember { mutableStateOf(false) }
            val cities by viewModel.cities.observeAsState(initial = Resource.Initial())
            SearchBar(
                query = searchQuery,
                onQueryChange = viewModel::onQueryChange,
                onSearch = { searchActive = false },
                active = searchActive,
                onActiveChange = { searchActive = it },
                leadingIcon = {
                    IconButton(onClick = {
                        if (searchQuery.isBlank()) {
                            searchActive = false
                        } else {
                            viewModel.onQueryChange("")
                        }
                    }) {
                        Icon(Icons.Filled.Close, contentDescription = "Close")
                    }
                },
                placeholder = { Text(stringResource(R.string.search_hint)) },
                trailingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            ) {
                SearchMenuList(cities, onClick = {
                    viewModel.onQueryChange(it)
                    searchActive = false
                })
            }
        }
    }
}

@Composable
private fun SearchMenuList(cities: Resource<List<City>>, onClick: (cityName: String) -> Unit) {
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
                        .clip(RoundedCornerShape(15.dp))
                        .clickable {
                            onClick(city.name!!)
                        },
                    tonalElevation = 8.dp

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
                Text("Search for a city")
            }
        }
        if (cities is Resource.Empty) {
            item {
                Text("No results found")
            }
        }
        if (cities is Resource.Error) {
            item {
                Text(cities.message ?: "An unknown error occurred")
            }
        }
    }
}
