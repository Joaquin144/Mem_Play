package com.vibhu.nitjsr.memplay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.vibhu.nitjsr.memplay.models.BoardSize
import com.vibhu.nitjsr.memplay.models.MemoryCard
import com.vibhu.nitjsr.memplay.models.MemoryGame
import com.vibhu.nitjsr.memplay.utils.DEFAULT_ICONS

class MainActivity : AppCompatActivity() {
    companion object{
        private const val TAG = "MainActivity"
    }

    private lateinit var memoryGame: MemoryGame
    private lateinit var clRoot:ConstraintLayout
    private lateinit var rvBoard : RecyclerView
    private lateinit var tvNumMoves : TextView
    private lateinit var tvNumPairs : TextView
    private lateinit var adapter : MemoryBoardAdapter
    private var boardSize:BoardSize = BoardSize.EASY
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvBoard = findViewById(R.id.rvBoard)
        tvNumMoves = findViewById(R.id.tvNumMoves)
        tvNumPairs = findViewById(R.id.tvNumPairs)
        memoryGame = MemoryGame(boardSize)
        clRoot= findViewById(R.id.clRoot)
        //layout manager is responsible for measuring and positioning of items(here called as itemview) infalted in RV
        rvBoard.layoutManager = GridLayoutManager(this,boardSize.getWidth());
        //adapter ka kaam hai binfding provide karna RV items ke liye
        adapter = MemoryBoardAdapter(this,boardSize,memoryGame.cards,object: MemoryBoardAdapter.CardClickListener{
            override fun onCardClicked(position: Int) {
                updateGameWithFlip(position)
            }

        })
        rvBoard.adapter = adapter
        rvBoard.setHasFixedSize(true) //--> This is performance optimisation (optional statement) -->Official Documentation :: I think jab itemviews mein hum koi change karte hain tab RV poora invalidate ho jata hai kyuki woh by deafult maan kar chalta hai ki uska size depends on its child elements. However generally low profile apps won't require this much sophistication. So they should prefer using setHasFixedSize function so that RV does not inflates all elements again and again when the content changes.



    }

    private fun updateGameWithFlip(position: Int) {
        //Error Handling
        if(memoryGame.haveWonGame()){
            //Alert the user of invalid move --> Use Snackbar
            Snackbar.make(clRoot,"You already won!",Snackbar.LENGTH_LONG).show()
            return
        }
        if(memoryGame.isCardFaceUp(position)){
            Snackbar.make(clRoot,"Invalid Move",Snackbar.LENGTH_LONG).show()
            return
        }
        memoryGame.flipCard(position)//card ka state badal do
        adapter.notifyDataSetChanged()//Neend mein soye hue adapter ko batao ki bhaiya badal do RV ko
    }
}