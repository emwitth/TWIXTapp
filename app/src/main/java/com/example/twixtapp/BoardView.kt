package com.example.twixtapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.abs

class BoardView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    // View size in pixels
    private var size = 320
    private var backgroundImage = ContextCompat.getDrawable(this.context, R.drawable.board)
    private var isRedTurn = true

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

        drawWalls(canvas, redWalls)
        drawWalls(canvas, blackWalls)
        drawWalls(canvas, tempWalls)
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

    private fun drawWalls(canvas: Canvas, walls: HashMap<Wall,WallCoords>) {
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 5f
        for(wall in walls.values) {
            paint.color = wall.color
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

        var color = if(isRedTurn){
            Color.parseColor("#ffb6c1")
        } else {
            Color.GRAY
        }
        boardArray[row][column].color = color
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
            else if(boardArray[checkRow][checkCol].isShown &&
                    boardArray[checkRow][checkCol].hasRightColor(isRedTurn)) {
                if(row < checkRow) {
                    addWall(Wall(row, checkRow, column, checkCol), tempWalls, color)
                }
                else {
                    addWall(Wall(checkRow, row, checkCol, column), tempWalls, color)
                }
            }
        }
    }

    fun confirmPeg() {
        boardArray[tempRow][tempColumn].isShown = true
        boardArray[tempRow][tempColumn].isConfirmed = true
        if(isRedTurn) {
            boardArray[tempRow][tempColumn].color = Color.RED
            boardArray[tempRow][tempColumn].isRed = true

            for ((wall, wallCoords) in tempWalls.entries) {
                redWalls[wall] = wallCoords
                redWalls[wall]?.color = Color.RED
            }
        }
        else {
            boardArray[tempRow][tempColumn].color = Color.BLACK
            boardArray[tempRow][tempColumn].isBlack = true

            for ((wall, wallCoords) in tempWalls.entries) {
                blackWalls[wall] = wallCoords
                blackWalls[wall]?.color = Color.BLACK
            }
        }
        tempWalls = HashMap()

        isRedTurn = !isRedTurn

        this.invalidate()
    }

    private fun addWall(wall: Wall, walls: HashMap<Wall,WallCoords>, color: Int) {
        if(isRedTurn) {
            if(isBlockingWall(wall, blackWalls.keys)){return}
        }
        else {
            if(isBlockingWall(wall, redWalls.keys)){return}
        }
        if(wall !in walls) {
            walls[wall] = WallCoords(
                xCoords[wall.row1][wall.column1],
                xCoords[wall.row2][wall.column2],
                yCoords[wall.row1][wall.column1],
                yCoords[wall.row2][wall.column2],
                color)
        }
    }

    private fun isBlockingWall(wall: Wall, walls: MutableSet<Wall>) : Boolean {
        for(w in walls) {
            if(
                ccw(wall.row1,wall.column1,w.row1,w.column1,w.row2,w.column2) !=
                ccw(wall.row2,wall.column2,w.row1,w.column1,w.row2,w.column2) &&
                ccw(wall.row1,wall.column1,wall.row2,wall.column2,w.row1,w.column1) !=
                ccw(wall.row1,wall.column1,wall.row2,wall.column2,w.row2,w.column2)
            )
            {
                return true
            }
        }
        // if no walls intersecting
        return false
    }

    fun ccw(Ax:Int, Ay:Int, Bx:Int, By:Int, Cx:Int, Cy:Int) : Boolean {
        return (Cy-Ay) * (Bx-Ax) > (By-Ay) * (Cx-Ax)
    }

    fun touchPoint(x: Float, y: Float, sf: Float) {
        val xOnBoard = calculateCord(x, sf) - this.translationX/sf
        val yOnBoard = calculateCord(y, sf) - this.translationY/sf
        val column = kotlin.math.floor(xOnBoard / (this.width / 24))
        val row = kotlin.math.floor(yOnBoard / (this.width / 24))
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

    fun isRedTurn() : Boolean {
        return isRedTurn
    }

}