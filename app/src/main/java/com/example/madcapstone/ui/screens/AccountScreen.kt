package com.example.madcapstone.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.example.madcapstone.R
import com.example.madcapstone.ui.theme.customTopAppBarColor
import com.example.madcapstone.viewmodels.AuthViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AccountScreen(viewModel: AuthViewModel, navigateTo: (String) -> Unit){
    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text(stringResource(R.string.screen_label_account))},
            colors = customTopAppBarColor()
        )
    }) {
        val user = Firebase.auth.currentUser
        Column(Modifier.padding(it)) {
            Text(text = "account Screen")
            Text(text = user?.email ?: "No user")
            Text(text = user?.displayName ?: "No user")
            Button(onClick = {
                navigateTo(Screens.HomeScreen.route)
                viewModel.signOut()
            }) {
                Text(text = "Sign Out")
            }
            AsyncImage(model = user?.photoUrl, contentDescription = "profile picture")

        }
    }
}