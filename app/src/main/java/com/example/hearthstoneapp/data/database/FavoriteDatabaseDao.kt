package com.example.hearthstoneapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteDatabaseDao {

    @Insert
    fun insert(card: FavoriteCard)

    @Query("DELETE FROM favorite_cards_table WHERE cardId = :key")
    fun delete(key: String)

    @Query("SELECT * FROM favorite_cards_table")
    fun getAllCards(): List<FavoriteCard>?

    @Query("SELECT cardId FROM favorite_cards_table")
    fun getAllIdCards(): List<String>?

    @Query("SELECT * FROM favorite_cards_table WHERE cardId = :key")
    fun getCard(key: String): FavoriteCard?

}