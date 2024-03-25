package com.example.madcapstone.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.madcapstone.R
import com.example.madcapstone.data.models.Activity
import com.example.madcapstone.ui.components.utils.RatingBar
import com.example.madcapstone.utils.Utils

@Composable
fun SmallActivityCard(
    activity: Activity,
    onClick: (Activity) -> Unit
) {
    SmallActivityCard(activity, ActivityCardType.NORMAL, onClick = onClick)
}

@Composable
fun ReviewActivityCard(activity: Activity, onClick: (Activity) -> Unit, onReview: (Activity, rating:Int) -> Unit ) {
    SmallActivityCard(activity, ActivityCardType.REVIEW, onClick = onClick, onReview = onReview)
}

@Composable
fun ExploreActivityCard(
    activity: Activity,
    onClick: (Activity) -> Unit,
    onHearted: (Activity) -> Unit
) {
    LargeActivityCard(activity, ActivityCardType.SEARCH, onClick = onClick, onHearted = onHearted)
}

@Composable
fun TripActivityCard(
    activity: Activity,
    onClick: (Activity) -> Unit,
    onDelete: (Activity) -> Unit,
    onEdit: (Activity) -> Unit
) {
    LargeActivityCard(activity, ActivityCardType.TRIP, onClick = onClick, onDelete = onDelete, onEdit = onEdit)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SmallActivityCard(
    activity: Activity,
    type: ActivityCardType = ActivityCardType.NORMAL,
    onClick: (Activity) -> Unit,
    onReview: ((Activity, Int) -> Unit)? = null
) {
    var reviewScore by remember { mutableIntStateOf(0) }
    var priceText = ""
    if (type == ActivityCardType.NORMAL) {
        priceText = getPriceText(activity)
    }
    val smallIconSize = 14.dp
    ElevatedCard(modifier = Modifier
        .width(if (type == ActivityCardType.REVIEW) 200.dp else 250.dp)
        .height(150.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        onClick = {
            onClick(activity)
        }
    ) {
        Row {
            //TODO Async image from activity.imageUrl
            AsyncImage(
                model = activity.imageUrl,
                contentDescription = "Activity image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.weight(0.4f)
            )
            Column(
                Modifier
                    .weight(0.6f)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = activity.name,
                        style = if (type == ActivityCardType.NORMAL) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleSmall,
                    )
                    Text(
                        text = activity.place,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                if (type == ActivityCardType.REVIEW) {
                    Column {
                        Text(
                            text = stringResource(id = R.string.your_score),
                            style = MaterialTheme.typography.titleSmall,
                        )
                        RatingBar(
                            rating = reviewScore,
                            onRatingChange = {
                                reviewScore = it
                                onReview!!(activity, it)
                            },
                            iconSize = smallIconSize,
                        )
                    }
                } else {
                    RatingBar(
                        rating = activity.rating,
                        reviewers = activity.amountOfReviews,
                        iconSize = smallIconSize
                    )
                    Text(
                        priceText,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LargeActivityCard(
    activity: Activity,
    type: ActivityCardType,
    onClick: (Activity) -> Unit,
    onDelete: ((Activity) -> Unit)? = null,
    onEdit: ((Activity) -> Unit)? = null,
    onHearted: ((Activity) -> Unit)? = null,
) {
    val iconButtonSize = 30.dp
    val iconSize = 20.dp
    val topAndEndPadding = 8.dp
    ElevatedCard(
        modifier = Modifier
            .width(350.dp)
            .height(150.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        onClick = {
            onClick(activity)
        }
    ) {
        Row {
            AsyncImage(
                model = activity.imageUrl,
                contentDescription = "Activity image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.weight(0.4f)
            )
            Column(
                Modifier
                    .weight(0.6f)
                    .padding(
                        top = topAndEndPadding,
                        start = 16.dp,
                        end = topAndEndPadding,
                        bottom = 16.dp
                    )
                    .fillMaxSize()
            ) {
                if (type == ActivityCardType.TRIP) {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = { onEdit!!(activity) },
                            modifier = Modifier.size(iconButtonSize)
                        ) {
                            Icon(
                                painterResource(R.drawable.outline_edit_square_24),
                                contentDescription = "Edit",
                                Modifier.size(iconSize)
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        IconButton(
                            onClick = { onDelete!!(activity) },
                            modifier = Modifier.size(iconButtonSize)
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "delete",
                                Modifier.size(iconSize)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = activity.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                            if (type == ActivityCardType.SEARCH) {
                                var heartState by remember { mutableStateOf(false) }
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(
                                    onClick = {
                                        heartState = !heartState
                                        onHearted!!(activity)
                                    },
                                    modifier = Modifier.size(iconButtonSize)
                                ) {
                                    Icon(
                                        if (heartState) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "Favorite",
                                        Modifier.size(iconSize),
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                        Text(
                            text = activity.place,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    if (type == ActivityCardType.SEARCH) {
                        RatingBar(
                            rating = activity.rating,
                            reviewers = activity.amountOfReviews,
                            iconSize = 14.dp
                        )
                    }

                    MonthlyVisitorsDisplay(visitors = activity.monthlyVisitors)

                    Text(text = getPriceText(activity), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

private fun getPriceText(activity: Activity): String {
    return if (activity.isFree) "Free" else "€${Utils.formatLocalePrice(activity.minPrice!!)} - €${
        Utils.formatLocalePrice(
            activity.maxPrice!!
        )
    }"
}

@Composable
private fun MonthlyVisitorsDisplay(visitors: Int) {
    Row {
        Text(
            text = stringResource(R.string.monthly_visitors),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = " ${Utils.formatBigNumber(visitors)}",
            style = MaterialTheme.typography.bodySmall
        )
    }

}

private enum class ActivityCardType {
    TRIP, NORMAL, REVIEW, SEARCH
}