package com.example.medilinkapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * TokenManager for handling authentication token storage
 *
 * Why: Manages persistent storage of authentication token
 * - Provides secure token storage
 * - Handles token retrieval and updates
 * - Uses DataStore for modern storage solution
 */
class TokenManager(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    }

    /**
     * Get the stored access token
     * @return Flow of token string, null if not found
     */
    val accessToken: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[ACCESS_TOKEN_KEY]
    }

    /**
     * Save the access token
     * @param token The token to save
     */
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = token
        }
    }

    /**
     * Clear the stored token
     */
    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
        }
    }
}