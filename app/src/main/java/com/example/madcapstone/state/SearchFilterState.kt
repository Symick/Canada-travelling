package com.example.madcapstone.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

data class SearchFilterState(
    var minPrice: Float? = null,
    var maxPrice: Float? = null,
    var minRating: Int? = null,
    var minAmountOfVisitors: Int? = null,
) {
    fun clearFilters() {
        minPrice = null
        maxPrice = null
        minRating = null
        minAmountOfVisitors = null
    }

    fun activeFilters(): Boolean {
        return minPrice != null || maxPrice != null || minRating != null || minAmountOfVisitors != null
    }
}

@Composable
fun rememberSearchFilterState(): MutableState<SearchFilterState> {
    return remember {
        mutableStateOf(SearchFilterState())
    }
}
