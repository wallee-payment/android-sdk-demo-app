package com.wallee.samples.apps.shop.viewmodels

import androidx.lifecycle.ViewModel
import com.wallee.samples.apps.shop.di.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

sealed class MainEvent {
    object UserIdCached : MainEvent()
    class UserIdFetch(val userId: String) : MainEvent()

    object SpaceIdCached : MainEvent()
    class SpaceFetch(val spaceId: String) : MainEvent()

    object AppKeyCached : MainEvent()
    class AppKeyFetch(val appKey: String) : MainEvent()
}

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    fun cacheUserId(userId: String) = flow {
        userPreferencesRepository.setUserId(userId)
        emit(MainEvent.UserIdCached)
    }

    fun getCachedUserId() = flow {
        val result = userPreferencesRepository.getUserId()
        emit(MainEvent.UserIdFetch(result))
    }

    fun cacheSpaceId(spaceId: String) = flow {
        userPreferencesRepository.setSpaceId(spaceId)
        emit(MainEvent.SpaceIdCached)
    }

    fun getCachedSpaceId() = flow {
        val result = userPreferencesRepository.getSpaceId()
        emit(MainEvent.SpaceFetch(result))
    }

    fun cacheApplicationKey(appKey: String) = flow {
        userPreferencesRepository.setApplicationKey(appKey)
        emit(MainEvent.AppKeyCached)
    }

    fun getCachedApplicationKey() = flow {
        val result = userPreferencesRepository.getApplicationKey()
        emit(MainEvent.AppKeyFetch(result))
    }
}
