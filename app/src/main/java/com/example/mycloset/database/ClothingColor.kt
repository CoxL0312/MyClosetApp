package com.example.mycloset.database

enum class ClothingColor(val displayName: String) {
    BLACK("Black"),
    WHITE("White"),
    RED("Red"),
    ORANGE("Orange"),
    YELLOW("Yellow"),
    GREEN("Green"),
    BLUE("Blue"),
    PURPLE("Purple"),
    PINK("Pink"),
    BROWN("Brown");

    override fun toString(): String = displayName
}