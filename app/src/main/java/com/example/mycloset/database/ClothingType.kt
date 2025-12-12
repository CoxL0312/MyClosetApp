package com.example.mycloset.database

enum class ClothingType(val displayName: String) {
    SHIRT("Shirt"),
    TANKTOP("Tanktop"),
    LONGSLEEVE("Longsleeve"),
    SWEATER_HOODIE("Sweater/Hoodie"),
    PANTS("Pants"),
    LEGGINGS("Leggings"),
    SKIRT("Skirt"),
    SHORTS("Shorts");

    override fun toString(): String = displayName
}