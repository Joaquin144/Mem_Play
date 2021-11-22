package com.vibhu.nitjsr.memplay

import android.animation.ArgbEvaluator
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
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

        clRoot= findViewById(R.id.clRoot)
        rvBoard = findViewById(R.id.rvBoard)
        tvNumMoves = findViewById(R.id.tvNumMoves)
        tvNumPairs = findViewById(R.id.tvNumPairs)

        setUpBoard()
    }

    private fun updateGameWithFlip(position: Int) {
        //Error Handling
        if(memoryGame.haveWonGame()){
            //Alert the user of invalid move --> Use Snackbar
            Snackbar.make(clRoot,"You already won!",Snackbar.LENGTH_LONG).show()
            return
        }
        if(memoryGame.isCardFaceUp(position)){
            Snackbar.make(clRoot,"Invalid Move",Snackbar.LENGTH_SHORT).show()
            return
        }
        if(memoryGame.flipCard(position)) {//card ka state badal do
            Log.i(TAG, "Found a Match! Num Pairs found: ${memoryGame.numPairsFound}")
            val color = ArgbEvaluator().evaluate(
                memoryGame.numPairsFound.toFloat() / boardSize.getNumPairs(),
                ContextCompat.getColor(this,R.color.color_progress_none),
                ContextCompat.getColor(this,R.color.color_progress_full)
            ) as Int//interpolation lagana
            tvNumPairs.setTextColor(color)
            tvNumPairs.text = "Pairs: ${memoryGame.numPairsFound} / ${boardSize.getNumPairs()}"
            if(memoryGame.haveWonGame())
                Snackbar.make(clRoot,"You won! Congrats. Hehehe😁",Snackbar.LENGTH_LONG).show()
        }
        tvNumMoves.text = "Moves: ${memoryGame.getNumMoves()}"
        adapter.notifyDataSetChanged()//Neend mein soye hue adapter ko batao ki bhaiya badal do RV ko
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mi_refresh -> {
                //set up the game again
                if(memoryGame.getNumMoves() > 0 && !memoryGame.haveWonGame()){
                    showAlertDialog("Quit your current game?",null,View.OnClickListener { setUpBoard() })
                }
                else
                    setUpBoard()
            }
            R.id.mi_new_size ->{
                showNewSizeDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showNewSizeDialog() {
        val boardSizeView:View = LayoutInflater.from(this).inflate(R.layout.dialog_board_size,null)
        val radioGroupSize:RadioGroup = boardSizeView.findViewById<RadioGroup>(R.id.radioGroup)
        when(boardSize){
            BoardSize.EASY -> radioGroupSize.check(R.id.rbEasy)
            BoardSize.MEDIUM -> radioGroupSize.check(R.id.rbMedium)
            BoardSize.HARD -> radioGroupSize.check(R.id.rbHard)
        }
        showAlertDialog("Choose new Size",boardSizeView,View.OnClickListener {
            //Seat a new value for boardsize
            boardSize = when(radioGroupSize.checkedRadioButtonId){
                R.id.rbEasy -> BoardSize.EASY
                R.id.rbMedium -> BoardSize.MEDIUM
                else -> BoardSize.HARD
            }
            setUpBoard()
        })
    }

    private fun showAlertDialog(title:String , view:View?,positiveClickListener:View.OnClickListener) {
        AlertDialog.Builder(this).setTitle(title)
            .setView(view)
            .setNegativeButton("Cancel",null)
            .setPositiveButton("Haan bhai"){_,_->
                positiveClickListener.onClick(null)
            }.show()
    }

    private fun setUpBoard(){
        when(boardSize){
            BoardSize.EASY -> {
                tvNumMoves.text = "Easy: 4X2"
                tvNumPairs.text = "Pairs:0/4"
            }
            BoardSize.MEDIUM -> {
                tvNumMoves.text = "MEDIUM: 6X3"
                tvNumPairs.text = "Pairs:0/9"
            }
            BoardSize.HARD -> {
                tvNumMoves.text = "HARD: 6X4"
                tvNumPairs.text = "Pairs:0/12"
            }
        }
        tvNumPairs.setTextColor(ContextCompat.getColor(this,R.color.color_progress_none))
        memoryGame = MemoryGame(boardSize)
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
}