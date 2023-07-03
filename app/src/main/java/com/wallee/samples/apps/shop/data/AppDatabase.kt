package com.wallee.samples.apps.shop.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.wallee.samples.apps.shop.utilities.DATABASE_NAME
import com.wallee.samples.apps.shop.utilities.ITEMS_DATA_FILENAME
import com.wallee.samples.apps.shop.utilities.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ItemMetadata::class, Item::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemMetadataDao(): ItemMetadataDao
    abstract fun itemDao(): ItemDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(
                    object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val filename = ITEMS_DATA_FILENAME
                                    context.assets.open(filename)
                                        .use { inputStream ->
                                            JsonReader(inputStream.reader()).use { jsonReader ->
                                                val itemType =
                                                    object : TypeToken<List<Item>>() {}.type
                                                val itemList: List<Item> =
                                                    Gson().fromJson(jsonReader, itemType)
                                                val itemDao = getInstance(context).itemDao()
                                                itemDao.insertAll(itemList)
                                            }
                                        }
                                } catch (ex: Exception) {
                                    Log.e(TAG, "Database error: ", ex)
                                }
                            }
                        }
                    }
                )
                .build()
        }
    }
}
