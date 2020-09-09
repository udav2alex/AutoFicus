package ru.gressor.autoficus.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Note(
    val id: String = "",
    val title: String = "",
    val text: String = "",
    val color: Color = Color.WHITE,
    val lastChanged: Date = Date(),
    val checked: Boolean = false
): Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Note) return false

        if (this.id == other.id) return true
        return false
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

enum class Color {
    WHITE,
    YELLOW,
    GREEN,
    BLUE,
    RED,
    VIOLET,
    PINK
}