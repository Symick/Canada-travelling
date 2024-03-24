package com.example.madcapstone.ui.components.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.madcapstone.R

@Composable
fun RatingBar(
    rating: Int,
    modifier: Modifier = Modifier,
    dotsAmount: Int = 5,
    reviewers: Int,
    iconSize: Dp = 24.dp
) {
    if (rating > dotsAmount) {
        throw IllegalArgumentException("Rating cannot be greater than size")
    }
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        for (i in 1 .. dotsAmount) {
            Icon(
                painterResource(
                    if (i <= rating) {
                        R.drawable.baseline_circle_24
                    } else {
                        R.drawable.outline_circle_24
                    }
                ),
                contentDescription = if (i <= rating) "Filled circle" else "Empty circle",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(iconSize),
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$reviewers",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun RatingBar(
    rating: Int,
    modifier: Modifier = Modifier,
    dotsAmount: Int = 5,
    onRatingChange: (Int) -> Unit,
    iconSize: Dp = 24.dp
) {
    if (rating > dotsAmount) {
        throw IllegalArgumentException("Rating cannot be greater than size")
    }
    Row(modifier = modifier) {
        for (i in 1 .. dotsAmount) {
            Icon(
                painterResource(
                    if (i <= rating) {
                        R.drawable.baseline_circle_24
                    } else {
                        R.drawable.outline_circle_24
                    }
                ),
                contentDescription = "Rating",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(iconSize)
                    .clickable { onRatingChange(i) },
            )
        }
    }
}


