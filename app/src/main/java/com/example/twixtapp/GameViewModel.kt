package com.example.twixtapp

import android.graphics.Color
import android.util.Log
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

    fun reset() {
        isRedTurn = true
        hasChosenValidOption = false
        hasRedWon = false
        hasBlackWon = false

        boardArray = Array(24) { Array(24){ Node() } }
        tempRow = 0
        tempColumn = 0
        redWalls = HashMap<Wall,Int>()
        blackWalls = HashMap<Wall,Int>()
        tempWalls = HashMap<Wall,Int>()
    }

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

    fun formatForFile() : List<String> {
        var toReturn = mutableListOf<String>()

        toReturn.add(isRedTurn.toString())
        toReturn.add(hasChosenValidOption.toString())

        val nodes = formatBoardArrayForFile()
        toReturn.add(nodes.size.toString())
        toReturn.addAll(nodes)

        var walls = formatWallsForFile(redWalls)
        toReturn.add(walls.size.toString())
        toReturn.addAll(walls)

        walls = formatWallsForFile(blackWalls)
        toReturn.add(walls.size.toString())
        toReturn.addAll(walls)

        return toReturn
    }

    private fun formatBoardArrayForFile() : List<String> {
        var toReturn = mutableListOf<String>()
        for(row in boardArray.indices) {
            for (node in boardArray[row]) {
                if(node.isShown) {
                    toReturn.add(
                        "" + node.column + " " + node.row + " "
                                + node.color + " " + node.isRed
                    )
                }
            }
        }
        return toReturn
    }

    private fun formatWallsForFile(walls: HashMap<Wall, Int>) : List<String> {
        var toReturn = mutableListOf<String>()
        walls.forEach() {
            toReturn.add("" + it.key.row1 + " " + it.key.row2
                    + " " + it.key.column1 + " " + it.key.column2
                    + " " + it.value )
        }
        return toReturn
    }

    fun loadBoardData(boardData: List<String>) {
        isRedTurn = boardData[0].toBoolean()
        Log.v("rootbeer", isRedTurn.toString())
        hasChosenValidOption = boardData[1].toBoolean()
        Log.v("rootbeer", hasChosenValidOption.toString())

        Log.v("rootbeer", "loading nodes")
        var startIndex = 3
        var endIndex = startIndex +  boardData[2].toInt()
        Log.v("rootbeer", "$startIndex $endIndex")
        if(startIndex != endIndex) {
            for (i in startIndex..endIndex) {
                if(i == endIndex) {
                    break
                }
                Log.v("rootbeer", "$i")
                Log.v("rootbeer", boardData[i])
                updateNode(boardData[i].split(" "))
            }
            startIndex = endIndex + 1
            endIndex = startIndex + boardData[startIndex - 1].toInt()
        }
        else {
            startIndex = endIndex + 1
            endIndex = startIndex + boardData[startIndex - 1].toInt()
        }

        Log.v("rootbeer", "loading redWalls")
        Log.v("rootbeer", "$startIndex $endIndex")
        if(startIndex != endIndex) {
            for (i in startIndex..endIndex) {
                if(i == endIndex) {
                    break
                }
                Log.v("rootbeer", "$i")
                Log.v("rootbeer", boardData[i])
                addWall(boardData[i].split(" "), redWalls)
            }
            startIndex = endIndex + 1
            endIndex = startIndex + boardData[startIndex - 1].toInt()
        }
        else {
            startIndex = endIndex + 1
            endIndex = startIndex + boardData[startIndex - 1].toInt()
        }

        Log.v("rootbeer", "loading black walls")
        Log.v("rootbeer", "$startIndex $endIndex")
        if(startIndex != endIndex) {
            for (i in startIndex..endIndex) {
                if(i == endIndex) {
                    break
                }
                Log.v("rootbeer", "$i")
                Log.v("rootbeer", boardData[i])
                addWall(boardData[i].split(" "), blackWalls)
            }
        }
    }

    private fun updateNode(nodeData: List<String>) {
        val column = nodeData[0].toInt()
        val row = nodeData[1].toInt()
        Log.v("rootbeer","$row $column")
        boardArray[row][column].isShown = true
        boardArray[row][column].color = nodeData[2].toInt()
        boardArray[row][column].isRed = nodeData[3].toBoolean()
        boardArray[row][column].isBlack = !boardArray[row][column].isRed
        boardArray[row][column].isConfirmed = true
        boardArray[row][column].row = row
        boardArray[row][column].column = column
        Log.v("rootbeer", "End update Node")
    }

    private fun addWall(wallData: List<String>, walls: HashMap<Wall, Int>) {
        Log.v("rootbeer","in add wall")
        for(d in wallData) {
            Log.v("rootbeer","$d")
        }
        val wall = Wall(wallData[0].toInt(), wallData[1].toInt(),
            wallData[2].toInt(), wallData[3].toInt())
        walls[wall] = wallData[4].toInt()
    }
}