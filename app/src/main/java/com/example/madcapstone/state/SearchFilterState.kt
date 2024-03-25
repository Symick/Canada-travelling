package com.example.madcapstone.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.madcapstone.state.SearchFilterState

data class SearchFilterState(
    var minPrice: Double = 0.0,
    var maxPrice: Double = 0.0,
    var minRating: Double = 0.0,
    var minAmountOfVisitors: Int = 0,
)

@Composable
private fun rememberSearchFilterState(): MutableState<SearchFilterState> {
    return remember {
        mutableStateOf(SearchFilterState())
    }
}
