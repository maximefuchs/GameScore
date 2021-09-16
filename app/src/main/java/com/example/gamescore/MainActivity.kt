package com.example.gamescore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gamescore.belote.BeloteActivity
import com.example.gamescore.tarot.TarotActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameTarot.setOnClickListener {
            val i = Intent(this, TarotActivity::class.java)
            startActivity(i)
        }

        gameBelote.setOnClickListener {
            val i = Intent(this, BeloteActivity::class.java)
            startActivity(i)
        }




    }
}
