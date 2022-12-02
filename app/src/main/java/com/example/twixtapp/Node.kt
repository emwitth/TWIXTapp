package com.example.twixtapp

import android.graphics.Color
import android.util.Log

class Node {
    var isShown: Boolean = false
    var isConfirmed: Boolean = false
    var isRed: Boolean = false
    var isBlack: Boolean = false
    var color: Int = Color.CYAN
    var cons = mutableSetOf<Node>()
    var row = -1
    var column = -1

    fun hasRightColor(isRedTurn: Boolean): Boolean {
//        Log.v("rootbeer", "$isRed, $isRedTurn")
        return (isRed && isRedTurn) || (isBlack && !isRedTurn)
    }

    fun addCon(n: Node) {
        if(n !in cons) {
            cons.add(n)
        }
    }
}
