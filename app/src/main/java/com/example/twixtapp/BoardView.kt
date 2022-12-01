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

    private var boardArray = Array(24) { Array(24){ Node() } }
    private var xCoords = Array(24) { FloatArray(24) }
    private var yCoords = Array(24) { FloatArray(24) }
    private var redWalls = HashMap<Wall,WallCoords>()
    private var blackWalls = HashMap<Wall,WallCoords>()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val setupCoords = SetupCoords()

    override fun onDraw(canvas: Canvas) {
        // call the super method to keep any drawing from the parent side.
        super.onDraw(canvas)
        val imageBounds: Rect = canvas.clipBounds  // Adjust this for where you want it

        backgroundImage?.bounds = imageBounds
        backgroundImage?.draw(canvas)

        drawPegs(canvas)
        drawRedWalls(canvas)
    }

    private fun drawPegs(canvas: Canvas) {
//        Log.d("rootbeer", "in drawPegs")
        paint.style = Paint.Style.FILL
        for((r, row) in boardArray.withIndex()) {
            for((c, column) in row.withIndex()) {
                if(boardArray[r][c].isShown) {
                    paint.color = boardArray[r][c].color
                    canvas.drawCircle(xCoords[r][c], yCoords[r][c], 8f, paint)
                    Log.d("rootbeer", "$r, $c")
                    Log.d("rootbeer", "" + xCoords[r][c] + " " + yCoords[r][c])
                }
            }
        }
    }

    private fun drawRedWalls(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        paint.color = Color.CYAN
        paint.strokeWidth = 5f
        for(wall in redWalls.values) {
            canvas.drawLine(wall.x1, wall.y1, wall.x2, wall.y2, paint)
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

    private fun addPeg(row: Int, column: Int) {
        boardArray[row][column].isShown = true
        var rowChecks = arrayOf(   -2,-2,-1,-1, 1,1, 2,2)
        var columnChecks = arrayOf(-1, 1,-2, 2,-2,2,-1,1)


        for(i in rowChecks.indices)
        {
            var checkRow = row+rowChecks[i]
            var checkCol = column+columnChecks[i]
            if(checkRow < 0 || checkRow > 23 || checkCol < 0 || checkCol > 23) {
                continue
            }
            else if(boardArray[row+rowChecks[i]][column+columnChecks[i]].isShown) {
                addWall(Wall(row, row+rowChecks[i], column, column+columnChecks[i]))
            }
        }
    }

    private fun addWall(wall: Wall) {
        if(wall !in redWalls) {
            redWalls.put(wall,
                WallCoords(
                    xCoords[wall.row1][wall.column1],
                    xCoords[wall.row2][wall.column2],
                    yCoords[wall.row1][wall.column1],
                    yCoords[wall.row2][wall.column2])
            )
        }
    }

    fun touchPoint(x: Float, y: Float, sf: Float) {
        Log.d("rootbeer", "touched ($x, $y)")
        var xOnBoard = calculateCord(x, sf) - this.translationX/sf
        var yOnBoard = calculateCord(y, sf) - this.translationY/sf
        Log.d("rootbeer", "($xOnBoard, $yOnBoard) relatively")
        val column = kotlin.math.floor(xOnBoard / (this.width / 24))
        val row = kotlin.math.floor(yOnBoard / (this.width / 24))
        Log.d("rootbeer", "row: $row, column: $column")
        if(!boardArray[row.toInt()][column.toInt()].isShown) {
            addPeg(row.toInt(), column.toInt())
        }
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