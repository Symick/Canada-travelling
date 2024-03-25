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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.madcapstone.ui.components.CanadaTripsBottomBar
import com.example.madcapstone.ui.screens.AccountScreen
import com.example.madcapstone.ui.screens.ExploreScreen
import com.example.madcapstone.ui.screens.HomeScreen
import com.example.madcapstone.ui.screens.Screens
import com.example.madcapstone.ui.screens.auth.SignInScreen
import com.example.madcapstone.ui.screens.auth.SignUpScreen
import com.example.madcapstone.ui.screens.trips.TripsDetailScreen
import com.example.madcapstone.ui.screens.trips.TripsListScreen
import com.example.madcapstone.ui.theme.MadCapstoneTheme
import com.example.madcapstone.viewmodels.ActivityViewModel
import com.example.madcapstone.viewmodels.AuthViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

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
                            BottomBar(nc)
                        }
                    ) {
                        CanadaTripsNavHost(nc = nc, modifier = Modifier.padding(it))
                    }
                }
            }
        }
    }
}

@Composable
private fun CanadaTripsNavHost(nc: NavHostController, modifier: Modifier) {
    val authViewModel: AuthViewModel = viewModel()
    val activityViewModel: ActivityViewModel = viewModel()
    NavHost(navController = nc, startDestination = Screens.HomeScreen.route) {
        composable(Screens.HomeScreen.route) { HomeScreen() }
        composable(Screens.ExploreScreen.route) { ExploreScreen(activityViewModel) }
        composable(Screens.TripsListScreen.route) {
            if (Firebase.auth.currentUser != null) {
                TripsListScreen(navigateTo = { nc.navigate(it) })
            } else {
                nc.navigate(Screens.SignInScreen.route) {
                    popUpTo(Screens.HomeScreen.route)
                }
            }
        }
        composable(Screens.AccountScreen.route) {
            if (Firebase.auth.currentUser != null) {
                AccountScreen(viewModel = authViewModel, navigateTo = { nc.navigate(it) })
            } else {
                nc.navigate(Screens.SignInScreen.route) {
                    popUpTo(Screens.HomeScreen.route)
                }
            }
        }
        composable(Screens.TripsDetailScreen.route) { TripsDetailScreen(navigateUp = { nc.popBackStack() }) }
        composable(Screens.SignInScreen.route) {
            SignInScreen(
                navigateUp = { nc.popBackStack() },
                navigateTo = { nc.navigate(it) },
                viewModel = authViewModel)
        }
        composable(Screens.SignUpScreen.route) {
            SignUpScreen(
                viewModel = authViewModel,
                navigateUp = { nc.popBackStack() },
                navigateTo = { nc.navigate(it) })
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val noBottomBarRoutes = listOf(
        Screens.SignInScreen.route,
        Screens.SignUpScreen.route
    )
    if (currentDestination?.route in noBottomBarRoutes) {
        return
    }
    CanadaTripsBottomBar(navController)

}

