package com.vibhu.nitjsr.memplay.models

import com.vibhu.nitjsr.memplay.utils.DEFAULT_ICONS

class MemoryGame(private val boardSize: BoardSize){
    //har kaam MainActivity se nahi karwake mai is class se karwayuhnga
    val cards: List<MemoryCard>
    private var indexOfSingleSelectedCard:Int? = null //jab game chalu hoga tab yeh null rahega

    var numPairsFound = 0
    init{//initialize the game
        val chosenImages:List<Int> = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
        val randomizedImages =  (chosenImages + chosenImages).shuffled()
        cards = randomizedImages.map { MemoryCard(it) }
    }
    fun flipCard(position: Int):Boolean {
        val card:MemoryCard = cards[position]
        /*2 cases possible hain:-
        0 cards are flipped  --> If so then restore cards & flip this selected card
        1 card is flipped    --> If so then flip over selected card and check whether its same as earlier one or not
         */
        var foundMatch = false
        if(indexOfSingleSelectedCard == null){
            //0 ya 2 cards were flipped over
            restoreCards()
            indexOfSingleSelectedCard = position
        }
        else{
            //1 card tha pehle se
            foundMatch = checkForMatch(indexOfSingleSelectedCard!!,position)//jo card pehle se selected tha aur jo ab click kiya gaya unka match karayo
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp = !card.isFaceUp
        return foundMatch
    }

    private fun checkForMatch(position1: Int, position2: Int): Boolean {
        if(cards[position1].identifier!=cards[position2].identifier) {
            return false
        }
        cards[position1].isMatched=true;
        cards[position2].isMatched=true;
        numPairsFound++;
        return true
    }

    private fun restoreCards() {
        for(card:MemoryCard in cards){
            if(!card.isMatched)
                card.isFaceUp = false;
        }
    }

    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs()
    }

    fun isCardFaceUp(position: Int): Boolean {
        return cards[position].isFaceUp
    }
}
