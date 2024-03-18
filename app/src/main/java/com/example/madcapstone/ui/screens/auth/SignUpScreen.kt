package com.example.madcapstone.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.madcapstone.R
import com.example.madcapstone.ui.components.BackNavigationTopBar
import com.example.madcapstone.ui.components.utils.PasswordTextField
import com.example.madcapstone.ui.components.utils.TextDivider
import com.example.madcapstone.ui.screens.Screens
import com.example.madcapstone.utils.Utils
import com.example.madcapstone.viewmodels.AuthViewModel

@Composable
fun SignUpScreen(navigateUp: () -> Unit, viewModel: AuthViewModel) {
    Scaffold(topBar = {
        BackNavigationTopBar {
            navigateUp()
        }
    }) {
        Column(Modifier.padding(it)) {
            ScreenContent(modifier = Modifier.padding(it), navigateUp = navigateUp, viewModel = viewModel)
        }
    }
}

@Composable
private fun ScreenContent(modifier: Modifier, navigateUp: () -> Unit, viewModel: AuthViewModel) {
    Column(
        modifier
            .padding(horizontal = 32.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(stringResource(R.string.lets_get_started), style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        SignUpForm(viewModel)

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
private fun SignUpForm(viewModel: AuthViewModel) {
    Column(Modifier.fillMaxWidth()) {
        var nameState by remember { mutableStateOf("") }
        var emailState by remember { mutableStateOf("") }
        var passwordState by remember { mutableStateOf("") }
        var confirmPasswordState by remember { mutableStateOf("") }
        val context = LocalContext.current

        OutlinedTextField(
            value = nameState,
            onValueChange = { nameState = it },
            label = { Text(stringResource(R.string.name_label)) },
            placeholder = { Text(stringResource(R.string.name_placeholder)) },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = emailState,
            onValueChange = { emailState = it },
            label = { Text(stringResource(R.string.email_label)) },
            placeholder = { Text(stringResource(R.string.email_placeholder)) },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        PasswordTextField(
            value = passwordState,
            onValueChange = { passwordState = it },
            Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        PasswordTextField(
            value = confirmPasswordState,
            onValueChange = { confirmPasswordState = it },
            label = R.string.confirm_password_label,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (!viewModel.isValidateEmail(emailState)) {
                    Utils.showToast(context, R.string.invalid_email)
                }
                if (!viewModel.isValidatePassword(passwordState)) {
                    Utils.showToast(context, R.string.password_length)
                }
                if (!viewModel.isPasswordMatch(passwordState, confirmPasswordState)){
                    Utils.showToast(context, R.string.password_mismatch)
                }

                if (!viewModel.isValidateName(nameState)) {
                    Utils.showToast(context, R.string.empty_name)
                }

                viewModel.signUp(emailState, passwordState, nameState)
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.sign_in), style = MaterialTheme.typography.titleMedium)
        }
    }
}