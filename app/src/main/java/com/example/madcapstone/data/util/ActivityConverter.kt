package com.example.madcapstone.data.util

import com.example.madcapstone.data.models.firebaseModels.FirestoreActivity
import com.example.madcapstone.data.models.roomModels.RoomActivity

class ActivityConverter {
    companion object{
        fun convertToRoomActivity(firestoreActivity: FirestoreActivity) :RoomActivity {
            return RoomActivity(
                activityId = firestoreActivity.id,
                name = firestoreActivity.name,
                description = firestoreActivity.description,
                imageUrl = firestoreActivity.imageUrl,
                websiteUrl = firestoreActivity.websiteUrl,
                location = firestoreActivity.Location,
                address = firestoreActivity.address,
                minPrice = firestoreActivity.minPrice,
                maxPrice = firestoreActivity.maxPrice,
                isFree = firestoreActivity.isFree,
                openingHours = firestoreActivity.openingHours
            )
        }
    }
}