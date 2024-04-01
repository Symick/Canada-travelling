package com.example.madcapstone.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * Class for the state of the sign
 * up error.
 *
 * @property nameError The name error
 *  @property emailError The email error
 *  @property passwordError The password error
 *  @property confirmPasswordError The confirm password error
 *
 *  @author Julian Kruithof
 */
data class SignUpErrorState(
    var nameError: Boolean = false,
    var emailError: Boolean = false,
    var passwordError: Boolean = false,
    var confirmPasswordError: Boolean = false,
)

/**
 * Function to remember the state of the sign up error.
 *
 * @return The state of the sign up error
 */
@Composable
fun rememberSignUpErrorState(): MutableState<SignUpErrorState> {
    return remember {
        mutableStateOf(SignUpErrorState())
    }
}
