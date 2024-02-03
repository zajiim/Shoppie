package com.example.shoppie.utils.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.withTranslation
import com.example.shoppie.R

class CustomPagerIndicator : View {
    var pageCount: Int = 0
    var position: Int = 0
    var positionOffset: Float = 0f
    private var spacing: Float = 0f
    private var selectedLineWidth = 0f
    private var defaultLineWidth = 0f
    private var selectionColor = Color.BLACK
    private var defaultColor = Color.BLACK

    private val paint = Paint()

    constructor(context: Context) : super(context) {
        initialise(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialise(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initialise(context)
    }

    private fun initialise(context: Context) {
        val dp = context.resources.displayMetrics.density
        paint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 6f * dp
            color = Color.GRAY
            strokeCap = Paint.Cap.ROUND
        }
        selectedLineWidth = 10f * dp
        defaultLineWidth = 1.5f * dp
        spacing = 14f * dp
        selectionColor = ResourcesCompat.getColor(context.resources, R.color.primary_violet, null)
        defaultColor = Color.parseColor("#CED0D0")
    }

    override fun onDraw(canvas: Canvas) {
        if (isInEditMode) {
            pageCount = 5
        }
        val pointsWidth = spacing * (pageCount - 1)
        canvas.withTranslation(width.toFloat() / 2f - pointsWidth / 2f, paint.strokeWidth / 2f) {
            for (pageIndex in 0 until pageCount) {
                when (pageIndex) {
                    position -> {
                        val computedColor = Color.argb(
                            255,
                            (Color.red(selectionColor) * (1f - positionOffset) + Color.red(
                                defaultColor
                            ) * positionOffset).toInt(),
                            (Color.green(selectionColor) * (1f - positionOffset) + Color.green(
                                defaultColor
                            ) * positionOffset).toInt(),
                            (Color.blue(selectionColor) * (1f - positionOffset) + Color.blue(
                                defaultColor
                            ) * positionOffset).toInt(),
                        )
                        paint.color = computedColor
                        val lineWidth =
                            selectedLineWidth * (1f - positionOffset) + defaultLineWidth * positionOffset
                        val x = pageIndex * spacing
                        val startX = x - lineWidth / 2f
                        val endX = x + lineWidth / 2f
                        drawLine(startX, 0f, endX, 0f, paint)
                    }

                    position + 1 -> {
                        val computedColor = Color.argb(
                            255,
                            (Color.red(selectionColor) * (positionOffset) + Color.red(defaultColor) * (1f - positionOffset)).toInt(),
                            (Color.green(selectionColor) * (positionOffset) + Color.green(
                                defaultColor
                            ) * (1f - positionOffset)).toInt(),
                            (Color.blue(selectionColor) * (positionOffset) + Color.blue(defaultColor) * (1f - positionOffset)).toInt(),
                        )
                        paint.color = computedColor
                        val lineWidth =
                            selectedLineWidth * (positionOffset) + defaultLineWidth * (1f - positionOffset)
                        val x = pageIndex * spacing
                        val startX = x - lineWidth / 2f
                        val endX = x + lineWidth / 2f
                        drawLine(startX, 0f, endX, 0f, paint)
                    }

                    else -> {
                        val x = pageIndex * spacing
                        val startX = x - defaultLineWidth / 2f
                        val endX = x + defaultLineWidth / 2f
                        paint.color = defaultColor
                        drawLine(startX, 0f, endX, 0f, paint)
                    }
                }
            }
        }
    }
}