package com.example.catandiceroller

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView

class MainActivity : AppCompatActivity() {
    // state machine for dice roller
    /* >> types <<
     * cards
     *      generates a "card" per possible roll and cycles through them
     * random cards
     *      ibid, but changes which numbers have how many cards (a 2 can have chances of a 6, etc.)
     * random changing cards
     *      re-randomizes every shuffle (36 cards)
     * random uneven cards
     *      ibid, but the statistics will be random, not 6-5-4-3-2-1 like normal
     *
     */
    private val states = arrayOf("cards", "random uneven cards")
    private var diceType = "random uneven cards"
    
    
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        var cards = newCards() as ArrayList<*>

        // draw button click
        val drawButton = findViewById<Button>(R.id.draw)
        drawButton.setOnClickListener {
            // shuffle the deck when empty
            if (cards.size == 0) {
                cards = newCards() as ArrayList<*>
                Toast.makeText(this, "shuffling...", Toast.LENGTH_SHORT).show()
            }

            // draw a card
            val dice = cards.removeAt(0)
            displayRoll(dice)
        }
        
        
        
        // "A" button click (rotate through states
        val aButton = findViewById<Button>(R.id.button)
        aButton.setOnClickListener {
            val dex = states.indexOf(diceType)
            diceType = states[dex+1 % states.size]
            
            cards = newCards() as ArrayList<*>
        }
    }
    

    // changes the dice images to match input
    @SuppressLint("WrongViewCast")
    fun displayRoll(dice: Any?) {
        // pair of actual dice rolls
        if (dice is Pair<*, *>) {
            var die1Drawable = 0
            var die2Drawable = 0
    
            when (dice.first) {
                1 -> die1Drawable = R.drawable.dice_six_faces_one
                2 -> die1Drawable = R.drawable.dice_six_faces_two
                3 -> die1Drawable = R.drawable.dice_six_faces_three
                4 -> die1Drawable = R.drawable.dice_six_faces_four
                5 -> die1Drawable = R.drawable.dice_six_faces_five
                6 -> die1Drawable = R.drawable.dice_six_faces_six
                else -> die1Drawable = R.drawable.liar
            }
    
            when (dice.second) {
                1 -> die2Drawable = R.drawable.inverted_dice_1
                2 -> die2Drawable = R.drawable.inverted_dice_2
                3 -> die2Drawable = R.drawable.inverted_dice_3
                4 -> die2Drawable = R.drawable.inverted_dice_4
                5 -> die2Drawable = R.drawable.inverted_dice_5
                6 -> die2Drawable = R.drawable.inverted_dice_6
                else -> die2Drawable = R.drawable.liar
            }
    
            val die1View: AppCompatImageView = findViewById(R.id.imageView2)
            die1View.setImageResource(die1Drawable)
    
            val die2View: AppCompatImageView = findViewById(R.id.imageView3)
            die2View.setImageResource(die2Drawable)
            
        // randomly picks the die faces to match the dice int
        } else if (dice is Int) {
            val max = if (dice <= 6) dice-1 else 6
            val min = if (dice > 6) dice - 6 else 1
            val die1 = (min..max).random()
            displayRoll(Pair(die1, dice - die1))
            Toast.makeText(this,
                "$dice - $die1 and ${dice-die1}",
                Toast.LENGTH_SHORT).show()
        }
    }
    
    // returns a fresh deck of cards to draw from
    private fun newCards(): Any? {
        when (diceType) {
            // return one of each possible dice roll in the deck
            "cards" -> {
                val newCards = ArrayList<Pair<Int, Int>>()
                for (i in 1..6) {
                    for (j in 1..6) {
                        newCards.add(Pair(i, j))
                    }
                }
                
                return newCards.shuffled().toMutableList()
            }
            
            // changes which rolls are likely vs unlikely
            "random cards" -> {
                val newCards = ArrayList<Int>()
                val dieRolls = (2..12).shuffled().toMutableList()
                val chances: IntArray = intArrayOf(1, 2, 3, 4, 5, 6, 5, 4, 3, 2, 1)
                
                for (i in chances) {
                    val die = dieRolls.removeAt(0)
                    for (j in 1..i) newCards.add(die)
                }
                
                return newCards.shuffled().toMutableList()
            }
            
            // funny random goofs to make it crazy. still 36 cards
            "random uneven cards" -> {
                val newCards = ArrayList<Int>()
                for (i in 2..12) newCards.add(i)
                for (i in 1..25) newCards.add(newCards.random())
                return newCards.shuffled().toMutableList()
            }
        }
        
        return null
    }
}
