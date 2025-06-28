package com.example.medilinkapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medilinkapp.ui.screens.AppointmentsScreen
import com.example.medilinkapp.ui.screens.ProfileScreen
import com.example.medilinkapp.ui.screens.ReportsScreen
import com.example.medilinkapp.viewmodel.AuthViewModel

sealed class Screen(val route: String, val title: String) {
    object Reports : Screen("reports", "Reports")
    object Appointments : Screen("appointments", "Appointments")
    object Profile : Screen("profile", "Profile")
}

@Composable
fun MainNavigation() {
    val authViewModel: AuthViewModel = viewModel()
    val authResult by authViewModel.authResult.collectAsState()

    var selectedScreen by remember { mutableStateOf<Screen>(Screen.Reports) }

    val items = listOf(
        Triple(Screen.Reports, Icons.Default.Warning, "Reports"),
        Triple(Screen.Appointments, Icons.Default.DateRange, "Appointments"),
        Triple(Screen.Profile, Icons.Default.Person, "Profile")
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { (screen, icon, label) ->
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) },
                        selected = selectedScreen == screen,
                        onClick = { selectedScreen = screen }
                    )
                }
            }
        }
    ) { paddingValues ->
        when (selectedScreen) {
            Screen.Reports -> ReportsScreen(
                paddingValues = paddingValues,
                userName = authResult?.name ?: "User"
            )
            Screen.Appointments -> AppointmentsScreen(paddingValues)
            Screen.Profile -> ProfileScreen(paddingValues)
        }
    }
}
