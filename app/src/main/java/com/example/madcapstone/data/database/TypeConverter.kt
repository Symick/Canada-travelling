package com.example.madcapstone.data.database

import androidx.room.TypeConverter
import com.example.madcapstone.data.models.firebaseModels.OpeningHours
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class TypeConverter {
    /**
     * Convert a sql timestamp into a date object
     */
    @TypeConverter
    fun dateFromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    /**
     * Convert a data object in to a sql timestamp
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    private val gson = Gson()

    @TypeConverter
    fun stringToOpeningHours(value: String): Map<String, OpeningHours> {
        val mapType = object : TypeToken<Map<String, OpeningHours>>() {}.type
        return gson.fromJson(value, mapType)
    }

    @TypeConverter
    fun openingHoursToString(openingHours: Map<String, OpeningHours>): String {
        return gson.toJson(openingHours)
    }

}