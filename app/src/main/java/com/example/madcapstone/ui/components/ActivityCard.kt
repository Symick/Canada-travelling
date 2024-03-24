package com.example.madcapstone.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.madcapstone.R
import com.example.madcapstone.data.models.Activity
import com.example.madcapstone.ui.components.utils.RatingBar

@Composable
fun ActivityCard(
    activity: Activity,
    size: ActivityCardSize,
    type: ActivityCardType = ActivityCardType.NORMAL
) {
    when (size) {
        ActivityCardSize.SMALL -> SmallActivityCard(activity)
        ActivityCardSize.LARGE -> LargeActivityCard(activity, type)
    }
}

@Composable
fun ReviewActivityCard(activity: Activity) {
    SmallActivityCard(activity, ActivityCardType.REVIEW)
}

@Composable
private fun SmallActivityCard(activity: Activity, type: ActivityCardType = ActivityCardType.NORMAL) {
    var reviewScore by remember { mutableIntStateOf(0) }
    var priceText = ""
    if (type == ActivityCardType.NORMAL) {
        priceText = if (activity.isFree) "Free" else "€${activity.minPrice} - €${activity.maxPrice}"
    }
    Card(modifier = Modifier.size(200.dp)) {
        Row {
            //TODO Async image from activity.imageUrl
            Image(
                painter = painterResource(id = R.drawable.gondola),
                contentDescription = null,
                modifier = Modifier.weight(0.25f),
                contentScale = ContentScale.Crop
            )
            Column(
                Modifier
                    .weight(0.75f)
                    .padding(16.dp)
            ) {
                Text(text = activity.name)
                Text(text = activity.place)

                if (type == ActivityCardType.REVIEW) {
                    Text(text = stringResource(id = R.string.your_score))
                    RatingBar(rating = reviewScore, onRatingChange = { reviewScore = it })
                } else {
                    RatingBar(rating = activity.rating, reviewers = activity.amountOfReviews)
                    Text(priceText)
                }
            }

        }
    }
}

@Composable
private fun LargeActivityCard(activity: Activity, type: ActivityCardType) {
    // TODO()
}

enum class ActivityCardSize {
    SMALL, LARGE
}

enum class ActivityCardType {
    TRIP, NORMAL, REVIEW
}