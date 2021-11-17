package com.example.hearthstoneapp.di.main

import android.content.Context
import androidx.room.Room
import com.example.hearthstoneapp.data.database.FavoriteCard
import com.example.hearthstoneapp.data.database.FavoriteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MainComponent {

    @Singleton
    @Provides
    fun provideHSDB(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        FavoriteDatabase::class.java,
        "favorite_cards_database"
    ).build()

    @Singleton
    @Provides
    fun provideHSDao(db: FavoriteDatabase) = db.favoriteDatabaseDao
}
