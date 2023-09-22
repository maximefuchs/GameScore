package com.example.gamescore.tarot

import android.content.Context
import android.content.SharedPreferences
import com.example.gamescore.Game
import com.example.gamescore.TypeGameSaved
import java.util.HashMap

open class GameTarot : Game {
    constructor()
    constructor(
        game_id: Int,
        player_take: Int,
        contract: String,
        success: Boolean,
        difference: Int,
        bonus_name: IntArray, bonus_value: IntArray
    ) {
        this.gameId = game_id
        this.nbPlayers = 4
        this.playerTake = player_take
        this.contract = contract
        this.difference = difference
        this.success = success
        this.bonusName = bonus_name
        this.bonusValue = bonus_value
    }

    val contracts: HashMap<String, Int> = hashMapOf(
        "Petite" to 10,
        "Pousse" to 20,
        "Garde" to 40,
        "Garde Sans" to 80,
        "Garde Contre" to 160
    )


    var playerTake: Int = 0
    var contract: String = "Garde"
    var success: Boolean = true
    var difference: Int = 0
    var bonusName: IntArray = intArrayOf()
    var bonusValue: IntArray = intArrayOf()
    override var score: IntArray = intArrayOf()

    override fun toString(): String {
        return super.toString() + "| Tarot | nb_players=$nbPlayers, player_take=$playerTake, contract='$contract', score=$score "
    }

    open fun updateScore(previousScore: IntArray) {
        val contractValue = contracts[contract]!!
        var toAdd = contractValue + difference
        if (!success) toAdd = -toAdd
        score = previousScore

        for (index_player in 0..3) {
            if (index_player == playerTake) score[index_player] =
                score[index_player] + 3 * toAdd
            else score[index_player] = score[index_player] - toAdd

            for (index_bonus in bonusName.indices) {
                val nameBonus = bonusName[index_bonus]
                val valueBonus = bonusValue[index_bonus]
                if (nameBonus == index_player)
                    score[index_player] = score[index_player] + valueBonus * 3
                else
                    score[index_player] = score[index_player] - valueBonus
            }
        }
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