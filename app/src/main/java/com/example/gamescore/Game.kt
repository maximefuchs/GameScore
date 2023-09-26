package com.example.gamescore

import android.content.SharedPreferences
import java.io.Serializable

open class Game() : Serializable {

    var gameId: Int = 0
    var nbPlayers: Int = 4
    open var score: IntArray = intArrayOf()
    var restart : Boolean = false // when we use a saved score

    override fun toString(): String {
        return "|| Game || game_id=$gameId, nb_players=$nbPlayers, score=$score "
    }

    fun saveGameToSharedPreferences(editor: SharedPreferences.Editor, typeGame : TypeGameSaved) {
        editor.putString("type", typeGame.name)
        editor.putInt("numberOfPlayers", nbPlayers)
        score.forEachIndexed { index, value ->
            editor.putInt("playerScore_$index", value)
        }
        editor.apply()
    }

}