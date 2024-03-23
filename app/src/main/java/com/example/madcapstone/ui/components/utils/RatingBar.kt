package com.example.madcapstone.ui.components.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.madcapstone.R

@Composable
fun RatingBar(
    rating: Int,
    modifier: Modifier = Modifier,
    dotsAmount: Int = 5,
    reviewers: Int,
) {
    if (rating > dotsAmount) {
        throw IllegalArgumentException("Rating cannot be greater than size")
    }
    Row(modifier = modifier) {
        for (i in 1 until dotsAmount) {
            if (i < rating) {
                Icon(
                    painterResource(R.drawable.baseline_circle_24),
                    contentDescription = "Filled Circle",
                    tint = MaterialTheme.colorScheme.primary,
                )
            } else {
                Icon(
                    painterResource(R.drawable.outline_circle_24),
                    contentDescription = "Outlined circle",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
        Text(
            text = "($reviewers)",
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Light,
            fontSize = 12.sp
        )
    }
}

@Composable
fun RatingBar(
    rating: Int,
    modifier: Modifier = Modifier,
    dotsAmount: Int = 5,
    onRatingChange: (Int) -> Unit
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
                modifier = Modifier.clickable { onRatingChange(i) }
            )
        }
    }
}


