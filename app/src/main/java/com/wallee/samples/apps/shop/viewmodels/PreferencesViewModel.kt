package com.wallee.samples.apps.shop.viewmodels

import androidx.lifecycle.ViewModel
import com.wallee.samples.apps.shop.data.UserPreferencesEvent
import com.wallee.samples.apps.shop.data.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    fun cacheUserId(userId: String) = flow {
        userPreferencesRepository.setUserId(userId)
        emit(UserPreferencesEvent.UserIdCached)
    }

    fun getCachedUserId() = flow {
        val result = userPreferencesRepository.getUserId()
        emit(UserPreferencesEvent.UserIdFetch(result))
    }

    fun cacheSpaceId(spaceId: String) = flow {
        userPreferencesRepository.setSpaceId(spaceId)
        emit(UserPreferencesEvent.SpaceIdCached)
    }

    fun getCachedSpaceId() = flow {
        val result = userPreferencesRepository.getSpaceId()
        emit(UserPreferencesEvent.SpaceFetch(result))
    }

    fun cacheApplicationKey(appKey: String) = flow {
        userPreferencesRepository.setApplicationKey(appKey)
        emit(UserPreferencesEvent.AppKeyCached)
    }

    fun getCachedApplicationKey() = flow {
        val result = userPreferencesRepository.getApplicationKey()
        emit(UserPreferencesEvent.AppKeyFetch(result))
    }
}
