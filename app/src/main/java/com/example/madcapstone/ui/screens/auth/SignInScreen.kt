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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.madcapstone.R
import com.example.madcapstone.ui.components.BackNavigationTopBar
import com.example.madcapstone.ui.components.utils.PasswordTextField
import com.example.madcapstone.ui.components.utils.TextDivider
import com.example.madcapstone.ui.screens.Screens

@Composable
fun SignInScreen(navigateUp: () -> Unit, navigateTo: (String) -> Unit){
    Scaffold(topBar = {
        BackNavigationTopBar {
            navigateUp()
        }
    }) {
        ScreenContent(modifier = Modifier.padding(it), navigateTo = navigateTo)
    }
}

@Composable
private fun ScreenContent(modifier: Modifier, navigateTo: (String) -> Unit) {
    Column(
        modifier
            .padding(horizontal = 32.dp, vertical = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(stringResource(R.string.welcome_back), style = MaterialTheme.typography.headlineMedium)

        //google login button
        Button(
            onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(15.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painterResource(R.drawable.google),
                    contentDescription = "google logo",
                    Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    stringResource(R.string.gmail_login_button_text),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        TextDivider(stringResource(R.string.login_divider_text), thickness = 2.dp)

        SignInForm()

        Row {
            Text(stringResource(R.string.no_account_yet))
            Text(
                " ${stringResource(R.string.sign_up)}",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { navigateTo(Screens.SignUpScreen.route) }
            )
        }

    }
}

@Composable
private fun SignInForm() {
    Column(Modifier.fillMaxWidth()) {
        var emailState by remember { mutableStateOf("") }
        var passwordState by remember { mutableStateOf("") }
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
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.sign_in), style = MaterialTheme.typography.titleMedium)
        }
    }
}
