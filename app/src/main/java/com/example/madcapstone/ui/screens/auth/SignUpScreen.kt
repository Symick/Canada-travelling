package com.example.madcapstone.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.madcapstone.ui.Components.BackNavigationTopBar

@Composable
fun SignUpScreen(navigateUp: () -> Unit) {
    Scaffold(topBar = {
        BackNavigationTopBar {
            navigateUp()
        }
    }) {
        Column(Modifier.padding(it)) {
            Text(text = "SignUp")
        }
    }
}