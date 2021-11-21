package com.vibhu.nitjsr.memplay

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.vibhu.nitjsr.memplay.models.BoardSize
import com.vibhu.nitjsr.memplay.models.MemoryCard
import kotlin.math.min

//          $&$  NOTES  ----------------------
//1.) MemoryBoardApater class is sub class of RecyclerView.Adapter class
//2.) ViewHolder object represents single item and its properties in RV
//3.) RV class is an abstract class so we need to overwrite certain methods
class MemoryBoardAdapter(
    private val context: Context,
    private val boardSize: BoardSize,
    private val cards: List<MemoryCard>,
    private val cardClickListener: CardClickListener
) : RecyclerView.Adapter<MemoryBoardAdapter.ViewHolder>() {

    //6.) Companion object are singletons where we define our constants --> equivalent of static variables in JAVA
    companion object {
        private const val MARGIN_SIZE = 10
        private const val TAG = "MemeryBoardAdapter"
    }
    interface CardClickListener{
        fun onCardClicked(position: Int)
    }
    //4.) How to create one view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //5.) Jo yahan kiya hai woh bhot important hai. Hum dynamically height and width set kar rahe hain cardView ka jisse screen par sabhi cardViews fit aye jisse screen space na toh kam pade na hi zyada pade(<--> scroll ho)
        val cardWidth: Int = parent.width/boardSize.getWidth() -(2* MARGIN_SIZE);
        val cardHeight: Int = parent.height/boardSize.getHeight() -(2* MARGIN_SIZE);
        val cardSideLength: Int = min(cardWidth,cardHeight)
        val view:View = LayoutInflater.from(context).inflate(R.layout.memory_card,parent,false)
        val layoutParams:ViewGroup.MarginLayoutParams =  view.findViewById<CardView>(R.id.cardView).layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.width = cardSideLength
        layoutParams.height = cardSideLength
        layoutParams.setMargins(MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE)
        return ViewHolder(view)
    }

    //5.) Takes the data presetn at given position and binds it to RecyclerView.ViewHolder class
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = boardSize.numCards

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageButton = itemView.findViewById<ImageButton>(R.id.imageButton)
        fun bind(position: Int) {
            val memoryCard: MemoryCard = cards[position]
            imageButton.setImageResource(if (memoryCard.isFaceUp) memoryCard.identifier else R.drawable.ic_launcher_background)
            imageButton.alpha = if(memoryCard.isMatched) .4f else 1.0f
            //I am not able to find the reason why next 2 lines crash my app on successfully matching 2 cards up
//            val colorStateList:ColorStateList? = if(memoryCard.isMatched) ContextCompat.getColorStateList(context,R.color.color_gray) else null
           // ViewCompat.setBackgroundTintList(imageButton,colorStateList)//shading laga dega imageButton par
            imageButton.setOnClickListener {
                Log.i(TAG,"Clicked on postion $position")
                cardClickListener.onCardClicked(position)
            }
        }
    }


}
