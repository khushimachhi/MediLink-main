package com.example.medilinkapp.network

import com.example.medilinkapp.network.models.ApiResponse
import com.example.medilinkapp.network.models.LoginRequest
import com.example.medilinkapp.network.models.SignupRequest
import com.example.medilinkapp.network.models.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Service class for handling authentication operations
 *
 * Why: Manages all authentication-related API calls
 * - Provides centralized authentication logic
 * - Handles API communication
 * - Manages request/response serialization
 */
class AuthService {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    private val baseUrl = "https://5a27-27-107-175-218.ngrok-free.app/v1"

    /**
     * Signs up a new user
     * @param request SignupRequest containing user details
     * @return ApiResponse containing user data
     */
    suspend fun signup(request: SignupRequest): ApiResponse<User> {
        return client.post("$baseUrl/auth/signup") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    /**
     * Logs in an existing user
     * @param request LoginRequest containing credentials
     * @return ApiResponse containing user data with access token
     */
    suspend fun login(request: LoginRequest): ApiResponse<User> {
        return client.post("$baseUrl/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    companion object {
        fun create(): AuthService {
            return AuthService()
        }
    }
}