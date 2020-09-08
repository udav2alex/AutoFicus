package ru.gressor.autoficus.ui.common

import ru.gressor.autoficus.R
import ru.gressor.autoficus.data.entity.Color

const val DATE_TIME_FORMAT = "d MMMM yyyy, HH:mm"

fun getColorResource(color: Color) = when(color) {
    Color.WHITE -> R.color.color_white
    Color.VIOLET -> R.color.color_violet
    Color.YELLOW -> R.color.color_yellow
    Color.RED -> R.color.color_red
    Color.PINK -> R.color.color_pink
    Color.GREEN -> R.color.color_green
    Color.BLUE -> R.color.color_blue
}