package ru.gressor.autoficus.ui.common

import android.view.View

fun View.dp2Px(value: Float) = (value * resources.displayMetrics.density).toFloat()
fun View.dp2Px(value: Int) = (value * resources.displayMetrics.density).toFloat()