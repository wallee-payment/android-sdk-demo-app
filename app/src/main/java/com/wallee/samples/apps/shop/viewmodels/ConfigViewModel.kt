package com.wallee.samples.apps.shop.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wallee.samples.apps.shop.data.Settings
import com.wallee.samples.apps.shop.utilities.TAG

class ConfigViewModel : ViewModel() {

    var settings: Settings = Settings("", "", "")

    var userId: MutableState<String> = mutableStateOf(settings.userId)
    var isUserIdValid: MutableState<Boolean> = mutableStateOf(false)
    var userIdErrMsg: MutableState<String> = mutableStateOf("")

    var spaceId: MutableState<String> = mutableStateOf(settings.spaceId)
    var isSpaceIdValid: MutableState<Boolean> = mutableStateOf(false)
    var spaceIdErrMsg: MutableState<String> = mutableStateOf("")

    var applicationKey: MutableState<String> = mutableStateOf(settings.applicationKey)
    var isApplicationKeyValid: MutableState<Boolean> = mutableStateOf(false)
    var applicationKeyErrMsg: MutableState<String> = mutableStateOf("")

    var isEnabledSavedButton: MutableState<Boolean> = mutableStateOf(false)

    fun getUserId(): String {
        return settings.userId
    }

    fun getSpaceId(): String {
        return settings.spaceId
    }

    fun getAuthenticationKey(): String {
        return settings.applicationKey
    }

    fun loadSettings() {
        settings.userId = userId.value
        settings.spaceId = spaceId.value
        settings.applicationKey = applicationKey.value
        Log.d(
            TAG,
            "Settings for Wallee Portal ${userId.value}|${spaceId.value}|${applicationKey.value}"
        )
    }

    fun saveSettings() {
        loadSettings()
        _showSnackbar.value = true
    }

    val showSnackbar: LiveData<Boolean>
        get() = _showSnackbar

    private val _showSnackbar = MutableLiveData(false)

    fun dismissSnackbar() {
        _showSnackbar.value = false
    }

    fun validateUserId() {
        if (userId.value.length < 5) {
            isUserIdValid.value = true
            userIdErrMsg.value = "User id should be more than 5 chars"
        } else {
            isUserIdValid.value = false
            userIdErrMsg.value = ""
        }
        shouldEnableSaveConfigButton()
    }

    fun validateSpaceId() {
        if (spaceId.value.length < 5) {
            isSpaceIdValid.value = true
            spaceIdErrMsg.value = "Space id should be more than 5 chars"
        } else {
            isSpaceIdValid.value = false
            spaceIdErrMsg.value = ""
        }
        shouldEnableSaveConfigButton()
    }

    fun validateApplicationKey() {
        if (applicationKey.value.length < 20) {
            isApplicationKeyValid.value = true
            applicationKeyErrMsg.value = "Application Key should be more than 20 chars"
        } else {
            isApplicationKeyValid.value = false
            applicationKeyErrMsg.value = ""
        }
        shouldEnableSaveConfigButton()
    }

    private fun shouldEnableSaveConfigButton() {
        isEnabledSavedButton.value =
            userIdErrMsg.value.isEmpty() && spaceIdErrMsg.value.isEmpty() && applicationKeyErrMsg.value.isEmpty()
    }

    fun isConfigSettingsEmpty(): Boolean {
        if(settings.spaceId.isEmpty() && settings.userId.isEmpty() && settings.applicationKey.isEmpty()){
            return userId.value.isEmpty() && spaceId.value.isEmpty() && applicationKey.value.isEmpty()
        }
        return false;
    }

}