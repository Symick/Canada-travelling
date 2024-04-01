package com.example.madcapstone.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * Class for the state of the search filters.
 *
 * @property minPrice The minimum price of the search filter
 * @property maxPrice The maximum price of the search filter
 * @property minRating The minimum rating of the search filter
 * @property minAmountOfVisitors The minimum amount of visitors of the search filter
 *
 * @author Julian Kruithof
 */
data class SearchFilterState(
    var minPrice: Float? = null,
    var maxPrice: Float? = null,
    var minRating: Int? = null,
    var minAmountOfVisitors: Int? = null,
) {
/**
     * Function to check if there are active filters.
     *
     * @return True if there are active filters, false otherwise
     */
    fun activeFilters(): Boolean {
        return minPrice != null || maxPrice != null || minRating != null || minAmountOfVisitors != null
    }
}

/**
 * Function to remember the state of the search filters.
 *
 * @return The state of the search filters
 */
@Composable
fun rememberSearchFilterState(): MutableState<SearchFilterState> {
    return remember {
        mutableStateOf(SearchFilterState())
    }
}
