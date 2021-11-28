package com.vibhu.nitjsr.memplay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.vibhu.nitjsr.memplay.models.BoardSize
import com.vibhu.nitjsr.memplay.utils.EXTRA_BOARD_SIZE

class CreateActivity : AppCompatActivity() {
    private lateinit var boardSize: BoardSize
    private var numImagesRequired = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)//Built in Back Button on Action Bar
        boardSize = intent.getSerializableExtra(EXTRA_BOARD_SIZE) as BoardSize //yahan as lagakar hum casting kar rahe hain BoardSize type object mein
        numImagesRequired = boardSize.getNumPairs()
        supportActionBar?.title = "Choose pics (0/$numImagesRequired)"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}