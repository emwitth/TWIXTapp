package com.example.twixtapp

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    // booleans for game state information
    var isRedTurn = true
    var hasChosenValidOption = false
    var hasRedWon = false
    var hasBlackWon = false

    // peg and wall state information
    var boardArray = Array(24) { Array(24){ Node() } }
    private var tempRow = 0
    private var tempColumn = 0
    var redWalls = HashMap<Wall,Int>()
    var blackWalls = HashMap<Wall,Int>()
    var tempWalls = HashMap<Wall,Int>()

    /**
     * resets board to beginning values, for starting a new game
     */
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

    /**
     * adds a peg 'onion skin' as well as possible walls
     */
    private fun addPeg(row: Int, column: Int) {
        // red cannot go in black rows and black cannot go in red columns
        if(isRedTurn && (row == 0 || row == 23)) {
            return
        }
        if(!isRedTurn && (column == 0 || column == 23)) {
            return
        }

        // if there was another 'onion skin' hide it
        if(!boardArray[tempRow][tempColumn].isConfirmed) {
            boardArray[tempRow][tempColumn].isShown = false
        }

        // set game state variables
        hasChosenValidOption = true
        boardArray[row][column].isShown = true

        // set the color for pegs and walls
        var color = if(isRedTurn){
            Color.parseColor("#ffb6c1")
        } else {
            Color.GRAY
        }
        boardArray[row][column].color = color
        tempRow = row
        tempColumn = column

        // where to check for pegs within walling distance
        // are pairs of row and column values
        val rowChecks = arrayOf(   -2,-2,-1,-1, 1,1, 2,2)
        val columnChecks = arrayOf(-1, 1,-2, 2,-2,2,-1,1)

        // reset tempWalls from last time they were used
        tempWalls = HashMap()


        // check holes where this peg could make a wall with other pegs
        for(i in rowChecks.indices)
        {
            // look relative to chosen peg placement
            val checkRow = row+rowChecks[i]
            val checkCol = column+columnChecks[i]
            // if the current checkRow/checkColumn is outside of the game board
            if(checkRow < 0 || checkRow > 23 || checkCol < 0 || checkCol > 23) {
                continue
            }
            // if there is a peg at the current checkRow/checkColumn
            // and it is the same color as the just placed peg
            else if(boardArray[checkRow][checkCol].isShown &&
                boardArray[checkRow][checkCol].hasRightColor(isRedTurn)) {
                // add the wall to temp walls
                if(row < checkRow) {
                    addWall(Wall(row, checkRow, column, checkCol), tempWalls, color)
                }
                else {
                    addWall(Wall(checkRow, row, checkCol, column), tempWalls, color)
                }
            }
        }
    }

    /**
     * confirms a peg if one has been added
     */
    fun confirmPeg() {
        // confirm the peg set in addPeg
        boardArray[tempRow][tempColumn].isShown = true
        boardArray[tempRow][tempColumn].isConfirmed = true
        // set red
        if(isRedTurn) {
            boardArray[tempRow][tempColumn].color = Color.RED
            boardArray[tempRow][tempColumn].isRed = true
            // set walls from tempWalls to redWalls
            for ((wall, wallCoords) in tempWalls.entries) {
                redWalls[wall] = wallCoords
                redWalls[wall] = Color.RED
                // set connection within node for win checking
                boardArray[wall.row1][wall.column1].addCon(boardArray[wall.row2][wall.column2])
                boardArray[wall.row2][wall.column2].addCon(boardArray[wall.row1][wall.column1])
            }
        }
        // or set black
        else {
            boardArray[tempRow][tempColumn].color = Color.BLACK
            boardArray[tempRow][tempColumn].isBlack = true
            // set walls from tempWalls to blackWalls
            for ((wall, wallCoords) in tempWalls.entries) {
                blackWalls[wall] = wallCoords
                blackWalls[wall] = Color.BLACK
                // set connection within node for win checking
                boardArray[wall.row1][wall.column1].addCon(boardArray[wall.row2][wall.column2])
                boardArray[wall.row2][wall.column2].addCon(boardArray[wall.row1][wall.column1])

            }
        }
        // reset the temp walls, they are confirmed now
        tempWalls = HashMap()

        // set the row and column, this is redundant but easier for checking walls later
        boardArray[tempRow][tempColumn].row = tempRow
        boardArray[tempRow][tempColumn].column = tempColumn

        // check if a win has occured
        checkWin()

        // alternate the turn color
        isRedTurn = !isRedTurn
        hasChosenValidOption = false

    }

    /**
     * adds a wall if none are blocking according to color
     */
    private fun addWall(wall: Wall, walls: HashMap<Wall,Int>, color: Int) {
        // check for blocking wall
        if(isRedTurn) {
            if(isBlockingWall(wall, blackWalls.keys)){return}
        }
        else {
            if(isBlockingWall(wall, redWalls.keys)){return}
        }
        // if no walls found to be blocking, add the wall
        if(wall !in walls) {
            walls[wall] = color
        }
    }

    /**
     * takes wall and determines if there is one already placed that blocks it
     * walls of same color can go through each other, so only check one set
     */
    private fun isBlockingWall(wall: Wall, walls: MutableSet<Wall>) : Boolean {
        // segment crossing algorithm from the internet using math
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

    /**
     * checks for a win condition
     */
    private fun checkWin() {
        Log.v("rootbeer", "checking win")
        if(isRedTurn) {
            // check first or second column for red nodes and follow until end
            // a win can trigger on the second column because black cannot block anymore
            for(row in boardArray.indices) {
                if(boardArray[row][0].isRed) {
                    if(nodeConnectsToEnd(boardArray[row][0], mutableSetOf())) {
                        hasRedWon = true
                        Log.v("rootbeer", "RED WINS!")
                        break
                    }
                }
                if(boardArray[row][1].isRed) {
                    if(nodeConnectsToEnd(boardArray[row][1], mutableSetOf())) {
                        hasRedWon = true
                        Log.v("rootbeer", "RED WINS!")
                        break
                    }
                }
            }
        }
        else {
            // check first or second row for black nodes and follow until end
            // a win can trigger on the second row because red cannot block anymore
            for(node in boardArray[0]) {
                if(node.isBlack) {
                    if(nodeConnectsToEnd(node, mutableSetOf())) {
                        hasBlackWon = true
                        Log.v("rootbeer", "BLACK WINS!")
                        break
                    }
                }
            }
            for(node in boardArray[1]) {
                if(node.isBlack && !hasBlackWon) {
                    if(nodeConnectsToEnd(node, mutableSetOf())) {
                        hasBlackWon = true
                        Log.v("rootbeer", "BLACK WINS!")
                        break
                    }
                }
            }
        }
    }

    /**
     * runs connection from start to end
     * if a path is found, true is returned, else false
     */
    private fun nodeConnectsToEnd(n: Node, seenNodes: MutableSet<Node>) : Boolean {
        // return true to end recursive calls
        // if we are at the end, we know we followed a path through the nodes all the way here
        if(isRedTurn && (n.column == 23 || n.column == 22)) {
            return true
        }
        else if(!isRedTurn && (n.row == 23 || n.row == 22)) {
            return true
        }
        // recursively go into each connecting node further in the path and return result
        var toReturn = false
        seenNodes.add(n)
        for(con in n.cons) {
            if(con !in seenNodes) {
                toReturn = toReturn || nodeConnectsToEnd(con, seenNodes)
            }
        }
        return toReturn
    }

    /**
     * line segment intersection algorithm from
     * https://bryceboe.com/2006/10/23/line-segment-intersection-algorithm/
     * I haven't taken math for a while, so it's more or less magic to me
     */
    private fun ccw(Ax:Int, Ay:Int, Bx:Int, By:Int, Cx:Int, Cy:Int) : Boolean {
        return (Cy-Ay) * (Bx-Ax) > (By-Ay) * (Cx-Ax)
    }

    /**
     * a point has been touched
     */
    fun touchPoint(row: Int, column: Int) {
        // if there is a winner, no need to do anything else
        if(hasRedWon || hasBlackWon) {
            return
        }
        // if the touched row and column are within the game board's bounds (0-23)
        // and if the peg isn't already added, add a peg
        if(row in 0 .. 23 && column in 0 .. 23 &&
            !boardArray[row][column].isConfirmed ) {
            addPeg(row, column)
        }
    }

    /**
     * places board state into a list of strings to be printed to a file
     */
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

    /**
     * places visible, confirmed nodes in the board array to a string list for printing to a file
     */
    private fun formatBoardArrayForFile() : List<String> {
        var toReturn = mutableListOf<String>()
        for(row in boardArray.indices) {
            for (node in boardArray[row]) {
                if(node.isConfirmed) {
                    toReturn.add(
                        "" + node.column + " " + node.row + " "
                                + node.color + " " + node.isRed
                    )
                }
            }
        }
        return toReturn
    }

    /**
     * places walls of a given set into a string list for printing to a file
     */
    private fun formatWallsForFile(walls: HashMap<Wall, Int>) : List<String> {
        var toReturn = mutableListOf<String>()
        walls.forEach() {
            toReturn.add("" + it.key.row1 + " " + it.key.row2
                    + " " + it.key.column1 + " " + it.key.column2
                    + " " + it.value )
        }
        return toReturn
    }

    /**
     * loads a given board data as a string array into the data of the file
     */
    fun loadBoardData(boardData: List<String>) {
        // reset the board in case there is something there
        reset()
        // load the boolean data saved
        isRedTurn = boardData[0].toBoolean()
        hasChosenValidOption = boardData[1].toBoolean()
        // load the nodes, hard set the start index because this does not change
        Log.v("rootbeer", "loading nodes")
        var startIndex = 3
        var endIndex = startIndex +  boardData[2].toInt()
        if(startIndex != endIndex) {
            for (i in startIndex..endIndex) {
                if(i == endIndex) {
                    break
                }
                updateNode(boardData[i].split(" "))
            }
            startIndex = endIndex + 1
            endIndex = startIndex + boardData[startIndex - 1].toInt()
        }
        else {
            startIndex = endIndex + 1
            endIndex = startIndex + boardData[startIndex - 1].toInt()
        }
        // load the red walls, start and end index are flexible
        Log.v("rootbeer", "loading red walls")
        if(startIndex != endIndex) {
            for (i in startIndex..endIndex) {
                if(i == endIndex) {
                    break
                }
                addWall(boardData[i].split(" "), redWalls)
            }
            startIndex = endIndex + 1
            endIndex = startIndex + boardData[startIndex - 1].toInt()
        }
        else {
            startIndex = endIndex + 1
            endIndex = startIndex + boardData[startIndex - 1].toInt()
        }
        // load the black walls, start and end index are flexible
        Log.v("rootbeer", "loading black walls")
        if(startIndex != endIndex) {
            for (i in startIndex..endIndex) {
                if(i == endIndex) {
                    break
                }
                addWall(boardData[i].split(" "), blackWalls)
            }
        }
    }

    /**
     * updates a given node's information in the board array
     */
    private fun updateNode(nodeData: List<String>) {
        val column = nodeData[0].toInt()
        val row = nodeData[1].toInt()
        boardArray[row][column].isShown = true
        boardArray[row][column].color = nodeData[2].toInt()
        boardArray[row][column].isRed = nodeData[3].toBoolean()
        boardArray[row][column].isBlack = !boardArray[row][column].isRed
        boardArray[row][column].isConfirmed = true
        boardArray[row][column].row = row
        boardArray[row][column].column = column
        Log.v("rootbeer", "End update Node")
    }

    /**
     * adds a given wall information into the given wall set
     */
    private fun addWall(wallData: List<String>, walls: HashMap<Wall, Int>) {
        for(d in wallData) {
        }
        val row1 = wallData[0].toInt()
        val row2 = wallData[1].toInt()
        val column1 = wallData[2].toInt()
        val column2 = wallData[3].toInt()
        val wall = Wall(row1, row2, column1, column2)
        walls[wall] = wallData[4].toInt()
        boardArray[row1][column1].addCon(boardArray[row2][column2])
        boardArray[row2][column2].addCon(boardArray[row1][column1])
    }
}