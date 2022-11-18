package com.example.twixtapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat

class BoardView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    // View size in pixels
    private var size = 320
    private var backgroundImage = ContextCompat.getDrawable(this.context, R.drawable.board)

    private var boardArray = Array(24) { IntArray(24) }

    private var xtodraw = 0f
    private var ytodraw = 0f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas) {
        // call the super method to keep any drawing from the parent side.
        super.onDraw(canvas)
        val imageBounds: Rect = canvas.clipBounds  // Adjust this for where you want it

        backgroundImage?.bounds = imageBounds
        backgroundImage?.draw(canvas)

        drawPeg(canvas)
    }

    fun drawPeg(canvas: Canvas) {
        paint.color = Color.CYAN
        paint.style = Paint.Style.FILL

        canvas.drawCircle(xtodraw, ytodraw, 20f, paint)
        Log.d("rootbeer", "x: $xtodraw y: $ytodraw")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {      super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 1
        size = measuredWidth.coerceAtMost(measuredHeight)
        // 2
        setMeasuredDimension(size, size)
    }

    fun touchPoint(x: Float, y: Float, sf: Float) {
        xtodraw = calculateCord(x, sf) - this.translationX/sf
        ytodraw = calculateCord(y, sf) - this.translationY/sf
        this.invalidate()
    }

    fun calculateCord(x: Float, sf: Float) : Float {
        val nx = x/this.width
        val num = sf - 1
        val denom = 2f*sf
        val adjust = nx/sf
        return ((num/denom)+adjust) * this.width
    }

}