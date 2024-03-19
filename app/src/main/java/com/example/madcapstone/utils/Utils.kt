package com.example.madcapstone.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHostState

class Utils {

    companion object {
        fun showToast(context: Context, @StringRes message: Int) {
            val duration = android.widget.Toast.LENGTH_SHORT
            val toast = android.widget.Toast.makeText(context, context.getText(message), duration)
            toast.show()
        }

        fun showToast(context: Context, message: String) {
            val duration = android.widget.Toast.LENGTH_SHORT
            val toast = android.widget.Toast.makeText(context, message, duration)
            toast.show()
        }
    }
}