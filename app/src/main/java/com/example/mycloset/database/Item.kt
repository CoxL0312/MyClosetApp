package com.example.mycloset.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_table")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "type")
    val type: ClothingType,
    @ColumnInfo(name = "color")
    val color: ClothingColor,
    @ColumnInfo(name = "occasion")
    val occasion: Occasion,
    val imageUri: String? = null
)
