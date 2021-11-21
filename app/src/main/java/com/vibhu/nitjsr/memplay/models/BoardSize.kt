package com.vibhu.nitjsr.memplay.models

enum class BoardSize(val numCards: Int) {
    EASY(8),
    MEDIUM(18),
    HARD(24);

    fun getWidth(): Int{
        return when (this) {//when is equivaltne to switch
            EASY -> 2
            MEDIUM -> 3
            HARD -> 4
        }
    }
    fun getHeight(): Int{
        return numCards/getWidth()
    }
    fun getNumPairs(): Int {
        return numCards/2
    }
}