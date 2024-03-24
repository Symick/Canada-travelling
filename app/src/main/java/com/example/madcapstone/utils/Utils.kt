package com.example.madcapstone.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHostState
import java.text.DecimalFormat

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

        fun formatLocalePrice(price: Double? = 0.0): String {
            return DecimalFormat("#,###.##").format(price)
        }
    }
}