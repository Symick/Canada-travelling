package com.example.madcapstone.data.database

import androidx.room.TypeConverter
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
}