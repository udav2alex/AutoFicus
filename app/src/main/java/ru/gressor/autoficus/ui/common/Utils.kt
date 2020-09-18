package ru.gressor.autoficus.ui.common

import android.content.Context
import androidx.core.content.ContextCompat
import ru.gressor.autoficus.R
import ru.gressor.autoficus.data.entity.Color
import java.text.SimpleDateFormat
import java.util.*

const val DATE_TIME_FORMAT = "d MMMM yyyy, HH:mm"

const val DEBUG_TAG = "AutoFicusTag :"

fun Date.format(): String = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
    .format(this)

fun Color.getColorRes(context: Context) =
    ContextCompat.getColor(
        context,
        when (this) {
            Color.WHITE -> R.color.color_white
            Color.VIOLET -> R.color.color_violet
            Color.YELLOW -> R.color.color_yellow
            Color.RED -> R.color.color_red
            Color.PINK -> R.color.color_pink
            Color.GREEN -> R.color.color_green
            Color.BLUE -> R.color.color_blue
        }
    )

fun Color.getColorInt(context: Context) : Int =
    ContextCompat.getColor(context, this.getColorRes(context))
