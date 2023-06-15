package com.wallee.samples.apps.shop.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wallee.samples.apps.shop.data.portal.Transaction
import com.wallee.samples.apps.shop.portal.PortalService
import com.wallee.samples.apps.shop.utilities.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class PortalRepository @Inject constructor(private val service: PortalService) {

    private val _showResult = MutableLiveData(false)
    val showResult: LiveData<Boolean>
        get() = _showResult

    fun createTransaction(transaction: String, settings: Settings, launchSdk: (String) -> Unit) {

        val userId = settings.userId
        val spaceId = settings.spaceId
        val accessKey = settings.applicationKey
        val requestPath = "/api/transaction/create?spaceId=$spaceId"

        PortalService.create(accessKey, userId, requestPath).createTransaction(spaceId, transaction)
            .enqueue(object : Callback<Transaction> {
                override fun onResponse(
                    call: Call<Transaction>, response: Response<Transaction>
                ) {
                    if (response.isSuccessful) {
                        Log.d(TAG, "This is the new transaction id: ${response.body()?.id}")
                        response.body()?.id?.let {
                            createTokenForTransaction(
                                launchSdk,
                                settings,
                                it
                            )
                        }
                        _showResult.value = false
                    } else {
                        Log.d(TAG, "Error code: " + response.code().toString())
                        _showResult.value = true
                    }
                }

                override fun onFailure(call: Call<Transaction>, t: Throwable) {
                    Log.d(TAG, "FAIL on create transaction call")
                }
            })
    }


    fun createTokenForTransaction(
        launchSdk: (String) -> Unit, settings: Settings, transactionId: Int
    ) {

        val userId = settings.userId
        val spaceId = settings.spaceId
        val accessKey = settings.applicationKey
        val requestPath =
            "/api/transaction/createTransactionCredentials?spaceId=$spaceId&id=$transactionId"

        PortalService.create(accessKey, userId, requestPath)
            .createTransactionToken(spaceId, transactionId).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        _showResult.value = false
                        launchSdk(response.body().toString())
                    } else {
                        Log.d(TAG, "Unable to get a Wallee transaction token!: " + response.code().toString())
                        _showResult.value = true
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d(TAG, "FAIL on create token call")
                }
            })
    }
}