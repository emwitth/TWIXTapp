package com.example.twixtapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class BoardView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    // View size in pixels
    private var size = 320
    private var backgroundImage = ContextCompat.getDrawable(this.context, R.drawable.board)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val setupCoords = SetupCoords()

    private var boardArray = Array(24) { Array(24){ Node() } }
    private var xCoords = Array(24) { FloatArray(24) }
    private var yCoords = Array(24) { FloatArray(24) }
    private var redWalls = HashMap<Wall,Int>()
    private var blackWalls = HashMap<Wall,Int>()
    private var tempWalls = HashMap<Wall,Int>()

    override fun onDraw(canvas: Canvas) {
        // call the super method to keep any drawing from the parent side.
        super.onDraw(canvas)
        val imageBounds: Rect = canvas.clipBounds  // Adjust this for where you want it

        backgroundImage?.bounds = imageBounds
        backgroundImage?.draw(canvas)

        drawWalls(canvas, redWalls)
        drawWalls(canvas, blackWalls)
        drawWalls(canvas, tempWalls)
        drawPegs(canvas)
    }

    private fun drawPegs(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        for((r, row) in boardArray.withIndex()) {
            for((c, column) in row.withIndex()) {
                if(boardArray[r][c].isShown) {
                    paint.color = boardArray[r][c].color
                    canvas.drawCircle(xCoords[r][c], yCoords[r][c], 8f, paint)
                }
            }
        }
    }

    private fun drawWalls(canvas: Canvas, walls: HashMap<Wall,Int>) {
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 5f
        for((wall, color) in walls.entries) {
            paint.color = color
            canvas.drawLine(
                xCoords[wall.row1][wall.column1],
                yCoords[wall.row1][wall.column1],
                xCoords[wall.row2][wall.column2],
                yCoords[wall.row2][wall.column2], paint)
        }
    }

    fun setElemens(boardArray: Array<Array<Node>>, redWalls: HashMap<Wall,Int>, blackWalls: HashMap<Wall,Int>, tempWalls: HashMap<Wall,Int>) {
        this.boardArray = boardArray
        this.redWalls = redWalls
        this.blackWalls = blackWalls
        this.tempWalls = tempWalls
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 1
        size = measuredWidth.coerceAtMost(measuredHeight)
        // 2
        setMeasuredDimension(size, size)
        setupCoords()
    }

    fun calcRow(y: Float, sf: Float): Int {
        val yOnBoard = calculateCord(y, sf) - this.translationY/sf
        val row = kotlin.math.floor(yOnBoard / (this.width / 24))
        return row.toInt()
    }

    fun calcColumn(x: Float, sf: Float): Int {
        val xOnBoard = calculateCord(x, sf) - this.translationX / sf
        val column = kotlin.math.floor(xOnBoard / (this.width / 24))
        return column.toInt()
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