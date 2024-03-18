package com.example.madcapstone.ui.screens

import androidx.annotation.StringRes
import com.example.madcapstone.R

sealed class Screens(
    val route: String,
    val icon: Int? = null,
    @StringRes val stringDisplayId: Int? = null,
) {
    data object HomeScreen : Screens("Home", R.drawable.baseline_home_24, R.string.screen_label_home)
    data object ExploreScreen : Screens("Explore", R.drawable.baseline_search_24, R.string.screen_label_explore)
    data object TripsListScreen : Screens("TripsList", R.drawable.baseline_card_travel_24, R.string.screen_label_trips)
    data object TripsDetailScreen : Screens("TripsDetail")
    data object AccountScreen : Screens("Account", R.drawable.baseline_person_24, R.string.screen_label_account)
    data object AddActivityScreen : Screens("AddActivity", R.drawable.outline_add_box_24, R.string.screen_label_new_activities)
    data object ActivityDetailScreen : Screens("ActivityDetail")
    data object ReviewScreen : Screens("Review", R.drawable.size_32__weight_regular, R.string.screen_label_review)
    data object SignInScreen : Screens("SignIn", )
    data object SignUpScreen : Screens("SignUp",)
}