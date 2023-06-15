package com.wallee.samples.apps.shop.di

import android.content.Context
import com.wallee.samples.apps.shop.data.AppDatabase
import com.wallee.samples.apps.shop.data.ItemDao
import com.wallee.samples.apps.shop.data.ItemMetadataDao
import com.wallee.samples.apps.shop.portal.PortalService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideItemDao(appDatabase: AppDatabase): ItemDao {
        return appDatabase.itemDao()
    }

    @Provides
    fun provideItemMetadataDao(appDatabase: AppDatabase): ItemMetadataDao {
        return appDatabase.itemMetadataDao()
    }

    @Singleton
    @Provides
    fun providePortalService(): PortalService {
        return PortalService.create("", "", "")
    }
}
