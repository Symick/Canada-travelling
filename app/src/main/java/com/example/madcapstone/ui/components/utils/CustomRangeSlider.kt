package com.example.madcapstone.ui.components.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.madcapstone.R
import com.example.madcapstone.utils.Utils

/**
 * Custom range slider component.
 *
 * @param range The range
 * @param onValueChange The function to change the value
 * @param onValueChangeFinished The function to change the value finished
 * @author Julian Kruithof
 */
@Composable
fun CustomRangeSlider(
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    onValueChangeFinished: () -> Unit

) {
    RangeSlider(
        value = range,
        onValueChange = onValueChange,
        valueRange = 0f..500f,
        onValueChangeFinished = onValueChangeFinished,
    )
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = "0")
        Row {
            Text(stringResource(R.string.min_price))
            Text(" ${Utils.formatLocalePrice(range.start)}")
            Text(text = " - ")
            Text(stringResource(R.string.max_price))
            Text(" ${Utils.formatLocalePrice(range.endInclusive)}")
        }
        Text(text = "500")
    }

}