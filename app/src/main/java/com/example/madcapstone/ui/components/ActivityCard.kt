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
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.madcapstone.R
import com.example.madcapstone.data.models.firebaseModels.FirestoreActivity
import com.example.madcapstone.data.models.roomModels.RoomActivity
import com.example.madcapstone.data.util.ModelConverter
import com.example.madcapstone.ui.components.utils.RatingBar
import com.example.madcapstone.utils.Utils

/**
 * Small activity card component.
 *
 * @param activity The activity
 * @param onClick The function to execute when clicked

 */
@Composable
fun SmallActivityCard(
    activity: FirestoreActivity,
    onClick: () -> Unit
) {
    SmallActivityCard(activity, ActivityCardType.NORMAL, onClick = onClick)
}

/**
 * Review activity card component.
 *
 * @param activity The activity
 * @param onClick The function to execute when clicked
 * @param onReview The function to execute when reviewed
 */
@Composable
fun ReviewActivityCard(
    activity: FirestoreActivity,
    onClick: () -> Unit,
    onReview: (rating: Int) -> Unit
) {
    SmallActivityCard(activity, ActivityCardType.REVIEW, onClick = onClick, onReview = onReview)
}

/**
 * Explore activity card component.
 *
 * @param activity The activity
 * @param onClick The function to execute when clicked
 * @param onHearted The function to execute when hearted
 * @param isHearted The hearted state
 */
@Composable
fun ExploreActivityCard(
    activity: FirestoreActivity,
    onClick: () -> Unit,
    onHearted: () -> Unit,
    isHearted: Boolean
) {
    LargeActivityCard(
        activity,
        ActivityCardType.SEARCH,
        onClick = onClick,
        onHearted = onHearted,
        isHearted = isHearted
    )
}

/**
 * Trip activity card component.
 *
 * @param activity The activity
 * @param onClick The function to execute when clicked
 * @param onDelete The function to execute when deleted
 * @param onEdit The function to execute when edited
 */
@Composable
fun TripActivityCard(
    activity: RoomActivity,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    LargeActivityCard(
        ModelConverter.convertToFirestoreActivity(activity),
        ActivityCardType.TRIP,
        onClick = onClick,
        onDelete = onDelete,
        onEdit = onEdit
    )
}

/**
 * Small activity card component. used for small and review cards.
 *
 * @param activity The activity
 * @param type The type of the card (small or review)
 * @param onClick The function to execute when clicked
 * @param onReview The function to execute when reviewed
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SmallActivityCard(
    activity: FirestoreActivity,
    type: ActivityCardType = ActivityCardType.NORMAL,
    onClick: () -> Unit,
    onReview: ((Int) -> Unit)? = null
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
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        onClick = {
            onClick()
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
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = activity.name,
                        style = if (type == ActivityCardType.NORMAL) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleSmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = activity.Location,
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
                                onReview!!(it)
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

/**
 * Large activity card component.
 * Used for trip and search cards.
 *
 * @param activity The activity
 * @param type The type of the card (trip or search)
 * @param onClick The function to execute when clicked
 * @param onDelete The function to execute when deleted
 * @param onEdit The function to execute when edited
 * @param onHearted The function to execute when hearted
 * @param isHearted The hearted state
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LargeActivityCard(
    activity: FirestoreActivity,
    type: ActivityCardType,
    onClick: () -> Unit,
    onDelete: (() -> Unit)? = null,
    onEdit: (() -> Unit)? = null,
    onHearted: (() -> Unit)? = null,
    isHearted: Boolean = false
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
            onClick()
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
                            onClick = { onEdit!!() },
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
                            onClick = { onDelete!!() },
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
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                            Text(
                                text = activity.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                maxLines = if (type == ActivityCardType.TRIP) 1 else 2,
                                modifier = Modifier.weight(0.8f),
                                overflow = TextOverflow.Ellipsis
                            )
                            if (type == ActivityCardType.SEARCH) {
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(
                                    onClick = {
                                        onHearted!!()
                                    },
                                    modifier = Modifier
                                        .size(iconButtonSize)
                                        .weight(0.2f)
                                ) {
                                    Icon(
                                        if (isHearted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "Favorite",
                                        Modifier.size(iconSize),
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                        if (type == ActivityCardType.SEARCH) {
                            Text(
                                text = activity.Location,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    if (type == ActivityCardType.SEARCH) {
                        RatingBar(
                            rating = activity.rating,
                            reviewers = activity.amountOfReviews,
                            iconSize = 14.dp
                        )
                        MonthlyVisitorsDisplay(visitors = activity.monthlyVisitors)
                    } else {
                        Row {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = "address")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = activity.address,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }


                    Text(text = getPriceText(activity), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

/**
 * Helper function to get the price text.
 */
private fun getPriceText(activity: FirestoreActivity): String {
    return if (activity.isFree) "Free" else "€${Utils.formatLocalePrice(activity.minPrice)} - €${
        Utils.formatLocalePrice(
            activity.maxPrice
        )
    }"
}

/**
 * Helper function to display the monthly visitors.
 */
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

/**
 * Class of all the activity card types.
 */
private enum class ActivityCardType {
    TRIP, NORMAL, REVIEW, SEARCH
}