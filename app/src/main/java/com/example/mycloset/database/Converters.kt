package com.example.mycloset.database

import androidx.room.TypeConverter
class Converters {
    @TypeConverter
    fun fromClothingType(value: ClothingType?): String? = value?.name

    @TypeConverter
    fun toClothingType(value: String?): ClothingType? =
        value?.let { ClothingType.valueOf(it) }

    @TypeConverter
    fun fromClothingColor(value: ClothingColor?): String? = value?.name

    @TypeConverter
    fun toClothingColor(value: String?): ClothingColor? =
        value?.let { ClothingColor.valueOf(it) }

    @TypeConverter
    fun fromOccasion(value: Occasion?): String? = value?.name

    @TypeConverter
    fun toOccasion(value: String?): Occasion? =
        value?.let { Occasion.valueOf(it) }
}