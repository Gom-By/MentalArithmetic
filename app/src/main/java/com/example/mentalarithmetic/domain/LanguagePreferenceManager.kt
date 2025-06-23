package com.example.mentalarithmetic.domain

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object LanguagePreferenceManager {
    private const val DATASTORE_NAME = "settings"
    private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

    private val LANGUAGE_KEY = stringPreferencesKey("language")

    suspend fun saveLanguage(context: Context, lang: String) {
        context.dataStore.edit { prefs ->
            prefs[LANGUAGE_KEY] = lang
        }
    }

    fun getLanguageFlow(context: Context): Flow<String> {
        return context.dataStore.data
            .map { prefs -> prefs[LANGUAGE_KEY] ?: "en" }
    }
}
