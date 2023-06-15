package com.wallee.samples.apps.shop.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ResultViewModel : ViewModel() {

    private val _paymentResult = MutableStateFlow("")
    private val paymentResult: StateFlow<String> = _paymentResult

    fun setPaymentResult(result: String) {
        _paymentResult.value = result
        _showPaymentResult.value = true
    }

    fun getPaymentResult(): String {
        return paymentResult.value
    }

    val showPaymentResult: LiveData<Boolean>
        get() = _showPaymentResult

    private val _showPaymentResult = MutableLiveData(false)

    fun dismissPaymentResultSnackbar() {
        _paymentResult.value = ""
        _showPaymentResult.value = false
    }
}