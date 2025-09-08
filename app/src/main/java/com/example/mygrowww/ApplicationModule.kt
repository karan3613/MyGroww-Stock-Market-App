package com.example.mygrowww

import android.content.Context
import androidx.room.Room
import com.example.mygrowww.api.API
import com.example.mygrowww.api.ApiRepository
import com.example.mygrowww.constants.APIConstants
import com.example.mygrowww.database.StockDatabase
import com.example.mygrowww.database.StockDatabaseRepository
import com.example.mygrowww.database.WatchListDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


//APPLICATION MODULE FOR HILT , RETROFIT API AND ROOM DATABASE
@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule{

    @Provides
    @Singleton
    fun provideApi(): API = Retrofit.Builder()
        .baseUrl(APIConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(API::class.java)

    @Provides
    @Singleton
    fun provideApiRepository(api: API): ApiRepository{
        return ApiRepository(api)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): StockDatabase {
        return Room.databaseBuilder(
            appContext,
            StockDatabase::class.java,
            "stock_app.db"
        ).build()
    }

    @Provides
    fun provideWatchlistDao(db: StockDatabase): WatchListDao {
        return db.watchlistDao()
    }

    @Provides
    @Singleton
    fun provideRepository(dao: WatchListDao): StockDatabaseRepository{
        return StockDatabaseRepository(dao)
    }
}