package com.example.madcapstone.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asLiveData
import com.example.madcapstone.R
import com.example.madcapstone.data.util.Resource
import com.example.madcapstone.ui.theme.customTopAppBarColor
import com.example.madcapstone.viewmodels.ActivityViewModel
import kotlinx.coroutines.flow.asStateFlow

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
                onSearch = {},
                active = searchActive,
                onActiveChange = { searchActive = it }) {
                SearchMenuList(cities)
            }
        }
    }
}

@Composable
private fun SearchMenuList(cities: Resource<List<String>>) {
    LazyColumn {
        if (cities is Resource.Success) {
            items(cities.data!!) { city ->
                Text(city)
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
    }
}
