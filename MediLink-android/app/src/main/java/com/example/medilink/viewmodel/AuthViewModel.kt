package com.example.medilinkapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.medilinkapp.data.TokenManager
import com.example.medilinkapp.network.AuthService
import com.example.medilinkapp.network.models.ApiResponse
import com.example.medilinkapp.network.models.LoginRequest
import com.example.medilinkapp.network.models.SignupRequest
import com.example.medilinkapp.network.models.User
import com.example.medilinkapp.network.models.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel for handling authentication state and operations
 *
 * Why: Manages authentication state and business logic
 * - Handles user authentication flow
 * - Manages form state
 * - Provides error handling
 * - Manages token storage
 */
class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val authService = AuthService.create()
    private val tokenManager = TokenManager(application)

    // State for showing signup form
    private val _showSignup = MutableStateFlow(false)
    val showSignup: StateFlow<Boolean> = _showSignup

    // State for selected user role
    private val _selectedRole = MutableStateFlow(UserRole.PATIENT)
    val selectedRole: StateFlow<UserRole> = _selectedRole

    // State for authentication result
    private val _authResult = MutableStateFlow<User?>(null)
    val authResult: StateFlow<User?> = _authResult

    // State for error message
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // State for token presence
    private val _hasToken = MutableStateFlow(false)
    val hasToken: StateFlow<Boolean> = _hasToken

    init {
        // Check for existing token on initialization
        viewModelScope.launch {
            val token = tokenManager.accessToken.first()
            _hasToken.value = token != null
        }
    }

    /**
     * Toggles between login and signup forms
     */
    fun toggleSignup() {
        _showSignup.value = !_showSignup.value
        _error.value = null
    }

    /**
     * Updates the selected user role
     */
    fun updateRole(role: UserRole) {
        _selectedRole.value = role
    }

    /**
     * Handles login attempt
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _error.value = null
                val response = authService.login(
                    LoginRequest(
                        email = email,
                        password = password,
                        role = _selectedRole.value.value
                    )
                )
                if (response.error == null) {
                    _authResult.value = response.data
                    // Save token if present
                    response.data.accessToken?.let { token ->
                        tokenManager.saveToken(token)
                        _hasToken.value = true
                    }
                } else {
                    _error.value = response.error
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Login failed"
            }
        }
    }

    /**
     * Handles signup attempt
     */
    fun signup(email: String, password: String, name: String) {
        viewModelScope.launch {
            try {
                _error.value = null
                val response = authService.signup(
                    SignupRequest(
                        email = email,
                        password = password,
                        name = name
                    )
                )
                if (response.error == null) {
                    _authResult.value = response.data
                } else {
                    _error.value = response.error
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Signup failed"
            }
        }
    }

    /**
     * Handles logout
     */
    fun logout() {
        viewModelScope.launch {
            tokenManager.clearToken()
            _hasToken.value = false
            _authResult.value = null
        }
    }
}