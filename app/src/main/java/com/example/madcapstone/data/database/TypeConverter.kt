package com.example.madcapstone.data.database

import androidx.room.TypeConverter
import com.example.madcapstone.data.models.firebaseModels.OpeningHours
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

/**
 * TypeConverter for the Room database
 * This class is used to convert complex objects to a format that can be stored in the database
 * and vice versa.
 *
 *
 * @author Julian Kruithof

 */
class TypeConverter {
    private val gson = Gson() // Create a Gson object to convert objects to json and parse json to objects
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

    /**
     * Convert a json string into a map of OpeningHours objects
     */
    @TypeConverter
    fun stringToOpeningHours(value: String): Map<String, OpeningHours> {
        val mapType = object : TypeToken<Map<String, OpeningHours>>() {}.type
        return gson.fromJson(value, mapType)
    }

    /**
     * Convert a map of OpeningHours objects into a json string
     */
    @TypeConverter
    fun openingHoursToString(openingHours: Map<String, OpeningHours>): String {
        return gson.toJson(openingHours)
    }

}