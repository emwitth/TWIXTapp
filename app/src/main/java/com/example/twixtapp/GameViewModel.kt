package com.example.twixtapp

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    private var isRedTurn = true
    private var hasChosenValidOption = false
    var hasRedWon = false
    var hasBlackWon = false

    var boardArray = Array(24) { Array(24){ Node() } }
    private var tempRow = 0
    private var tempColumn = 0
    var redWalls = HashMap<Wall,Int>()
    var blackWalls = HashMap<Wall,Int>()
    var tempWalls = HashMap<Wall,Int>()

    private fun addPeg(row: Int, column: Int) {
        if(isRedTurn && (row == 0 || row == 23)) {
            return
        }
        if(!isRedTurn && (column == 0 || column == 23)) {
            return
        }

        if(!boardArray[tempRow][tempColumn].isConfirmed) {
            boardArray[tempRow][tempColumn].isShown = false
        }

        hasChosenValidOption = true

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
                redWalls[wall] = Color.RED
                boardArray[wall.row1][wall.column1].addCon(boardArray[wall.row2][wall.column2])
                boardArray[wall.row2][wall.column2].addCon(boardArray[wall.row1][wall.column1])
            }
        }
        else {
            boardArray[tempRow][tempColumn].color = Color.BLACK
            boardArray[tempRow][tempColumn].isBlack = true

            for ((wall, wallCoords) in tempWalls.entries) {
                blackWalls[wall] = wallCoords
                blackWalls[wall] = Color.BLACK
                boardArray[wall.row1][wall.column1].addCon(boardArray[wall.row2][wall.column2])
                boardArray[wall.row2][wall.column2].addCon(boardArray[wall.row1][wall.column1])

            }
        }
        tempWalls = HashMap()

        boardArray[tempRow][tempColumn].row = tempRow
        boardArray[tempRow][tempColumn].column = tempColumn

        checkWin()

        isRedTurn = !isRedTurn
        hasChosenValidOption = false

    }

    private fun addWall(wall: Wall, walls: HashMap<Wall,Int>, color: Int) {
        if(isRedTurn) {
            if(isBlockingWall(wall, blackWalls.keys)){return}
        }
        else {
            if(isBlockingWall(wall, redWalls.keys)){return}
        }
        if(wall !in walls) {
            walls[wall] = color
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

    private fun checkWin() {
        Log.v("rootbeer", "checking win")
        if(isRedTurn) {
            for(row in boardArray.indices) {
                if(boardArray[row][0].isRed) {
                    Log.v("rootbeer", "node (" + boardArray[row][0].row + ", " + boardArray[row][0].column + ") is red" )
                    if(nodeConnectsToEnd(boardArray[row][0], mutableSetOf())) {
                        hasRedWon = true
                        Log.v("rootbeer", "BLACK WINS!")
                        break
                    }
                }
                if(boardArray[row][1].isRed) {
                    Log.v("rootbeer", "node (" + boardArray[row][1].row + ", " + boardArray[row][0].column + ") is red" )
                    if(nodeConnectsToEnd(boardArray[row][1], mutableSetOf())) {
                        hasRedWon = true
                        Log.v("rootbeer", "RED WINS!")
                        break
                    }
                }
            }
        }
        else {
            for(node in boardArray[0]) {
                if(node.isBlack) {
                    Log.v("rootbeer", "node (" + node.row + ", " + node.column + ") is black" )
                    if(nodeConnectsToEnd(node, mutableSetOf())) {
                        hasBlackWon = true
                        Log.v("rootbeer", "BLACK WINS!")
                        break
                    }
                }
            }
            for(node in boardArray[1]) {
                if(node.isBlack && !hasBlackWon) {
                    Log.v("rootbeer", "node (" + node.row + ", " + node.column + ") is black" )
                    if(nodeConnectsToEnd(node, mutableSetOf())) {
                        hasBlackWon = true
                        Log.v("rootbeer", "BLACK WINS!")
                        break
                    }
                }
            }
        }
    }

    private fun nodeConnectsToEnd(n: Node, seenNodes: MutableSet<Node>) : Boolean {
        Log.v("rootbeer", "node: " + n.row + ", " + n.column)
        if(isRedTurn && (n.column == 23 || n.column == 22)) {
            return true
        }
        else if(!isRedTurn && (n.row == 23 || n.row == 22)) {
            return true
        }
        var toReturn = false
        seenNodes.add(n)
        for(con in n.cons) {
            if(con !in seenNodes) {
                toReturn = toReturn || nodeConnectsToEnd(con, seenNodes)
            }
        }
        return toReturn
    }

    private fun ccw(Ax:Int, Ay:Int, Bx:Int, By:Int, Cx:Int, Cy:Int) : Boolean {
        return (Cy-Ay) * (Bx-Ax) > (By-Ay) * (Cx-Ax)
    }

    fun touchPoint(row: Int, column: Int) {
        if(hasRedWon || hasBlackWon) {
            return
        }

        if(row in 0 .. 23 && column in 0 .. 23 &&
            !boardArray[row][column].isConfirmed ) {
            addPeg(row, column)
        }
    }

    fun isRedTurn() : Boolean {
        return isRedTurn
    }

    fun hasChosenValidOption() : Boolean {
        return hasChosenValidOption
    }
}