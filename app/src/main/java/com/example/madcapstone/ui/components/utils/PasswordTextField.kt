package com.example.madcapstone.ui.components.utils

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.madcapstone.R

/**
 * Password text field component.
 *
 * @param value The value
 * @param onValueChange The function to change the value
 * @param modifier The modifier
 * @param label The label
 * @param isError The error state
 * @author Julian Kruithof
 */
@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    @StringRes label:  Int = R.string.password_label,
    isError: Boolean = false
) {
    var passwordVisible by remember {
        mutableStateOf(false)
    }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {Text(stringResource(label))},
        modifier = modifier,
        singleLine = true,
        isError = isError,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Password),
        leadingIcon = {Icon(Icons.Default.Lock, "Password Lock")},
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible}) {
                if (passwordVisible) {
                    Icon(painterResource(R.drawable.baseline_visibility_off_24), "Hide password")
                } else {
                    Icon(painterResource(R.drawable.baseline_visibility_24), "Show password")
                }
            }
        }
        )
}