package com.example.medilinkapp.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// Data class to represent the API response structure
@Serializable
data class GreetingResponse(
    val message: String
)

/**
 * Service class to handle API communication using Ktor client
 * Responsible for fetching greeting message from the server
 */
class GreetingService {
    // Initialize Ktor HTTP client with Android engine
    private val client = HttpClient(Android) {
        // Configure JSON serialization
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true  // Ignore any unknown fields in JSON
                isLenient = true         // Be more forgiving with JSON format
            })
        }
    }

    // Base URL for the API
    private val baseUrl = "https://5a27-27-107-175-218.ngrok-free.app"

    /**
     * Fetches greeting message from the server
     * @return GreetingResponse containing the message
     */
    suspend fun getGreeting(): GreetingResponse {
        return client.get("$baseUrl/greeting").body()
    }

    companion object {
        /**
         * Factory method to create a new instance of GreetingService
         */
        fun create(): GreetingService {
            return GreetingService()
        }
    }
}