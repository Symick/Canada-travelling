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

class Utils {

    companion object {
        fun showToast(context: Context, @StringRes message: Int) {
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(context, context.getText(message), duration)
            toast.show()
        }

        fun showToast(context: Context, message: String) {
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(context, message, duration)
            toast.show()
        }

        fun hasInternetConnection(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetwork
            val networkCapabilities = cm.getNetworkCapabilities(activeNetwork)
            return networkCapabilities != null &&
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }

        fun formatBigNumber(number: Int): String {
            return DecimalFormat("#,###").format(number)
        }

        fun formatLocalePrice(price: Double): String {
            return DecimalFormat("#,###.##").format(price)
        }

        fun formatLocalePrice(price: Float): String {
            return DecimalFormat("#,###.##").format(price)
        }

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
        fun formatLocaleDate(date: Date?, nullDisplay: String) : String {
            if (date == null) {
                return nullDisplay
            }
            val outputFormatter = SimpleDateFormat("dd MMMM", Locale.getDefault())
            return outputFormatter.format(date)
        }
        fun formatLocaleDate(timeInMillis: Long) : String {
            val calender = Calendar.getInstance()
            calender.timeInMillis = timeInMillis
            val outputFormatter = SimpleDateFormat("dd MMMM", Locale.getDefault())
            return outputFormatter.format(calender.timeInMillis)
        }
    }
}