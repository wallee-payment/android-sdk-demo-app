package com.wallee.samples.apps.shop.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

interface UserPreferencesRepository {

    suspend fun setUserId(userId: String)
    suspend fun getUserId(): String

    suspend fun setSpaceId(spaceId: String)
    suspend fun getSpaceId(): String

    suspend fun setApplicationKey(appKey: String)
    suspend fun getApplicationKey(): String
}

class UserPreferencesRepositoryImpl @Inject constructor(
    private val userDataStorePreferences: DataStore<Preferences>
) : UserPreferencesRepository {

    override suspend fun setUserId(userId: String) {
        Result.runCatching {
            userDataStorePreferences.edit { preferences ->
                preferences[KEY_USER_ID] = userId
            }
        }
    }

    override suspend fun getUserId(): String {
        return runBlocking {
            userDataStorePreferences.data.map { it[KEY_USER_ID] ?: "" }.first()
        }
    }

    override suspend fun setSpaceId(spaceId: String) {
        Result.runCatching {
            userDataStorePreferences.edit { preferences ->
                preferences[KEY_SPACE_ID] = spaceId
            }
        }
    }

    override suspend fun getSpaceId(): String {
        return runBlocking {
            userDataStorePreferences.data.map { it[KEY_SPACE_ID] ?: "" }.first()
        }
    }

    override suspend fun setApplicationKey(appKey: String) {
        Result.runCatching {
            userDataStorePreferences.edit { preferences ->
                preferences[KEY_APP_ID] = appKey
            }
        }
    }

    override suspend fun getApplicationKey(): String {
        return runBlocking {
            userDataStorePreferences.data.map { it[KEY_APP_ID] ?: "" }.first()
        }
    }

    private companion object {

        val KEY_USER_ID = stringPreferencesKey(
            name = "user_id"
        )
        val KEY_SPACE_ID = stringPreferencesKey(
            name = "space_id"
        )

        val KEY_APP_ID = stringPreferencesKey(
            name = "app_key_id"
        )
    }
}