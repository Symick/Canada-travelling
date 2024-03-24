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
import com.example.madcapstone.data.models.Activity
import com.example.madcapstone.ui.components.ReviewActivityCard
import com.example.madcapstone.ui.components.SmallActivityCard
import com.example.madcapstone.ui.components.TripActivityCard
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
            val activity = Activity(
                name = "Banff Gondola Ride",
                place = "Banff",
                address = "100 Mountain Ave, Banff, AB T1L 1B2",
                minPrice = 20.45,
                maxPrice = 40.95,
                rating = 4,
                amountOfReviews = 910,
                monthlyVisitors = 10000,
                imageUrl = "https://www.banffjaspercollection.com/wp-content/uploads/2019/12/Banff-Gondola-Header-Image-1920x1080.jpg",
                isFree = false,
            )
            TripActivityCard(activity = activity, onDelete = { }, onEdit = {})
        }
    }
}