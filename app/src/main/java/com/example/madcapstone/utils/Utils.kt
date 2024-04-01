package com.example.madcapstone.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.text.format.DateFormat
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHostState
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Utility class.
 * With useful functions.
 *
 * @author Julian Kruithof
 */
class Utils {

    companion object {

        /**
         * Show a toast with a predefined message.
         */
        fun showToast(context: Context, @StringRes message: Int) {
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(context, context.getText(message), duration)
            toast.show()
        }

        /**
         * Show a toast with a custom message.
         */
        fun showToast(context: Context, message: String) {
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(context, message, duration)
            toast.show()
        }

        /**
         * Check if the device has an internet connection.
         */
        fun hasInternetConnection(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetwork
            val networkCapabilities = cm.getNetworkCapabilities(activeNetwork)
            return networkCapabilities != null &&
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }

        /**
         * Format a big number.
         * For example: 1000 -> 1,000 or 1.000 depending on locale
         */
        fun formatBigNumber(number: Int): String {
            return DecimalFormat("#,###").format(number)
        }

        /**
         * Format a price.
         * For example: 10.00-> 10,00 or 10.00 depending on locale
         */
        fun formatLocalePrice(price: Double): String {
            return DecimalFormat("#,###.##").format(price)
        }

        /**
         * Format a price.
         * For example: 10.00-> 10,00 or 10.00 depending on locale
         */
        fun formatLocalePrice(price: Float): String {
            return DecimalFormat("#,###.##").format(price)
        }

        /**
         * format a time to the locale time format.
         * For example: 14:00 -> 2:00 PM or 14:00
         *
         * @param time The time to format
         * @param context The context
         *
         * @return The formatted time
         */
        fun formatLocaleTime(time: String, context: Context) : String {
            val inputFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            val formattedTime = inputFormatter.parse(time)
            return if (DateFormat.is24HourFormat(context)) {
                val outputFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
                outputFormatter.format(formattedTime!!)
            } else {
                val outputFormatter = SimpleDateFormat("hh:mm aa", Locale.getDefault())
                outputFormatter.format(formattedTime!!)
            }
        }

        /**
         * Format a date to the locale date format.
         * For example: 2022-01-01 -> 1 Jan or 01 Jan
         *
         * @param date The date to format
         * @param nullDisplay The string to display when the date is null
         *
         * @return The formatted date
         */
        fun formatLocaleDate(date: Date?, nullDisplay: String) : String {
            if (date == null) {
                return nullDisplay
            }
            val outputFormatter = SimpleDateFormat("dd MMM", Locale.getDefault())
            return outputFormatter.format(date)
        }

        /**
         * Format a date to the locale date format.
         * For example: 2022-01-01 -> 1 Jan or 01 Jan
         *
         * @param date The date to format
         *
         * @return The formatted date
         */
        fun formatLocaleDate(date: Date) : String {
            return formatLocaleDate(date, "")
        }
    }
}