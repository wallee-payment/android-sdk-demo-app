package com.wallee.samples.apps.shop.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.wallee.samples.apps.shop.data.AppDatabase
import com.wallee.samples.apps.shop.data.Item
import com.wallee.samples.apps.shop.utilities.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val filename = inputData.getString(KEY_FILENAME)
            if (filename != null) {
                applicationContext.assets.open(filename).use { inputStream ->
                    JsonReader(inputStream.reader()).use { jsonReader ->
                        val itemType = object : TypeToken<List<Item>>() {}.type
                        val itemList: List<Item> = Gson().fromJson(jsonReader, itemType)

                        val database = AppDatabase.getInstance(applicationContext)
                        database.itemDao().insertAll(itemList)

                        Result.success()
                    }
                }
            } else {
                Log.e(TAG, "Database error - no valid filename")
                Result.failure()
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Database error: ", ex)
            Result.failure()
        }
    }

    companion object {
        const val KEY_FILENAME = "ITEMS_DATA_FILENAME"
    }
}
