package com.example.shoppie.utils.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.text.InputType
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.BaseInputConnection
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputMethodManager
import androidx.core.content.res.ResourcesCompat
import com.example.shoppie.R

class OtpInput : View {
    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val defaultBlockFillPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val defaultBlockStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val selectedBlockFillPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val selectedBlockStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val errorBlockFillPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val errorBlockStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var digitCount = 4
    private var blockCornerRadius = 0f
    private var blockSpacing = 0f
    private var isDebugTextBounds = false
    var isError = false
        set(value) {
            field = value
            invalidate()
        }

    var onInputChanged: ((otp: String) -> Unit)? = null
    var onInputCompleted: ((otp: String) -> Unit)? = null

    private val charBuffer = mutableListOf<Char>()
    private val keyListener = KeyListener()
    private val textBounds = Rect()
    private val debugPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.argb(128, 255, 255, 0)
    }

    constructor(context: Context) : super(context) {
        initialize(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        initialize(context, attrs)
    }

    private fun initialize(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.OtpInput)
        val dp = context.resources.displayMetrics.density
        val fontResId = typedArray.getResourceId(R.styleable.OtpInput_android_fontFamily, 0)
        val font = if (fontResId != 0) ResourcesCompat.getFont(context, fontResId) else null

        textPaint.apply {
            typeface = font ?: Typeface.DEFAULT
            textSize = typedArray.getDimension(
                R.styleable.OtpInput_android_textSize,
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP, 32f,
                    context.resources.displayMetrics
                )
            )
            color = typedArray.getColor(R.styleable.OtpInput_android_textColor, Color.BLACK)
        }
        digitCount = typedArray.getInteger(R.styleable.OtpInput_digitCount, 4)
        defaultBlockFillPaint.apply {
            style = Paint.Style.FILL
            color = typedArray.getColor(
                R.styleable.OtpInput_defaultBlockFillColor,
                Color.argb(255, 255, 255, 255)
            )
        }
        defaultBlockStrokePaint.apply {
            style = Paint.Style.STROKE
            color = typedArray.getColor(
                R.styleable.OtpInput_defaultBlockStrokeColor,
                Color.argb(255, 128, 128, 128)
            )
            strokeWidth = typedArray.getDimension(
                R.styleable.OtpInput_defaultBlockStrokeWidth,
                1f * dp
            )
        }
        selectedBlockFillPaint.apply {
            style = Paint.Style.FILL
            color = typedArray.getColor(
                R.styleable.OtpInput_selectedBlockFillColor,
                Color.argb(255, 220, 220, 255)
            )
        }
        selectedBlockStrokePaint.apply {
            style = Paint.Style.STROKE
            color = typedArray.getColor(
                R.styleable.OtpInput_selectedBlockStrokeColor,
                Color.argb(255, 128, 128, 255)
            )
            strokeWidth = typedArray.getDimension(
                R.styleable.OtpInput_selectedBlockStrokeWidth,
                2f * dp
            )
        }
        errorBlockFillPaint.apply {
            style = Paint.Style.FILL
            color = typedArray.getColor(
                R.styleable.OtpInput_errorBlockFillColor,
                Color.argb(255, 255, 220, 220)
            )
        }
        errorBlockStrokePaint.apply {
            style = Paint.Style.STROKE
            color = typedArray.getColor(
                R.styleable.OtpInput_errorBlockStrokeColor,
                Color.argb(255, 255, 128, 128)
            )
            strokeWidth = typedArray.getDimension(
                R.styleable.OtpInput_errorBlockStrokeWidth,
                2f * dp
            )
        }
        blockCornerRadius = typedArray.getDimension(R.styleable.OtpInput_blockCornerRadius, 8f * dp)
        blockSpacing = typedArray.getDimension(R.styleable.OtpInput_blockSpacing, 8f * dp)
        isFocusable = true
        isFocusableInTouchMode = true
        setOnKeyListener(keyListener)
        typedArray.recycle()
    }

    override fun onCheckIsTextEditor(): Boolean = true

    override fun onCreateInputConnection(outAttrs: EditorInfo?): InputConnection {
        val inputConnection = BaseInputConnection(this, false)
        outAttrs?.actionLabel = null
        outAttrs?.inputType = InputType.TYPE_CLASS_NUMBER
        outAttrs?.imeOptions = EditorInfo.IME_ACTION_DONE
        return inputConnection
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .showSoftInput(this, InputMethodManager.RESULT_UNCHANGED_SHOWN)
                requestFocus()
                return true
            }
            MotionEvent.ACTION_UP -> {
                performClick()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        requestFocus()
    }

    var text: String
        get() = charBuffer.joinToString(separator = "")
        set(value) {
            charBuffer.clear()
            value.forEach { char ->
                if (char in '0'..'9') {
                    if (charBuffer.size < digitCount) {
                        charBuffer.add(char)
                    }
                } else {
                    charBuffer.clear()
                    error("Invalid character $char in $value")
                }
            }
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        (0 until digitCount).forEach {
            val strokePaint = when {
                isError -> errorBlockStrokePaint
                it == getHighlightedIndex() && hasFocus() -> selectedBlockStrokePaint
                else -> defaultBlockStrokePaint
            }
            val fillPaint = when {
                isError -> errorBlockFillPaint
                it == getHighlightedIndex() && hasFocus() -> selectedBlockFillPaint
                else -> defaultBlockFillPaint
            }
            drawBlock(canvas, it, fillPaint, strokePaint, charBuffer.getOrNull(it)?.toString())
        }
    }

    private fun getHighlightedIndex(): Int = when {
        charBuffer.size == 0 -> 0
        charBuffer.size >= digitCount -> digitCount - 1
        else -> charBuffer.size
    }

    private fun drawBlock(canvas: Canvas, index: Int, fill: Paint, stroke: Paint, text: String?) {
        val blockWidth =
            ((width - paddingLeft - paddingRight) - ((digitCount - 1) * blockSpacing)) / digitCount
        val blockHeight = height - paddingBottom - paddingTop
        val blockLeft = paddingLeft + (blockWidth + blockSpacing) * index
        val blockRight = blockLeft + blockWidth
        val blockTop = paddingTop.toFloat()
        val blockBottom = blockTop + blockHeight
        canvas.drawRoundRect(
            blockLeft, blockTop,
            blockRight, blockBottom,
            blockCornerRadius, blockCornerRadius,
            fill
        )
        canvas.drawRoundRect(
            blockLeft, blockTop,
            blockRight, blockBottom,
            blockCornerRadius, blockCornerRadius,
            stroke
        )
        if (text != null) {
            textPaint.getTextBounds(text, 0, text.length, textBounds)
            if (isDebugTextBounds) {
                canvas.drawRect(
                    blockLeft + blockWidth / 2f - textBounds.width() / 2f,
                    blockTop + blockHeight / 2f - textBounds.height() / 2f,
                    blockLeft + blockWidth / 2f + textBounds.width() / 2f,
                    blockTop + blockHeight / 2f + textBounds.height() / 2f,
                    debugPaint
                )
            }
            // Enable anti-aliasing for text paint
            textPaint.isAntiAlias = true
            canvas.drawText(
                text,
                blockLeft + blockWidth / 2f - textBounds.width() / 2f,
                blockTop + blockHeight / 2f + textBounds.height() / 2f,
                textPaint,
            )
        }
    }

//    private fun drawBlock(canvas: Canvas, index: Int, fill: Paint, stroke: Paint, text: String?) {
//        val blockWidth =
//            ((width - paddingLeft - paddingRight) - ((digitCount - 1) * blockSpacing)) / digitCount
//        val blockHeight = height - paddingBottom - paddingTop
//        val blockLeft = paddingLeft + (blockWidth + blockSpacing) * index
//        val blockRight = blockLeft + blockWidth
//        val blockTop = paddingTop.toFloat()
//        val blockBottom = blockTop + blockHeight
//        canvas.drawRoundRect(
//            blockLeft, blockTop,
//            blockRight, blockBottom,
//            blockCornerRadius, blockCornerRadius,
//            fill
//        )
//        canvas.drawRoundRect(
//            blockLeft, blockTop,
//            blockRight, blockBottom,
//            blockCornerRadius, blockCornerRadius,
//            stroke
//        )
//        if (text != null) {
//            textPaint.getTextBounds(text, 0, text.length, textBounds)
//            if (isDebugTextBounds) {
//                canvas.drawRect(
//                    blockLeft + blockWidth / 2f - textBounds.width() / 2f,
//                    blockTop + blockHeight / 2f - textBounds.height() / 2f,
//                    blockLeft + blockWidth / 2f + textBounds.width() / 2f,
//                    blockTop + blockHeight / 2f + textBounds.height() / 2f,
//                    debugPaint
//                )
//            }
//            canvas.drawText(
//                text,
//                blockLeft + blockWidth / 2f - textBounds.width() / 2f,
//                blockTop + blockHeight / 2f + textBounds.height() / 2f,
//                textPaint,
//            )
//        }
//    }

    private inner class KeyListener : OnKeyListener {
        override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if (event?.action == KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    in KeyEvent.KEYCODE_0..KeyEvent.KEYCODE_9 -> {
                        if (isError) {
                            charBuffer.clear()
                            isError = false
                        }
                        if (charBuffer.size < digitCount) {
                            charBuffer.add(event.unicodeChar.toChar())
                            invalidate()
                            val otpText = text
                            onInputChanged?.invoke(otpText)
                            if (charBuffer.size == digitCount) onInputCompleted?.apply {
                                hideKeyboard()
                                invoke(otpText)
                            }
                            return true
                        }
                    }

                    KeyEvent.KEYCODE_DEL -> {
                        isError = false
                        charBuffer.removeLastOrNull()
                        invalidate()
                        val otpText = text
                        onInputChanged?.invoke(otpText)
                        return true
                    }

                    KeyEvent.KEYCODE_ENTER -> {
                        hideKeyboard()
                        return true
                    }
                }
            }
            return false
        }

    }

    private fun hideKeyboard() {
        try {
            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                windowToken,
                InputMethodManager.RESULT_UNCHANGED_SHOWN
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}