package com.wallee.samples.apps.shop.data

import com.wallee.samples.apps.shop.data.portal.Transaction
import com.wallee.samples.apps.shop.portal.PortalService
import retrofit2.Response
import javax.inject.Inject

class PortalRepository @Inject constructor(private val service: PortalService) {

    suspend fun createTransaction(transaction: String, settings: Settings): Response<Transaction> {

        val spaceId = settings.spaceId
        val requestPath = "/api/transaction/create?spaceId=$spaceId"

        return PortalService.create(settings.applicationKey, settings.userId, requestPath)
            .createTransaction(spaceId, transaction);
    }


    suspend fun createToken(settings: Settings, transactionId: Int): Response<String> {

        val spaceId = settings.spaceId
        val requestPath =
            "/api/transaction/createTransactionCredentials?spaceId=$spaceId&id=$transactionId"

        return PortalService.create(settings.applicationKey, settings.userId, requestPath)
            .createTransactionToken(spaceId, transactionId)
    }
}