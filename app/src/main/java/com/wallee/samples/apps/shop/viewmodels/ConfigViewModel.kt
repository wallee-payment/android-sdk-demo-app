package com.wallee.samples.apps.shop.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ConfigState(
    val property: String,
    val isInvalid: Boolean = false,
    val errorMessage: String = ""
)

class ConfigViewModel : ViewModel() {

    private val _userState = MutableStateFlow(ConfigState("", false, ""))
    val userState: StateFlow<ConfigState> = _userState.asStateFlow()

    private val _spaceState = MutableStateFlow(ConfigState("", false, ""))
    val spaceState: StateFlow<ConfigState> = _spaceState.asStateFlow()

    private val _appKeyState =
        MutableStateFlow(ConfigState("", false, ""))
    val appKeyState: StateFlow<ConfigState> = _appKeyState.asStateFlow()


    var isEnabledSavedButton: MutableState<Boolean> = mutableStateOf(false)

    private val _showSnackbar = MutableStateFlow(false)

    val showSnackbar: StateFlow<Boolean>
        get() = _showSnackbar

    fun getUserId(): String {
        return userState.value.property
    }

    fun setUserId(userId: String) {
        _userState.update { it.copy(property = userId) }
        validateUserId()
    }

    fun getSpaceId(): String {
        return spaceState.value.property
    }

    fun setSpaceId(spaceId: String) {
        _spaceState.update { it.copy(property = spaceId) }
        validateSpaceId()
    }

    fun getAuthenticationKey(): String {
        return appKeyState.value.property
    }

    fun setAuthenticationKey(authenticationKey: String) {
        _appKeyState.update { it.copy(property = authenticationKey) }
        validateApplicationKey()
    }

    fun dismissSnackbar() {
        _showSnackbar.value = false
    }

    fun saveSettings() {
        _showSnackbar.value = true
    }

    fun isConfigSettingsEmpty(): Boolean {
        return getUserId().isEmpty() && getSpaceId().isEmpty() && getAuthenticationKey().isEmpty()
    }

    private fun validateUserId() {
        if (getUserId().length < 5) {
            _userState.update {
                it.copy(
                    isInvalid = true,
                    errorMessage = "User id should be more than 5 chars"
                )
            }
        } else {
            _userState.update {
                it.copy(
                    isInvalid = false,
                    errorMessage = ""
                )
            }
        }
        shouldEnableSaveConfigButton()
    }

    private fun validateSpaceId() {
        if (getSpaceId().length < 5) {
            _spaceState.update {
                it.copy(
                    isInvalid = true,
                    errorMessage = "Space id should be more than 5 chars"
                )
            }

        } else {
            _spaceState.update {
                it.copy(
                    isInvalid = false,
                    errorMessage = ""
                )
            }
        }
        shouldEnableSaveConfigButton()
    }

    private fun validateApplicationKey() {
        if (getAuthenticationKey().length < 20) {
            _appKeyState.update {
                it.copy(
                    isInvalid = true,
                    errorMessage = "Application Key should be more than 20 chars"
                )
            }
        } else {
            _appKeyState.update {
                it.copy(
                    isInvalid = false,
                    errorMessage = ""
                )
            }
        }
        shouldEnableSaveConfigButton()
    }

    private fun shouldEnableSaveConfigButton() {
        isEnabledSavedButton.value =
            userState.value.errorMessage.isEmpty() && spaceState.value.errorMessage.isEmpty() && appKeyState.value.errorMessage.isEmpty()
    }


}