package ru.gressor.autoficus.ui.customview

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import androidx.annotation.Dimension
import androidx.annotation.Dimension.DP
import ru.gressor.autoficus.data.entity.Color
import ru.gressor.autoficus.ui.common.dp2Px
import ru.gressor.autoficus.ui.common.getColorRes

class ColorPickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val PALETTE_ANIMATION_DURATION = 150L
        private const val HEIGHT = "height"
        private const val SCALE = "scale"
        @Dimension(unit = DP) private const val COLOR_VIEW_PADDING = 8
    }

    var onColorClickListener: (color: Color) -> Unit = { }

    val isOpen: Boolean
        get() = measuredHeight > 0

    private var desiredHeight = 0

    private val animator by lazy {
        ValueAnimator().apply {
            duration = PALETTE_ANIMATION_DURATION
            addUpdateListener(updateListener)
        }
    }

    private val updateListener by lazy {
        ValueAnimator.AnimatorUpdateListener { animator ->
            layoutParams.apply {
                height = animator.getAnimatedValue(HEIGHT) as Int
            }.let {
                layoutParams = it
            }

            val scaleFactor = animator.getAnimatedValue(SCALE) as Float
            for (i in 0 until childCount) {
                getChildAt(i).apply {
                    scaleX = scaleFactor
                    scaleY = scaleFactor
                    alpha = scaleFactor
                }
            }
        }
    }

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
        Color.values().forEach { color ->
            addView(
                ColorCircleView(context).apply {
                    fillColorRes = color.getColorRes()
                    tag = color
                    dp2Px(COLOR_VIEW_PADDING).let {px ->
                        val padding = px.toInt()
                        setPadding(padding, padding, padding, padding)
                    }
                    setOnClickListener { onColorClickListener(it.tag as Color) }
                })
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        layoutParams.apply {
            desiredHeight = height
            height = 0
        }.let {
            layoutParams = it
        }
    }

    fun open() {
        animator.cancel()
        animator.setValues(
            PropertyValuesHolder.ofInt(HEIGHT, measuredHeight, desiredHeight),
            PropertyValuesHolder.ofFloat(SCALE, getChildAt(0).scaleX, 1f))
        animator.start()
    }

    fun close() {
        animator.cancel()
        animator.setValues(
            PropertyValuesHolder.ofInt(HEIGHT, measuredHeight, 0),
            PropertyValuesHolder.ofFloat(SCALE, getChildAt(0).scaleX, 0f))
        animator.start()
    }
}