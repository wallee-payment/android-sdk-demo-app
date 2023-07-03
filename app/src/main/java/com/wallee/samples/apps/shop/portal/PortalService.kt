package com.wallee.samples.apps.shop.portal

import android.util.Log
import com.google.gson.GsonBuilder
import com.wallee.samples.apps.shop.data.portal.Transaction
import com.wallee.samples.apps.shop.utilities.TAG
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.*
import javax.crypto.SecretKey


interface PortalService {

    @retrofit2.http.Headers("Content-Type: application/json")
    @POST("create")
    suspend fun createTransaction(@Query("spaceId") spaceId: String, @Body body: String): retrofit2.Response<Transaction>


    @POST("createTransactionCredentials")
    @retrofit2.http.Headers("Content-Type: application/json")
    suspend fun createTransactionToken(
        @Query("spaceId") spaceId: String,
        @Query("id") id: Int
    ): retrofit2.Response<String>

    companion object {
        private const val BASE_URL = "https://app-wallee.com/api/transaction/"

        fun create(accessKey: String, userId: String, requestPath: String): PortalService {
            val logger = HttpLoggingInterceptor().apply {
                level =
                    HttpLoggingInterceptor.Level.BASIC
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addNetworkInterceptor(
                    Interceptor { chain ->
                        val original = chain.request()
                        val request = original.newBuilder()
                            .addHeader("Authorization", getJwtToken(accessKey, userId, requestPath))
                            .build()
                        Log.d(TAG, request.toString())
                        chain.proceed(request)
                    })
                .build()

            val gson = GsonBuilder()
                .setLenient()
                .create()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(PortalService::class.java)
        }

        private fun getJwtToken(accessKey: String, userId: String, requestPath: String): String {
            val claims = mapOf(
                "requestMethod" to "POST",
                "requestPath" to requestPath
            )

            try {
                val key: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessKey))

                val token = Jwts.builder()
                    .setSubject(userId)
                    .setIssuedAt(Date())
                    .signWith(key, SignatureAlgorithm.HS256)
                    .setHeaderParam("ver", "1")
                    .setHeaderParam("typ", "JWT")
                    .addClaims(claims)
                    .compact()
                return "Bearer $token"
            } catch (ex : Exception) {
                Log.d(TAG,
                    "Unable to create the secret key! Please check the application key provided.")
            }
            return ""
        }
    }
}


