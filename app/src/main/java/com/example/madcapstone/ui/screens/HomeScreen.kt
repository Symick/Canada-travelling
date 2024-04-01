package com.example.madcapstone.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.madcapstone.R
import com.example.madcapstone.data.models.firebaseModels.FirestoreActivity
import com.example.madcapstone.data.util.Resource
import com.example.madcapstone.ui.components.SmallActivityCard
import com.example.madcapstone.ui.theme.customTopAppBarColor
import com.example.madcapstone.viewmodels.ActivityViewModel
import com.example.madcapstone.viewmodels.HomeViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen(homeViewModel: HomeViewModel, activityViewModel: ActivityViewModel, navigateTo: (String) -> Unit){
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = { Text(stringResource(R.string.screen_label_home)) },
            colors = customTopAppBarColor(),
        )
    }) {
        ScreenContent(Modifier.padding(it), homeViewModel, activityViewModel, navigateTo)
    }
}

@Composable
private fun ScreenContent(modifier: Modifier, homeViewModel: HomeViewModel, activityViewModel: ActivityViewModel, navigateTo: (String) -> Unit) {
    Column(
        modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())) {
        val topActivities by homeViewModel.topActivities.observeAsState(initial = Resource.Initial())


        val placeRecommendations by homeViewModel.placeActivityRecommendations.observeAsState(
            initial = Resource.Initial()
        )
        val recentlyViewed by homeViewModel.activityRecommendations.observeAsState(initial = Resource.Initial())
        val recommendedPlace by homeViewModel.placeRecommendation.collectAsState(initial = "")

        val onClick: (FirestoreActivity) -> Unit = {
            activityViewModel.setSelectedActivity(it)
            navigateTo(Screens.ActivityDetailScreen.route)
        }

        Text(
            text = stringResource(R.string.label_top_activities),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        DisplayTopActivities(topActivities, onClick)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.label_place_recommendations, recommendedPlace),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        DisplayPlaceRecommendations(placeRecommendations, onClick)
        Spacer(modifier = Modifier.height(16.dp))
        DisplayRecentlyViewed(recentlyViewed, onClick)

    }
}

@Composable
private fun DisplayTopActivities(activities: Resource<List<FirestoreActivity>>, onClick: (FirestoreActivity) -> Unit) {
    when (activities) {
        is Resource.Loading -> {
            CircularProgressIndicator()
        }

        is Resource.Success -> {
            LazyRow {
                items(activities.data!!) { activity ->
                    SmallActivityCard(activity = activity, onClick = { onClick(activity) })
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }

        is Resource.Error -> {
            Text(text = stringResource(R.string.error_fetching_data))
        }

        else -> {
            // Show empty
        }
    }
}

@Composable
private fun DisplayPlaceRecommendations(activities: Resource<List<FirestoreActivity>>, onClick: (FirestoreActivity) -> Unit) {
    when (activities) {
        is Resource.Loading -> {
            CircularProgressIndicator()
        }

        is Resource.Success -> {
            LazyRow {
                items(activities.data!!) { activity ->
                    SmallActivityCard(activity = activity, onClick = { onClick(activity)})
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }

        is Resource.Error -> {
            Text(text = stringResource(R.string.error_fetching_data))
        }

        else -> {
            // Show empty
        }
    }
}

@Composable
private fun DisplayRecentlyViewed(activities: Resource<List<FirestoreActivity>>, onClick: (FirestoreActivity) -> Unit){
    when (activities) {
        is Resource.Loading -> {
            Text(
                stringResource(R.string.label_recently_viewed),
                style = MaterialTheme.typography.headlineSmall
            )

            CircularProgressIndicator()
        }

        is Resource.Success -> {
            Text(
                stringResource(R.string.label_recently_viewed),
                style = MaterialTheme.typography.headlineSmall
            )
            LazyRow {
                items(activities.data!!) { activity ->
                    SmallActivityCard(activity = activity, onClick = { onClick(activity) })
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }

        is Resource.Error -> {
            Text(stringResource(R.string.label_recently_viewed), style = MaterialTheme.typography.headlineSmall)

            Text(text = stringResource(R.string.error_fetching_data))
        }

        is Resource.Empty -> {
            Text(text = "empty")
        }
        else ->{
            Text(text = "initial")
        }
    }
}

