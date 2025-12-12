package com.example.mycloset.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert
    suspend fun insertItem(item: Item)

    @Delete
    suspend fun deleteItem(item: Item)

    @Update
    suspend fun updateItem(item: Item)

    @Query("SELECT * FROM item_table ORDER BY name ASC")
    fun getAllItems(): Flow<List<Item>>


    @Query("SELECT * FROM item_table WHERE id = :itemId LIMIT 1")
    suspend fun getItemById(itemId: Int): Item?

    @Query("SELECT COUNT(*) FROM item_table")
    suspend fun getItemCount(): Int

    @Query("""
    SELECT * FROM item_table
    WHERE (:nameQuery IS NULL OR name LIKE '%' || :nameQuery || '%')
      AND (:type IS NULL OR type = :type)
      AND (:color IS NULL OR color = :color)
      AND (:occasion IS NULL OR occasion = :occasion)
    ORDER BY name ASC
""")
    fun searchItems(
        nameQuery: String?,
        type: ClothingType?,
        color: ClothingColor?,
        occasion: Occasion?
    ): Flow<List<Item>>
}