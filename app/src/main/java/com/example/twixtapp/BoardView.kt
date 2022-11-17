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

    override fun onDraw(canvas: Canvas) {
        // call the super method to keep any drawing from the parent side.
        super.onDraw(canvas)
        val imageBounds: Rect = canvas.clipBounds  // Adjust this for where you want it

        backgroundImage?.bounds = imageBounds
        backgroundImage?.draw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {      super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 1
        size = measuredWidth.coerceAtMost(measuredHeight)
        // 2
        setMeasuredDimension(size, size)
    }

}