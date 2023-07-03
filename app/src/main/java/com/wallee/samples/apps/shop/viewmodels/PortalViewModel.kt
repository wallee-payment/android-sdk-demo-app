package com.wallee.samples.apps.shop.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wallee.samples.apps.shop.data.PortalRepository
import com.wallee.samples.apps.shop.data.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PortalViewModel @Inject constructor(private val repository: PortalRepository) : ViewModel() {

    private val _showResult = MutableStateFlow(false)
    val showResult: StateFlow<Boolean>
        get() = _showResult

    fun dismissResult() {
        _showResult.value = false
    }

    fun createToken(transaction: String, settings: Settings, launchSdk: (String) -> Unit) {
        viewModelScope.launch {
            val transactionResult = repository.createTransaction(transaction, settings)

            if (transactionResult.isSuccessful) {
                transactionResult.body()?.id?.let {
                    val token = repository.createToken(
                        settings,
                        it,
                    )

                    if (token.isSuccessful && token.body() != null) {
                        launchSdk(token.body().toString())
                    } else {
                        _showResult.value = true
                    }
                }
            } else {
                _showResult.value = true
            }
        }

    }

}