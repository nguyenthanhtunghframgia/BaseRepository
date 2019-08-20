package com.example.baserepository.customview

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import com.example.baserepository.R
import kotlin.math.max

class ProgressWheel(context: Context, attributeSet: AttributeSet)
    : View(context, attributeSet) {
    //Constant
    private val PROPERTY_PROGRESS = "PROPERTY_PROGRESS"

    //Layout size
    private var layoutWidth = 0
    private var layoutHeight = 0

    //Delay time between redraw
    private var delayTime = 20L

    //Check if spin
    private var isSpin = false

    //Percent text
    private var percentText = ""

    //Progress
    private var progress = 0F

    //Spin speed
    private var spinSpeed = 2F

    //Paint
    private var baseProgressPaint = Paint()
    private var innerProgressPaint = Paint()
    private var outtaProgressPaint = Paint()
    private var textProgressPaint = Paint()

    //Rectangle
    private var baseProgressRectF = RectF()
    private var innerProgressRectF = RectF()
    private var outtaProgressRectF = RectF()

    init {
        parseAttributes(context.obtainStyledAttributes(attributeSet, R.styleable.ProgressWheel))
    }

    private fun parseAttributes(obtainStyledAttributes: TypedArray) {
        obtainStyledAttributes.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        layoutWidth = w
        layoutHeight = h
        setUpPaint()
        setUpBound()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //Draw all rectF
        canvas?.apply {
            drawArc(baseProgressRectF, 360F, 360F, false, baseProgressPaint)
        }

        //Check spin and draw
        if (isSpin) {
            canvas?.apply {
                drawArc(innerProgressRectF, -90F, progress, false, innerProgressPaint)
                drawArc(outtaProgressRectF, -90F, progress, false, outtaProgressPaint)

                //Draw the text (attempts to center it horizontally and vertically)
                val textHeight = textProgressPaint.descent() - textProgressPaint.ascent()
                val verticalTextOffset = textHeight / 2 - textProgressPaint.descent()
                val horizontalTextOffset = textProgressPaint.measureText(percentText) / 2
                drawText(
                    percentText,
                    width / 2 - horizontalTextOffset,
                    height / 2 + verticalTextOffset,
                    textProgressPaint)
            }
        } else {
            canvas?.apply {
                drawArc(innerProgressRectF, -90F, progress, false, innerProgressPaint)
                drawArc(outtaProgressRectF, -90F, progress, false, outtaProgressPaint)

                //Draw the text (attempts to center it horizontally and vertically)
                val textHeight = textProgressPaint.descent() - textProgressPaint.ascent()
                val verticalTextOffset = textHeight / 2 - textProgressPaint.descent()
                val horizontalTextOffset = textProgressPaint.measureText(percentText) / 2
                drawText(
                    percentText,
                    width / 2 - horizontalTextOffset,
                    height / 2 + verticalTextOffset,
                    textProgressPaint)
            }
        }

        //Auto spin without animation
//        if (isSpin) autoSpin()
    }

    //Set progress manual
    fun setProgress(pg: Int) {
        isSpin = true
        progress = pg * 3.6F
        percentText = pg.toString().plus("%")
        if (progress >= 360F) {
            progressDone()
        }
        postInvalidateDelayed(delayTime)
    }

    private fun progressDone() {
        isSpin = false
        progress = 360F
        percentText = "Done"
    }

    private fun setUpPaint() {
        baseProgressPaint.apply {
            style = Paint.Style.STROKE
            isAntiAlias = true
            color = resources.getColor(R.color.colorPrimary)
            strokeWidth = 15F
        }

        innerProgressPaint.apply {
            style = Paint.Style.STROKE
            isAntiAlias = true
            color = resources.getColor(R.color.colorAccent)
            strokeWidth = 15F
        }

        outtaProgressPaint.apply {
            style = Paint.Style.STROKE
            isAntiAlias = true
            color = resources.getColor(R.color.colorPrimaryDark)
            strokeWidth = 15F
        }

        textProgressPaint.apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            color = resources.getColor(R.color.abc_btn_colored_borderless_text_material)
            textSize = 100F
        }
    }

    private fun setUpBound() {
        //Width should equal to height
        val minValue = minOf(layoutWidth, layoutHeight)

        //Calc offset if need
        val xOffSet = layoutWidth - minValue
        val yOffSet = layoutHeight - minValue

        //Get width and height
        val width = width
        val height = height

        //Init rectF
        baseProgressRectF = RectF(
            1.5F * 15F,
            1.5F * 15F,
            width - 1.5F * 15F,
            height - 1.5F * 15F
        )

        innerProgressRectF = RectF(
            2F * 15F,
            2F * 15F,
            width - 2F * 15F,
            height - 2F * 15F
        )

        outtaProgressRectF = RectF(
            1F * 15F,
            1F * 15F,
            width - 1F * 15F,
            height - 1F * 15F
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //Declare size
        val size: Int

        //Get measure width and height (can not get width and height here)
        //Minus padding if exist
        val width = measuredWidth
        val height = measuredHeight

        //Get width and height measure mode
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        //Check mode and set measure dimension
        size = if (heightMode != MeasureSpec.UNSPECIFIED && widthMode != MeasureSpec.UNSPECIFIED) {
            if (width > height) {
                height
            } else {
                width
            }
        } else {
            max(width, height)
        }

        //Set measure dimension at least one time
        setMeasuredDimension(size, size)
    }

    private fun autoSpin() {
        progress += spinSpeed
        percentText = (progress / 3.6).toInt().toString().plus("%")
        if (progress > 360F) {
            progress = 0F
        }
        postInvalidateDelayed(delayTime)
    }

    fun autoSpinWithAnim() {
        val propertyProgress = PropertyValuesHolder.ofFloat(PROPERTY_PROGRESS, 0F, 360F)
        ValueAnimator().apply {
            setValues(propertyProgress)
            duration = 10000
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animation ->
                progress = animation.getAnimatedValue(PROPERTY_PROGRESS) as Float
                percentText = (progress / 3.6).toInt().toString().plus("%")
                postInvalidateDelayed(delayTime)
            }
            start()
        }
    }

    fun startSpin() {
        isSpin = true
        postInvalidate()
    }

    fun stopSpin() {
        isSpin = false
        postInvalidate()
    }
}
