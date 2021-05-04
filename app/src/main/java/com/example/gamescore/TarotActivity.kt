package com.example.gamescore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_tarot.*
import kotlinx.android.synthetic.main.fragment_score.*

class TarotActivity : AppCompatActivity() {

    var NbPlayers : Int = 0
    var names : List<String> = listOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarot)

        supportFragmentManager.beginTransaction().replace(R.id.container,NbPlayersFragment()).commit()
    }

    fun getName(nb_players: Int){
        NbPlayers = nb_players
        supportFragmentManager.beginTransaction().replace(R.id.container,NameFragment()).commit()
    }

    fun startGame(list_names : List<String>){
        names = list_names
        supportFragmentManager.beginTransaction().replace(R.id.container,ScoreFragment()).commit()
    }

}
