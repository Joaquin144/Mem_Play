package com.vibhu.nitjsr.memplay.models

data class MemoryCard(
    val identifier: Int,//we need unique id for each memory card and we know resource ids are uniquley generated so we use those.
    var isFaceUp: Boolean = false,
    var isMatched: Boolean = false

)