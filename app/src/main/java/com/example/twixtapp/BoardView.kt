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
    private var tempRow = 0
    private var tempColumn = 0
    private var redWalls = HashMap<Wall,WallCoords>()
    private var blackWalls = HashMap<Wall,WallCoords>()
    private var tempWalls = HashMap<Wall,WallCoords>()


    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val setupCoords = SetupCoords()

    override fun onDraw(canvas: Canvas) {
        // call the super method to keep any drawing from the parent side.
        super.onDraw(canvas)
        val imageBounds: Rect = canvas.clipBounds  // Adjust this for where you want it

        backgroundImage?.bounds = imageBounds
        backgroundImage?.draw(canvas)

        drawWalls(canvas, redWalls, Color.RED)
        drawWalls(canvas, blackWalls, Color.BLACK)
        drawWalls(canvas, tempWalls, Color.GRAY)
        drawPegs(canvas)
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

    private fun drawWalls(canvas: Canvas, walls: HashMap<Wall,WallCoords>, color: Int) {
        paint.style = Paint.Style.FILL
        paint.color = color
        paint.strokeWidth = 5f
        for(wall in walls.values) {
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
        if(!boardArray[tempRow][tempColumn].isConfirmed) {
            boardArray[tempRow][tempColumn].isShown = false
        }
        boardArray[row][column].isShown = true
        boardArray[row][column].color = Color.GRAY
        tempRow = row
        tempColumn = column

        val rowChecks = arrayOf(   -2,-2,-1,-1, 1,1, 2,2)
        val columnChecks = arrayOf(-1, 1,-2, 2,-2,2,-1,1)
        tempWalls = HashMap()


        for(i in rowChecks.indices)
        {
            val checkRow = row+rowChecks[i]
            val checkCol = column+columnChecks[i]
            if(checkRow < 0 || checkRow > 23 || checkCol < 0 || checkCol > 23) {
                continue
            }
            else if(boardArray[checkRow][checkCol].isShown) {
                addWall(Wall(row, checkRow, column, checkCol), tempWalls)
            }
        }
    }

    fun confirmPeg() {
        boardArray[tempRow][tempColumn].isShown = true
        boardArray[tempRow][tempColumn].isConfirmed = true
        boardArray[tempRow][tempColumn].color = Color.RED

//        val rowChecks = arrayOf(   -2,-2,-1,-1, 1,1, 2,2)
//        val columnChecks = arrayOf(-1, 1,-2, 2,-2,2,-1,1)
//
//
//        for(i in rowChecks.indices)
//        {
//            val checkRow = tempRow+rowChecks[i]
//            val checkCol = tempColumn+columnChecks[i]
//            if(checkRow < 0 || checkRow > 23 || checkCol < 0 || checkCol > 23) {
//                continue
//            }
//            else if(boardArray[checkRow][checkCol].isShown) {
//                addWall(Wall(tempRow, checkRow, tempColumn, checkCol), redWalls)
//            }
//        }

        for((wall, wallCoords) in tempWalls.entries) {
            redWalls.put(wall,wallCoords)
        }
        tempWalls = HashMap()


        this.invalidate()
    }

    private fun addWall(wall: Wall, walls: HashMap<Wall,WallCoords>) {
        if(wall !in walls) {
            walls[wall] = WallCoords(
                xCoords[wall.row1][wall.column1],
                xCoords[wall.row2][wall.column2],
                yCoords[wall.row1][wall.column1],
                yCoords[wall.row2][wall.column2])
        }
    }

    fun touchPoint(x: Float, y: Float, sf: Float) {
        Log.d("rootbeer", "touched ($x, $y)")
        val xOnBoard = calculateCord(x, sf) - this.translationX/sf
        val yOnBoard = calculateCord(y, sf) - this.translationY/sf
        Log.d("rootbeer", "($xOnBoard, $yOnBoard) relatively")
        val column = kotlin.math.floor(xOnBoard / (this.width / 24))
        val row = kotlin.math.floor(yOnBoard / (this.width / 24))
        Log.d("rootbeer", "row: $row, column: $column")
        if(!boardArray[row.toInt()][column.toInt()].isConfirmed) {
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