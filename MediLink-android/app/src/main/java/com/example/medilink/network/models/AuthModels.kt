package com.example.medilinkapp.network.models

import kotlinx.serialization.Serializable

/**
 * Data models for authentication
 *
 * Why: Represents the structure of authentication requests and responses
 * - Ensures type safety for API communication
 * - Provides clear contract for data exchange
 * - Makes serialization/deserialization easier
 */

@Serializable
data class SignupRequest(
    val email: String,
    val password: String,
    val name: String
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
    val role: String
)

@Serializable
data class ApiResponse<T>(
    val message: String,
    val error: String?,
    val data: T
)

@Serializable
data class User(
    val _id: String,
    val name: String,
    val email: String,
    val verifiedEmail: Boolean,
    val role: String,
    val __v: Int,
    val accessToken: String? = null
)

/**
 * Enum class for user roles
 * Why: Provides type-safe way to handle user roles
 * - Prevents typos in role strings
 * - Makes role handling more maintainable
 */
enum class UserRole(val value: String) {
    PATIENT("patient"),
    DOCTOR("doctor")
}