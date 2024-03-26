package com.example.madcapstone.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.madcapstone.R
import com.example.madcapstone.data.models.firebaseModels.FirestoreActivity
import com.example.madcapstone.ui.components.ExploreActivityCard
import com.example.madcapstone.ui.theme.customTopAppBarColor

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen() {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = { Text(stringResource(R.string.screen_label_home)) },
            colors = customTopAppBarColor(),
        )
    }) {
        Column(
            Modifier
                .padding(it)
                .padding(16.dp)
        ) {
            Text(text = "home Screen")
            val activity = FirestoreActivity(
                name = "Banff sunshine village ski resort",
                Location = "Banff",
                address = "100 Mountain Ave, Banff, AB T1L 1B2",
                minPrice = 20.45,
                maxPrice = 40.95,
                rating = 4,
                amountOfReviews = 910,
                monthlyVisitors = 10000,
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/canada-travelling.appspot.com/o/activities%2Fritik-gautam-eJTt94NgIHs-unsplash.jpg?alt=media&token=7e0903bb-3be7-48df-b675-167786156a0d",
                isFree = false,
            )
            ExploreActivityCard(activity = activity, onClick = {}, onHearted = { /*TODO*/ })
        }
    }
}