package com.example.baserepository.dialog

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.example.baserepository.R
import kotlin.math.min

class ProgressWheel(context: Context, attributeSet: AttributeSet)
    : View(context, attributeSet) {
    //View size
    private var layoutWidth = 0
    private var layoutHeight = 0

    //Padding
    private var progressWheelPaddingTop = 5
    private var progressWheelPaddingBottom = 5
    private var progressWheelPaddingLeft = 5
    private var progressWheelPaddingRight = 5

    //Size
    private var textSize = 120
    private var baseCircleWidth = 5
    private var innerCircleWidth = 5
    private var outtaCircleWidth = 5

    //Paint
    private var textPain = Paint()
    private var baseCirclePain = Paint()
    private var innerCirclePain = Paint()
    private var outtaCirclePain = Paint()

    //Color
    private var textColor = 0xAA008577
    private var baseCircleColor = 0xAA008577
    private var innerCircleColor = 0xAA008577
    private var outtaCircleColor = 0xAA008577

    //RectF
    private var baseCircleBound = RectF()
    private var innerCircleBound = RectF()
    private var outtaCircleBound = RectF()

    //Text
    private var percentText = ""

    //Other
    private var spinSpeed = 2
    private var delayTimes = 10
    private var progress = 0F
    private var isSpin = false

    init {
        parseAttributes(context.obtainStyledAttributes(attributeSet, R.styleable.ProgressWheel))
    }

    private fun parseAttributes(obtainStyledAttributes: TypedArray) {
        //View size
        layoutWidth = obtainStyledAttributes.getDimension(R.styleable.ProgressWheel_pwLayoutWidth, layoutWidth.toFloat()).toInt()
        layoutHeight = obtainStyledAttributes.getDimension(R.styleable.ProgressWheel_pwLayoutWidth, layoutHeight.toFloat()).toInt()

        //Padding
        progressWheelPaddingTop = obtainStyledAttributes.getDimension(R.styleable.ProgressWheel_progressWheelPaddingTop, progressWheelPaddingTop.toFloat()).toInt()
        progressWheelPaddingBottom = obtainStyledAttributes.getDimension(R.styleable.ProgressWheel_progressWheelPaddingBottom, progressWheelPaddingBottom.toFloat()).toInt()
        progressWheelPaddingLeft = obtainStyledAttributes.getDimension(R.styleable.ProgressWheel_progressWheelPaddingLeft, progressWheelPaddingLeft.toFloat()).toInt()
        progressWheelPaddingRight = obtainStyledAttributes.getDimension(R.styleable.ProgressWheel_progressWheelPaddingRight, progressWheelPaddingRight.toFloat()).toInt()

        //Size
        textSize = obtainStyledAttributes.getDimension(R.styleable.ProgressWheel_textColor, textSize.toFloat()).toInt()
        baseCircleWidth = obtainStyledAttributes.getDimension(R.styleable.ProgressWheel_baseCircleWidth, baseCircleWidth.toFloat()).toInt()
        innerCircleWidth = obtainStyledAttributes.getDimension(R.styleable.ProgressWheel_innerCircleWidth, innerCircleWidth.toFloat()).toInt()
        outtaCircleWidth = obtainStyledAttributes.getDimension(R.styleable.ProgressWheel_outtaCircleWidth, outtaCircleWidth.toFloat()).toInt()

        //Color
        textColor = obtainStyledAttributes.getColor(R.styleable.ProgressWheel_baseCircleColor, textColor.toInt()).toLong()
        baseCircleColor = obtainStyledAttributes.getColor(R.styleable.ProgressWheel_baseCircleColor, baseCircleColor.toInt()).toLong()
        innerCircleColor = obtainStyledAttributes.getColor(R.styleable.ProgressWheel_innerCircleColor, innerCircleColor.toInt()).toLong()
        outtaCircleColor = obtainStyledAttributes.getColor(R.styleable.ProgressWheel_outtaCircleColor, outtaCircleColor.toInt()).toLong()

        //Other
        spinSpeed = obtainStyledAttributes.getDimension(R.styleable.ProgressWheel_spinSpeed, spinSpeed.toFloat()).toInt()
        delayTimes = obtainStyledAttributes.getDimension(R.styleable.ProgressWheel_delayTimes, delayTimes.toFloat()).toInt()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //Get width-height
        val size: Int
        val width = measuredWidth
        val height = measuredHeight
        val widthWithoutPadding = width - getCustomPaddingLeft() - getCustomPaddingRight()
        val heightWithoutPadding = height - getCustomPaddingTop() - getCustomPaddingBottom()

        //Measure mode
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)

        //Measure size
        size = if (heightMode != MeasureSpec.UNSPECIFIED && widthMode != MeasureSpec.UNSPECIFIED) {
            if (widthWithoutPadding > heightWithoutPadding) {
                heightWithoutPadding
            } else {
                widthWithoutPadding
            }
        } else {
            min(heightWithoutPadding, widthWithoutPadding)
        }

        //Set measure size
        setMeasuredDimension(
            size + paddingLeft + paddingRight,
            size + paddingTop + paddingBottom)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        layoutWidth = w
        layoutHeight = h
        //Setup
        setupPaints()
        setupRectF()
        invalidate()
    }

    private fun setupRectF() {
        // Width should equal to Height, find the min value to setup the circle
        val minValue = min(layoutWidth, layoutHeight)

        // Calc the Offset if needed
        val xOffset = layoutWidth - minValue
        val yOffset = layoutHeight - minValue

        // Add the offset
        progressWheelPaddingTop = getCustomPaddingTop() + yOffset / 2
        progressWheelPaddingBottom = getCustomPaddingBottom() + yOffset / 2
        progressWheelPaddingLeft = getCustomPaddingLeft() + xOffset / 2
        progressWheelPaddingRight = getCustomPaddingRight() + xOffset / 2

        val width = width
        val height = height

        baseCircleBound = RectF(
            progressWheelPaddingLeft + 1f * baseCircleWidth,
            progressWheelPaddingTop + 1f * baseCircleWidth,
            width.toFloat() - progressWheelPaddingRight.toFloat() - 1f * baseCircleWidth,
            height.toFloat() - progressWheelPaddingBottom.toFloat() - 1f * baseCircleWidth)

        innerCircleBound = RectF(
            progressWheelPaddingLeft + 1.5f * baseCircleWidth,
            progressWheelPaddingTop + 1.5f * baseCircleWidth,
            width.toFloat() - progressWheelPaddingRight.toFloat() - 1.5f * baseCircleWidth,
            height.toFloat() - progressWheelPaddingBottom.toFloat() - 1.5f * baseCircleWidth)

        outtaCircleBound = RectF(
            progressWheelPaddingLeft + 0.5f * baseCircleWidth,
            progressWheelPaddingTop + 0.5f * baseCircleWidth,
            width.toFloat() - progressWheelPaddingRight.toFloat() - 0.5f * baseCircleWidth,
            height.toFloat() - progressWheelPaddingBottom.toFloat() - 0.5f * baseCircleWidth)
    }

    private fun setupPaints() {
        textPain.apply {
            color = textColor.toInt()
            isAntiAlias = true
            style = Paint.Style.FILL
            textSize = this@ProgressWheel.textSize.toFloat()
        }

        baseCirclePain.apply {
            color = baseCircleColor.toInt()
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = baseCircleWidth.toFloat()
        }

        innerCirclePain.apply {
            color = innerCircleColor.toInt()
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = innerCircleWidth.toFloat()
        }

        outtaCirclePain.apply {
            color = outtaCircleColor.toInt()
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = outtaCircleWidth.toFloat()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            drawArc(baseCircleBound, 0f, 360f, false, baseCirclePain)
            if (true) {
                drawArc(innerCircleBound, -90f, progress, false, innerCirclePain)
                drawArc(outtaCircleBound, -90f, progress, false, outtaCirclePain)

                val textHeight = textPain.descent() - textPain.ascent()
                val verticalTextOffset = textHeight / 2 - textPain.descent()
                val horizontalTextOffset = textPain.measureText(percentText) / 2
                drawText(percentText,
                    width / 2 - horizontalTextOffset,
                    height / 2 + verticalTextOffset,
                    textPain)
            }
        }

        if (isSpin) scheduleRedraw()
    }

    fun scheduleRedraw() {
        progress += spinSpeed
        percentText = (progress.div(3.6)).toInt().toString().plus("%")
        if (progress > 360) {
            stopSpinning()
        }
        postInvalidateDelayed(delayTimes.toLong())
    }

    fun startSpin() {
        isSpin = true
        postInvalidate()
    }

    fun setProgress(i: Int) {
        isSpin = false
        progress = i.toFloat()
        percentText = progress.toString().plus("%")
        postInvalidate()
    }

    fun resetCount() {
        progress = 0f
        invalidate()
    }

    fun stopSpinning() {
        progress = 360f
        postInvalidate()
    }

    private fun getCustomPaddingTop(): Int {
        return progressWheelPaddingTop
    }

    private fun getCustomPaddingBottom(): Int {
        return progressWheelPaddingBottom
    }

    private fun getCustomPaddingLeft(): Int {
        return progressWheelPaddingLeft
    }

    private fun getCustomPaddingRight(): Int {
        return progressWheelPaddingRight
    }
}
