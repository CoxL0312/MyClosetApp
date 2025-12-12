package com.example.mycloset.database

enum class Occasion(val displayName: String) {
    OUTDOOR("Outdoor"),
    ATHLEISURE("Athleisure"),
    WORK("Work"),
    CASUAL("Casual"),
    COMFORT("Comfort"),
    PAJAMAS("Pajamas"),
    GOING_OUT("Going-Out");

    override fun toString(): String = displayName
}