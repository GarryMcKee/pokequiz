package com.gmk0232.whosthatpokemon.feature.quiz.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class KeyValueStorage(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    suspend fun getInt(key: String): Int {
        val preferenceKey = intPreferencesKey(key)

        return context.dataStore.data
            .map { preferences ->
                preferences[preferenceKey] ?: 0
            }.first()

    }

    suspend fun setInt(key: String, value: Int) {
        context.dataStore.edit {
            it[intPreferencesKey(key)] = value
        }
    }

}