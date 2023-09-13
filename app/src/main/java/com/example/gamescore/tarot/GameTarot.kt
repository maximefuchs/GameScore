package com.example.gamescore.tarot

import android.content.Context
import android.content.SharedPreferences
import com.example.gamescore.Game
import com.example.gamescore.TypeGameSaved

open class GameTarot : Game {
    constructor()
    constructor(game_id: Int, player_take: Int, contract: String, success: Boolean, difference : Int, bonus: Int, score: List<Int>) {
        this.gameId = game_id
        this.nbPlayers = 4
        this.playerTake = player_take
        this.contract = contract
        this.difference = difference
        this.success = success
        this.bonus = bonus
        this.score = score
    }

    var playerTake: Int = 0
    var contract : String = "Garde"
    var success : Boolean = true
    var difference: Int = 0
    var bonus: Int = -1

    override fun toString(): String {
        return super.toString() + "| Tarot | nb_players=$nbPlayers, player_take=$playerTake, contract='$contract', score=$score "
    }

//    fun getGameFromSharedPreferences(context: Context): Game? {
//        val sharedPreferences: SharedPreferences = context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE)
//        val gameData = sharedPreferences.getString("Game$gameId", null)
//        val gameId = sharedPreferences.getInt("id",-1)
//
//        if (gameId != -1) {
//
//            val numberOfPlayers = sharedPreferences.getInt("numberOfPlayers",4)
//            val parts = gameData.split("|")
//            val playerScores = parts[0].split(",").map { it.toInt() }
//            val numberOfPlayers = parts[1].toInt()
//
//            val game = Game(gameId, numberOfPlayers)
//            game.playerScores.addAll(playerScores)
//            return game
//        }
//
//        return null
//    }

}