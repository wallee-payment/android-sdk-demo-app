package com.wallee.samples.apps.shop

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wallee.walleepaymentsdk.WalleePaymentSdk
import com.wallee.walleepaymentsdk.event.OnResultEventListener
import com.wallee.walleepaymentsdk.event.PaymentResult
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    private val _paymentResultState = MutableLiveData<PaymentResult>()
    val paymentResultState: LiveData<PaymentResult> get() = _paymentResultState

    override fun onCreate() {
        super.onCreate()

        WalleePaymentSdk.init(listener = object : OnResultEventListener {
            override fun paymentResult(paymentResult: PaymentResult) {
                _paymentResultState.postValue(paymentResult)
            }
        })
    }

}
