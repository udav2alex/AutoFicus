package ru.gressor.autoficus.data.entity

data class Note(
    val title : String = "",
    val text : String = "",
    val color : Int = 0xfff06292.toInt(),
    val checked : Boolean = false
)