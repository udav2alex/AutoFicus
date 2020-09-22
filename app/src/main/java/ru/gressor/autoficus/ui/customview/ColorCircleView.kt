package ru.gressor.autoficus.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.annotation.Dimension.DP
import androidx.annotation.Dimension.PX
import androidx.core.content.ContextCompat
import ru.gressor.autoficus.R
import ru.gressor.autoficus.ui.common.dp2Px

class ColorCircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        @Dimension(unit = DP) private const val defRadiusDp = 16
        @Dimension(unit = DP) private const val defStrokeWidthDp = 1
    }

    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE }

    private var center: Pair<Float, Float> = 0f to 0f

    @Dimension(unit = PX) var radius: Float = dp2Px(defRadiusDp)
    @Dimension(unit = PX) var strokeWidth: Float = dp2Px(defStrokeWidthDp)
        set(value) {
            field = value
            strokePaint.strokeWidth = value
        }

    @ColorRes var fillColorRes: Int = R.color.color_white
        set(value) {
            field = value
            fillPaint.color = ContextCompat.getColor(context, value)
        }
    @ColorRes var strokeColorRes: Int = R.color.colorStroke
        set(value) {
            field = value
            strokePaint.color = ContextCompat.getColor(context, value)
        }

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.ColorCircleView)

        val defRadiusPx = dp2Px(defRadiusDp)
        radius = array.getDimension(R.styleable.ColorCircleView_circleRadius, defRadiusPx)
        val defStrokeWidthPx = dp2Px(defStrokeWidthDp)
        strokeWidth = array.getDimension(R.styleable.ColorCircleView_strokeWidth, defStrokeWidthPx)

        fillColorRes = array.getResourceId(R.styleable.ColorCircleView_fillColor, R.color.color_white)
        strokeColorRes = array.getResourceId(R.styleable.ColorCircleView_strokeColor, R.color.colorStroke)

        array.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = (radius * 2 + paddingTop + paddingBottom).toInt()
        val width = (radius * 2 + paddingStart + paddingEnd).toInt()

        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        center = measuredWidth / 2f to measuredHeight / 2f
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawCircle(center.first, center.second, radius, strokePaint)
        canvas.drawCircle(center.first, center.second, radius - strokeWidth, fillPaint)
    }
}