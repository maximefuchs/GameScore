package com.example.gamescore.belote

import android.os.Bundle
import com.example.gamescore.GameActivity
import com.example.gamescore.R
import com.example.gamescore.tarot.GameTarot
import kotlinx.android.synthetic.main.activity_game.*
import java.util.ArrayList

enum class TypeGame {
    NORMALE,
    COINCHEE
}

class BeloteActivity : GameActivity() {

    var gameType = TypeGame.NORMALE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        FrameTitle.text = resources.getText(R.string.game_belote)

        supportFragmentManager.beginTransaction().replace(
            R.id.container,
            TypeBeloteFragment()
        ).commit()
    }

    fun getName(game_of_type: TypeGame) {
        gameType = game_of_type
        supportFragmentManager.beginTransaction().replace(
            R.id.container,
            NameFragmentBelote()
        ).commit()
    }

    fun startGame(list_names: List<String>){
        names = list_names
        val f = ScoreBeloteFragment()
        listGames = ArrayList<GameTarot>()
        f.listPlayers = names
        f.listGames = listGames
        supportFragmentManager.beginTransaction().replace(R.id.container, f).commit()
    }
}