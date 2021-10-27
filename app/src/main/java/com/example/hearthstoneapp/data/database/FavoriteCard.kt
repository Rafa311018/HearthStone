package com.example.hearthstoneapp.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_cards_table")
data class FavoriteCard(
    @PrimaryKey(autoGenerate = false)
    var cardId: String = "",

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "type")
    var type: String? = "",

    @ColumnInfo(name = "rarity")
    var rarity: String? = "",

    @ColumnInfo(name = "card_set")
    var cardSet: String? = "",

    @ColumnInfo(name = "img")
    var img: String? = "",

    @ColumnInfo(name = "effect")
    var effect: String? = "",

    @ColumnInfo(name = "player_class")
    var playerClass: String? = ""
)