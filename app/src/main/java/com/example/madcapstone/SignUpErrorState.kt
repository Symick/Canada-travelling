package com.example.madcapstone

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

data class SignUpErrorState(
    var nameError: Boolean = false,
    var emailError: Boolean = false,
    var passwordError: Boolean = false,
    var confirmPasswordError: Boolean = false,
)

@Composable
fun rememberSignUpErrorState(): MutableState<SignUpErrorState> {
    return remember {
        mutableStateOf(SignUpErrorState())
    }
}
