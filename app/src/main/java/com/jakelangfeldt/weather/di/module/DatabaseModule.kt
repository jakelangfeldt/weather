package com.jakelangfeldt.weather.di.module

import android.content.Context
import androidx.room.Room
import com.jakelangfeldt.weather.data.datasource.local.WeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context) = Room.databaseBuilder(
        appContext,
        WeatherDatabase::class.java,
        WeatherDatabase.DATABASE_NAME,
    ).build()
}
