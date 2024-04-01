package com.example.madcapstone.ui.components.modals

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.madcapstone.R

/**
 * Simple dialog with a title, message and a confirm button used for deleting items.
 *
 * @param onDismissRequest The function to dismiss the dialog
 * @param title The title of the dialog
 * @param message The message of the dialog
 * @param onConfirm The function to confirm the dialog
 * @author Julian Kruithof
 */
@Composable
fun SimpleDeleteDialog(onDismissRequest: () -> Unit, title: String, message: String, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(title)
        },
        text = {
            Text(message)
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(id = R.string.delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(id = R.string.cancel))
            }
        },
    )
}