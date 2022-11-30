package com.example.twixtapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat

class BoardView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    // View size in pixels
    private var size = 320
    private var backgroundImage = ContextCompat.getDrawable(this.context, R.drawable.board)

    private var boardArray = Array(24) { IntArray(24) }
    private var xCoords = Array(24) { FloatArray(24) }
    private var yCoords = Array(24) { FloatArray(24) }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val setupCoords = SetupCoords()

    override fun onDraw(canvas: Canvas) {
        // call the super method to keep any drawing from the parent side.
        super.onDraw(canvas)
        val imageBounds: Rect = canvas.clipBounds  // Adjust this for where you want it

        backgroundImage?.bounds = imageBounds
        backgroundImage?.draw(canvas)

        drawPegs(canvas)
    }

    private fun drawPegs(canvas: Canvas) {
//        Log.d("rootbeer", "in drawPegs")
        paint.color = Color.CYAN
        paint.style = Paint.Style.FILL
        for((r, row) in boardArray.withIndex()) {
            for((c, column) in row.withIndex()) {
//                canvas.drawCircle(xCoords[r][c], yCoords[r][c], 8f, paint)
                if(boardArray[r][c] == 1) {
                    canvas.drawCircle(xCoords[r][c], yCoords[r][c], 8f, paint)
                    Log.d("rootbeer", "$r, $c")
                    Log.d("rootbeer", "" + xCoords[r][c] + " " + yCoords[r][c])
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 1
        size = measuredWidth.coerceAtMost(measuredHeight)
        // 2
        setMeasuredDimension(size, size)
        setupCoords()
    }
    fun touchPoint(x: Float, y: Float, sf: Float) {
        Log.d("rootbeer", "touched ($x, $y)")
        var xOnBoard = calculateCord(x, sf) - this.translationX/sf
        var yOnBoard = calculateCord(y, sf) - this.translationY/sf
        Log.d("rootbeer", "($xOnBoard, $yOnBoard) relatively")
        val column = kotlin.math.floor(xOnBoard / (this.width / 24))
        val row = kotlin.math.floor(yOnBoard / (this.width / 24))
        Log.d("rootbeer", "row: $row, column: $column")
        boardArray[row.toInt()][column.toInt()] = 1
        this.invalidate()
    }

    private fun calculateCord(x: Float, sf: Float) : Float {
        val nx = x/size
        val num = sf - 1
        val denom = 2f*sf
        val adjust = nx/sf
        return ((num/denom)+adjust) * size
    }

    private fun setupCoords() {
        xCoords = setupCoords.getXCoords(size)
        yCoords = setupCoords.getYCoords(size)
    }

}