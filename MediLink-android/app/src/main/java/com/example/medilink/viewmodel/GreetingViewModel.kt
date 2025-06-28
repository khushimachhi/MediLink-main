package com.example.medilinkapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medilinkapp.network.GreetingService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing greeting screen state and data
 * Handles API communication and state management for the greeting screen
 */
class GreetingViewModel : ViewModel() {
    // Initialize the network service
    private val greetingService = GreetingService.create()

    // StateFlow to hold the greeting message
    // Private mutable state that can only be modified within this class
    private val _greeting = MutableStateFlow<String>("")
    // Public immutable state that can be observed by the UI
    val greeting: StateFlow<String> = _greeting

    init {
        fetchGreeting()
        // Fetch greeting when ViewModel is created
    }

    /**
     * Fetches greeting message from the API
     * Updates the greeting state with the response or fallback message
     */
    private fun fetchGreeting() {
        viewModelScope.launch {
            try {
                // Attempt to fetch greeting from API
                val response = greetingService.getGreeting()
                _greeting.value = response.message
            } catch (e: Exception) {
                // If API call fails, show fallback message
                _greeting.value = "Welcome to MediLink!"
            }
        }
    }
}