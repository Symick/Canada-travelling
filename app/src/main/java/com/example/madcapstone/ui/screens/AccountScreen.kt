package com.example.madcapstone.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
        CenterAlignedTopAppBar(
            title = { Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painterResource(id = Screens.AccountScreen.icon!!), contentDescription = "home")
                Text(stringResource(Screens.AccountScreen.stringDisplayId!!)) } },
            colors = customTopAppBarColor()
        )
    }) {
        ScreenContent(Modifier.padding(it), viewModel, navigateTo)
    }
}

@Composable
private fun ScreenContent(modifier: Modifier, viewModel: AuthViewModel, navigateTo: (String) -> Unit){
    val user = Firebase.auth.currentUser
    Column(
        modifier
            .padding(16.dp)
            .fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Box(Modifier.size(75.dp)) {

            }
            Text(text = stringResource(id = R.string.profile), style = MaterialTheme.typography.headlineMedium)
            AsyncImage(
                model = user?.photoUrl,
                contentDescription = "profile picture",
                contentScale = ContentScale.Crop,
                fallback = painterResource(id = R.drawable.baseline_account_circle_24),
                modifier = Modifier
                    .size(75.dp)
                    .clip(CircleShape),
            )
        }
        Text(text = user?.displayName ?: "No name", style = MaterialTheme.typography.titleLarge)
        Row {
            Text(text = "Email: ", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            Text(text = user?.email ?: "No email", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
            navigateTo(Screens.HomeScreen.route)
            viewModel.signOut()
        }) {
            Text(text = stringResource(id = R.string.sign_out))
        }
    }
}