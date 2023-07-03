package com.wallee.samples.apps.shop.data

sealed class UserPreferencesEvent {
    object UserIdCached : UserPreferencesEvent()
    class UserIdFetch(val userId: String) : UserPreferencesEvent()

    object SpaceIdCached : UserPreferencesEvent()
    class SpaceFetch(val spaceId: String) : UserPreferencesEvent()

    object AppKeyCached : UserPreferencesEvent()
    class AppKeyFetch(val appKey: String) : UserPreferencesEvent()
}