package com.example.madcapstone.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.madcapstone.R
import com.example.madcapstone.data.util.Resource
import com.example.madcapstone.state.rememberSignUpErrorState
import com.example.madcapstone.ui.components.BackNavigationTopBar
import com.example.madcapstone.ui.components.utils.PasswordTextField
import com.example.madcapstone.ui.screens.Screens
import com.example.madcapstone.utils.Utils
import com.example.madcapstone.viewmodels.AuthViewModel

@Composable
fun SignUpScreen(navigateUp: () -> Unit, navigateTo: (String) -> Unit, viewModel: AuthViewModel) {
    Scaffold(topBar = {
        BackNavigationTopBar {
            navigateUp()
        }
    }) {
        Column(Modifier.padding(it)) {
            ScreenContent(
                modifier = Modifier.padding(it),
                navigateUp = navigateUp,
                navigateTo = navigateTo,
                viewModel = viewModel,
            )
        }
    }
}

@Composable
private fun ScreenContent(modifier: Modifier, navigateUp: () -> Unit, navigateTo: (String) -> Unit, viewModel: AuthViewModel) {
    Column(
        modifier
            .padding(horizontal = 32.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            stringResource(R.string.lets_get_started),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        SignUpForm(viewModel, navigateTo)

        Spacer(modifier = Modifier.height(32.dp))

        Row {
            Text(stringResource(R.string.already_have_account))
            Text(
                " ${stringResource(R.string.sign_in)}",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { navigateUp() }
            )
        }

    }
}

@Composable
private fun SignUpForm(viewModel: AuthViewModel, navigateTo: (String) -> Unit){
    Column(Modifier.fillMaxWidth()) {
        //input field states
        var nameState by remember { mutableStateOf("") }
        var emailState by remember { mutableStateOf("") }
        var passwordState by remember { mutableStateOf("") }
        var confirmPasswordState by remember { mutableStateOf("") }

        val context = LocalContext.current

        val authState by viewModel.authState.observeAsState()
        var signUpErrorState by rememberSignUpErrorState()

        if (authState is Resource.Error) {
            Utils.showToast(context, (authState as Resource.Error<Boolean>).message ?: "Error")
            viewModel.resetState()
        }

        if (authState is Resource.Success) {
            navigateTo(Screens.HomeScreen.route)
            viewModel.resetState()
        }

        OutlinedTextField(
            value = nameState,
            onValueChange = { nameState = it },
            label = { Text(stringResource(R.string.name_label)) },
            placeholder = { Text(stringResource(R.string.name_placeholder)) },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = signUpErrorState.nameError
        )

        OutlinedTextField(
            value = emailState,
            onValueChange = { emailState = it },
            label = { Text(stringResource(R.string.email_label)) },
            placeholder = { Text(stringResource(R.string.email_placeholder)) },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = signUpErrorState.emailError
        )

        Spacer(modifier = Modifier.height(8.dp))

        PasswordTextField(
            value = passwordState,
            onValueChange = { passwordState = it },
            Modifier.fillMaxWidth(),
            isError = signUpErrorState.passwordError || signUpErrorState.confirmPasswordError
        )

        Spacer(modifier = Modifier.height(8.dp))

        PasswordTextField(
            value = confirmPasswordState,
            onValueChange = { confirmPasswordState = it },
            label = R.string.confirm_password_label,
            modifier = Modifier.fillMaxWidth(),
            isError = signUpErrorState.confirmPasswordError
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            enabled = authState !is Resource.Loading,
            onClick = {
                var hasError = false
                if (!viewModel.isValidateName(nameState)) {
                    Utils.showToast(context, R.string.empty_name)
                    signUpErrorState = signUpErrorState.copy(nameError = true)
                    hasError = true
                } else {
                    signUpErrorState = signUpErrorState.copy(nameError = false)
                }
                if (!viewModel.isValidateEmail(emailState)) {
                    Utils.showToast(context, R.string.invalid_email)
                    signUpErrorState = signUpErrorState.copy(emailError = true)
                    hasError = true
                } else {
                    signUpErrorState = signUpErrorState.copy(emailError = false)
                }

                if (!viewModel.isValidatePassword(passwordState)) {
                    Utils.showToast(context, R.string.password_length)
                    signUpErrorState = signUpErrorState.copy(passwordError = true)
                    hasError = true
                } else {
                    signUpErrorState = signUpErrorState.copy(passwordError = false)
                }

                if (!viewModel.isPasswordMatch(passwordState, confirmPasswordState)) {
                    Utils.showToast(context, R.string.password_mismatch)
                    signUpErrorState = signUpErrorState.copy(confirmPasswordError = true)
                    hasError = true
                } else {
                    signUpErrorState = signUpErrorState.copy(confirmPasswordError = false)
                }

                if (!hasError) {
                    viewModel.signUp(emailState, passwordState, nameState)
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (authState is Resource.Loading) {
                CircularProgressIndicator()
            } else {
                Text(stringResource(R.string.sign_up), style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}