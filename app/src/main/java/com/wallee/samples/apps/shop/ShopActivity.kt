package com.wallee.samples.apps.shop

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStateAtLeast
import com.google.accompanist.themeadapter.material.MdcTheme
import com.wallee.samples.apps.shop.compose.WalleeShopApp
import com.wallee.samples.apps.shop.data.Settings
import com.wallee.samples.apps.shop.utilities.TAG
import com.wallee.samples.apps.shop.viewmodels.ConfigViewModel
import com.wallee.samples.apps.shop.viewmodels.MainEvent
import com.wallee.samples.apps.shop.viewmodels.PreferencesViewModel
import com.wallee.samples.apps.shop.viewmodels.ResultViewModel
import com.wallee.walleepaymentsdk.WalleePaymentSdk
import com.wallee.walleepaymentsdk.enums.ThemeEnum
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShopActivity : AppCompatActivity() {

    val resultViewModel: ResultViewModel by viewModels()
    private val preferencesViewModel: PreferencesViewModel by viewModels()
    val configViewModel: ConfigViewModel by viewModels()
    private var userIdPreferences = ""
    private var spaceIdPreferences = ""
    private var appKeyIdPreferences = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWallee()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        fetchFromPreferences()

        setContent {
            MdcTheme {
                WalleeShopApp(
                    onAttached = { toolbar ->
                        setSupportActionBar(toolbar)
                    }
                )
            }
        }
    }

    fun launchSdkFromActivity(token: String) {
        WalleePaymentSdk.instance?.launch(
            token,
            this
        ) ?: run {
            Log.e(
                TAG,
                "SDK is not initialized. Did you forget to run init on Application?"
            )
        }
    }


    fun savePreferences(settings: Settings) {
        cacheUserId(settings.userId)
        cacheSpaceId(settings.spaceId)
        cacheAppKey(settings.applicationKey)
    }

    private fun initWallee() {
        (application as? MainApplication)?.paymentResultState?.observe(this) { paymentResult ->
            val paymentResultAsString = paymentResult.code.toString()

            Log.d(
                TAG,
                "Payment Result Received: $paymentResultAsString"
            )

            resultViewModel.setPaymentResult(paymentResultAsString)
        }
    }

    private fun updateViewOnEvent(event: MainEvent) {
        when (event) {
            is MainEvent.UserIdCached -> {
                Log.d(TAG, "User id stored in cache")
            }
            is MainEvent.UserIdFetch -> {
                userIdPreferences = event.userId
                Log.d(TAG, "User id fetch from cache: $userIdPreferences")
            }
            is MainEvent.SpaceIdCached -> {
                Log.d(TAG, "Space id stored in cache")
            }
            is MainEvent.SpaceFetch -> {
                spaceIdPreferences = event.spaceId
                Log.d(TAG, "Space id fetch from cache: $spaceIdPreferences")
            }

            is MainEvent.AppKeyCached -> {
                Log.d(TAG, "App key stored in cache")
            }
            is MainEvent.AppKeyFetch -> {
                appKeyIdPreferences = event.appKey
                Log.d(TAG, "App Key fetch from cache: $appKeyIdPreferences")
            }
        }
    }

    private fun cacheUserId(userId: String) {
        lifecycleScope.launch {
            preferencesViewModel.cacheUserId(userId = userId).collect { event ->
                userIdPreferences = userId
                updateViewOnEvent(event)
            }
        }
    }

    private fun fetchUserId() {
        lifecycleScope.launch {
            preferencesViewModel.getCachedUserId().collect { event ->
                updateViewOnEvent(event)
            }
        }
    }

    private fun cacheSpaceId(spaceId: String) {
        lifecycleScope.launch {
            preferencesViewModel.cacheSpaceId(spaceId = spaceId).collect { event ->
                spaceIdPreferences = spaceId
                updateViewOnEvent(event)
            }
        }
    }

    private fun fetchSpaceId() {
        lifecycleScope.launch {
            preferencesViewModel.getCachedSpaceId().collect { event ->
                updateViewOnEvent(event)
            }
        }
    }

    private fun cacheAppKey(appKey: String) {
        lifecycleScope.launch {
            preferencesViewModel.cacheApplicationKey(appKey = appKey).collect { event ->
                appKeyIdPreferences = appKey
                updateViewOnEvent(event)
            }
        }
    }

    private fun fetchAppKey() {
        lifecycleScope.launch {
            preferencesViewModel.getCachedApplicationKey().collect { event ->
                updateViewOnEvent(event)
            }
        }
    }

    private fun fetchFromPreferences() {
        fetchUserId()
        fetchSpaceId()
        fetchAppKey()
        configViewModel.userId.value = userIdPreferences
        configViewModel.spaceId.value = spaceIdPreferences
        configViewModel.applicationKey.value = appKeyIdPreferences
        configViewModel.loadSettings()
    }
}