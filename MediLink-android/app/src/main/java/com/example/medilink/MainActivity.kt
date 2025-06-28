package com.example.medilinkapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medilinkapp.ui.GreetingScreen
import com.example.medilinkapp.ui.navigation.MainNavigation
import com.example.medilinkapp.viewmodel.GreetingViewModel

/**
 * Main activity of the application
 * Manages the navigation between greeting screen and main content
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Apply Material theme
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // State to control whether to show greeting or main content
                    var showGreeting by remember { mutableStateOf(true) }
                    // Initialize the ViewModel
                    val viewModel: GreetingViewModel = viewModel()

                    // Show either greeting screen or main navigation based on state
                    if (showGreeting) {
                        GreetingScreen(
                            viewModel = viewModel,
                            onGreetingComplete = { showGreeting = false }
                        )
                    } else {
                        MainNavigation()
                    }
                }
            }
        }
    }
}