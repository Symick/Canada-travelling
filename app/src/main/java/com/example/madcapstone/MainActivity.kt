package com.example.madcapstone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.madcapstone.ui.Components.CanadaTripsBottomBar
import com.example.madcapstone.ui.screens.AccountScreen
import com.example.madcapstone.ui.screens.ExploreScreen
import com.example.madcapstone.ui.screens.HomeScreen
import com.example.madcapstone.ui.screens.Screens
import com.example.madcapstone.ui.screens.trips.TripsDetailScreen
import com.example.madcapstone.ui.screens.trips.TripsListScreen
import com.example.madcapstone.ui.theme.MadCapstoneTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MadCapstoneTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val nc = rememberNavController()
                    Scaffold(
                        bottomBar = {
                            CanadaTripsBottomBar(navController = nc)
                        }
                    ) {
                        CanadaTripsNavHost(navHostController = nc, modifier = Modifier.padding(it))
                    }
                }
            }
        }
    }
}

@Composable
private fun CanadaTripsNavHost(navHostController: NavHostController, modifier: Modifier) {
    NavHost(navController = navHostController, startDestination = Screens.HomeScreen.route) {
        composable(Screens.HomeScreen.route) { HomeScreen() }
        composable(Screens.ExploreScreen.route) { ExploreScreen() }
        composable(Screens.TripsListScreen.route) { TripsListScreen(navigateTo = {navHostController.navigate(it)}) }
        composable(Screens.AccountScreen.route) { AccountScreen() }
        composable(Screens.TripsDetailScreen.route) { TripsDetailScreen {
             navHostController.popBackStack()
        }}
    }
}

