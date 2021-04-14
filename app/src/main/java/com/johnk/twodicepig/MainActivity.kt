package com.johnk.twodicepig

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class MainActivity : AppCompatActivity() {
    private var rollDice: Button? = null
    private var hold: Button? = null
    private var imageView1: ImageView? = null //image view for left dice
    private var imageView2: ImageView? = null //image view for right dice
    private var total:Int = 0 //keeps track of turn total
    private var player1score = 0
    private var player2score = 0
    private var whoseTurn = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rollDice = findViewById<View>(R.id.rollDice) as Button //grabs the roll dice button
        hold = findViewById<View>(R.id.hold) as Button //grabs the hold button
        imageView1 = findViewById<View>(R.id.imageView1) as ImageView //grabs the left dice image view
        imageView2 = findViewById<View>(R.id.imageView2) as ImageView //grabs the right dice image view
        val turnTotal:TextView = findViewById(R.id.turnTotal) as TextView //grabs turn total text view
        val player1Total:TextView = findViewById(R.id.player1Score) as TextView //grabs player 1 total text view
        val player2Total:TextView = findViewById(R.id.player2Score) as TextView //grabs player 2 total text view
        val playerTurn:TextView = findViewById(R.id.playerTurn) as TextView //grabs player turn text view

        rollDice!!.setOnClickListener { //when the player clicks the roll dice button
            val value1 = randomDiceValue() //gets a random value from 1-6 for left dice
            val value2 = randomDiceValue() //gets a random value from 1-6 for right dice
            val res1 =
                resources.getIdentifier("dice_$value1", "drawable", "com.johnk.twodicepig") //displays dice image corresponding to random value for left dice
            val res2 =
                resources.getIdentifier("dice_$value2", "drawable", "com.johnk.twodicepig") //displays dice image corresponding to random value for right dice
            imageView1!!.setImageResource(res1)
            imageView2!!.setImageResource(res2)


            if((value1==1 || value2==1) && (value1 != value2)){ //if one of the dice is a one
                total = 0
                turnTotal.setText("Turn Total: $total") //sets turn total to 0 and displays it in the textview
                hold!!.isEnabled=true //ensures that the hold button is still clickable
                switchTurn(whoseTurn) //switches to the other player
                if(whoseTurn==true)
                {
                    whoseTurn=false //if was player 1, now player 2, update corresponding bool
                }
                else
                {
                    whoseTurn=true //if was player 2, now player 1, update corresponding bool
                }
            }
            else if(value1==1 && value2==1){ //if snake eyes
                if(whoseTurn==true) //if player 1
                {
                    player1score = 0
                    player1Total.setText("Player 1 Total: $player1score") //resets player score and displays it corresponding text view
                    switchTurn(whoseTurn)
                    whoseTurn=false //switches player turn and updates bool
                }
                else //if player 2
                {
                    player2score = 0
                    player2Total.setText("Player 2 Total: $player2score") //resets player score and displays it corresponding text view
                    switchTurn(whoseTurn)
                    whoseTurn=true //switches player turn and updates bool
                }
                hold!!.isEnabled=true //ensures that the hold button is still clickable
            }
            else if(value1==value2 ) { //if roll a double
                total = total + value1 + value2;
                turnTotal.setText("Turn Total: $total") //update turn total and display in text view
                hold!!.isEnabled=false; //disable hold button
            }
            else{ //roll anything else
                total = total + value1 + value2;
                turnTotal.setText("Turn Total: $total") //update turn total and display in text view
                hold!!.isEnabled=true; //ensures that the hold button is still clickable
            }
        }
        hold!!.setOnClickListener{ //if user clicks the hold button

            if(whoseTurn==true) //hold for player 1
            {
                player1score=player1score+total
                total = 0 //reset turn total
                player1Total.setText("Player 1 Total: $player1score") //update player score and display it in text view
                checkWin()
                switchTurn(whoseTurn)
                whoseTurn=false //switches turn and updates corresponding bool

            }
            else if(whoseTurn==false) //hold for player 2
            {
                player2score=player2score+total
                total = 0 //reset turn total
                player2Total.setText("Player 2 Total: $player2score") //update player score and display it in text view
                checkWin()
                switchTurn(whoseTurn)
                whoseTurn=true //switches turn and updates corresponding bool
            }

        }
    }

    companion object { //will generate a value from 1-6
        val RANDOM = Random()
        fun randomDiceValue(): Int {
            return RANDOM.nextInt(6) + 1
        }
    }

    fun switchTurn(turn:Boolean) { //switches turn and resets the corresponding values
        val playerTurn:TextView = findViewById(R.id.playerTurn) as TextView
        val turnTotal:TextView = findViewById(R.id.turnTotal) as TextView //since they can't be grabbed due to scope issues, must be restated
        if(turn==true) //player 1
        {
            total=0
            turnTotal.setText("Turn Total: $total ") //resets turn total and displays it text view
            playerTurn.setText("Player Turn: P2") //displays player 2's turn in text view
        }
        if(turn==false) //player 2
        {
            total=0
            turnTotal.setText("Turn Total: $total ") //resets turn total and displays it text view
            playerTurn.setText("Player Turn: P1") //displays player 1's turn in text view
        }
    }
    fun checkWin(){ //checks win condition and resets the board if met
        val playerTurn:TextView = findViewById(R.id.playerTurn) as TextView
        val turnTotal:TextView = findViewById(R.id.turnTotal) as TextView
        val player1Total:TextView = findViewById(R.id.player1Score) as TextView
        val player2Total:TextView = findViewById(R.id.player2Score) as TextView //since they can't be grabbed due to scope issues, must be restated

        if(player1score>=50)
        {
            Toast.makeText(applicationContext, "Player 1 Wins", Toast.LENGTH_SHORT).show() //displays toast message to indicate winner
            total=0
            player1score=0
            player2score=0
            turnTotal.setText("Turn Total: $total ")
            player1Total.setText("Player 1 Total: $player1score ")
            player2Total.setText("Player 2 Total: $player2score ")
            playerTurn.setText("Player Turn: P1") //resets all scores and corresponding text views
        }
        else if(player2score>=50)
        {
            Toast.makeText(applicationContext, "Player 2 Wins", Toast.LENGTH_SHORT).show() //displays toast message to indicate winner
            total=0
            player1score=0
            player2score=0
            turnTotal.setText("Turn Total: $total ")
            player1Total.setText("Player 1 Total: $player1score ")
            player2Total.setText("Player 2 Total: $player2score ")
            playerTurn.setText("Player Turn: P1") //resets all scores and corresponding text views
        }
    }
}
