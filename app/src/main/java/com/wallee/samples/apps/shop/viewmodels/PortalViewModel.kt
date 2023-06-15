package com.wallee.samples.apps.shop.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.wallee.samples.apps.shop.data.PortalRepository
import com.wallee.samples.apps.shop.data.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PortalViewModel @Inject constructor(private val repository: PortalRepository) : ViewModel() {

    private val _showResult = MutableLiveData(false)
    val showResult: LiveData<Boolean>
        get() = _showResult

    fun dismissResult() {
        _showResult.value = false

    }

    private val observer = Observer<Boolean> {
        if (repository.showResult.value != null && repository.showResult.value!!) {
            _showResult.value = true
        }
    }

    val result = repository.showResult.observeForever {
        observer
    }

    fun createToken(transaction: String, settings: Settings, launchSdk: (String) -> Unit) {
        repository.createTransaction(
            transaction,
            settings,
            launchSdk
        )
    }

    override fun onCleared() {
        repository.showResult.removeObserver(observer)
        super.onCleared()
    }

}