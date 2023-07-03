package com.wallee.samples.apps.shop

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.themeadapter.material.MdcTheme
import com.wallee.samples.apps.shop.compose.WalleeShopApp
import com.wallee.samples.apps.shop.data.Settings
import com.wallee.samples.apps.shop.data.UserPreferencesEvent
import com.wallee.samples.apps.shop.utilities.TAG
import com.wallee.samples.apps.shop.viewmodels.*
import com.wallee.walleepaymentsdk.WalleePaymentSdk
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShopActivity : AppCompatActivity() {

    val resultViewModel: ResultViewModel by viewModels()
    val configViewModel: ConfigViewModel by viewModels()
    val itemAndMetadataListViewModel: ItemAndMetadataListViewModel by viewModels()
    val portalViewModel: PortalViewModel by viewModels()
    val itemListViewModel: ItemListViewModel by viewModels()
    val itemDetailViewModel: ItemDetailViewModel by viewModels()
    private val preferencesViewModel: PreferencesViewModel by viewModels()
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
        lifecycleScope.launch {
            preferencesViewModel.cacheUserId(userId = settings.userId).collect { event ->
                userIdPreferences = settings.userId
                updateViewOnEvent(event)
            }
        }

        lifecycleScope.launch {
            preferencesViewModel.cacheSpaceId(spaceId = settings.spaceId).collect { event ->
                spaceIdPreferences = settings.spaceId
                updateViewOnEvent(event)
            }
        }
        lifecycleScope.launch {
            preferencesViewModel.cacheApplicationKey(appKey = settings.applicationKey)
                .collect { event ->
                    appKeyIdPreferences = settings.applicationKey
                    updateViewOnEvent(event)
                }
        }
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

    private fun updateViewOnEvent(event: UserPreferencesEvent) {
        when (event) {
            is UserPreferencesEvent.UserIdCached -> {
                Log.d(TAG, "User id stored in cache")
            }
            is UserPreferencesEvent.UserIdFetch -> {
                userIdPreferences = event.userId
                Log.d(TAG, "User id fetch from cache: $userIdPreferences")
            }
            is UserPreferencesEvent.SpaceIdCached -> {
                Log.d(TAG, "Space id stored in cache")
            }
            is UserPreferencesEvent.SpaceFetch -> {
                spaceIdPreferences = event.spaceId
                Log.d(TAG, "Space id fetch from cache: $spaceIdPreferences")
            }

            is UserPreferencesEvent.AppKeyCached -> {
                Log.d(TAG, "App key stored in cache")
            }
            is UserPreferencesEvent.AppKeyFetch -> {
                appKeyIdPreferences = event.appKey
                Log.d(TAG, "App Key fetch from cache: $appKeyIdPreferences")
            }
        }
    }


    private fun fetchFromPreferences() {
        lifecycleScope.launch {
            preferencesViewModel.getCachedUserId().collect { event ->
                updateViewOnEvent(event)
            }
        }
        lifecycleScope.launch {
            preferencesViewModel.getCachedSpaceId().collect { event ->
                updateViewOnEvent(event)
            }
        }
        lifecycleScope.launch {
            preferencesViewModel.getCachedApplicationKey().collect { event ->
                updateViewOnEvent(event)
            }
        }
        configViewModel.setUserId(userIdPreferences)
        configViewModel.setSpaceId(spaceIdPreferences)
        configViewModel.setAuthenticationKey(appKeyIdPreferences)
    }
}