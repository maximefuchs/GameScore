package com.example.gamescore.tarot

import com.example.gamescore.Game
import java.util.HashMap

open class GameTarot : Game {
    constructor()
    constructor(
        game_id: Int,
        player_take: Int,
        contract: String,
        success: Boolean,
        difference: Int,
        bonus_players_id: IntArray, bonus_string_names: Array<String>
    ) {
        this.gameId = game_id
        this.nbPlayers = 4
        this.playerTake = player_take
        this.contract = contract
        this.difference = difference
        this.success = success
        this.bonusPlayersId = bonus_players_id
        this.bonusStringNames = bonus_string_names
    }

    val contracts: HashMap<String, Int> = hashMapOf(
        "Petite" to 10,
        "Pousse" to 20,
        "Garde" to 40,
        "Garde Sans" to 80,
        "Garde Contre" to 160
    )

    var bonuses: HashMap<String, Int> = hashMapOf(
        "Petit au Bout" to 10,
        "Poignée" to 10,
        "Double Poignée" to 20,
        "Triple Poignée" to 30,
        "Misère" to 10,
        "Double Misère" to 20
    )


    var playerTake: Int = 0
    var contract: String = "Garde"
    var success: Boolean = true
    var difference: Int = 0
    var bonusPlayersId: IntArray = intArrayOf()
    var bonusStringNames: Array<String> = arrayOf<String>()
    override var score: IntArray = intArrayOf()

    override fun toString(): String {
        return super.toString() + "| Tarot | nb_players=$nbPlayers, player_take=$playerTake, contract='$contract', score=$score "
    }

    open fun updateScore(previousScore: IntArray) {
        val contractValue = contracts[contract]!!
        var toAdd = contractValue + difference
        if (!success) toAdd = -toAdd
        score = previousScore.clone()

        for (index_player in 0..3) {
            if (index_player == playerTake) score[index_player] =
                score[index_player] + 3 * toAdd
            else score[index_player] = score[index_player] - toAdd

            for (index_bonus in bonusPlayersId.indices) {
                val bonusPlayerId = bonusPlayersId[index_bonus]
                val bonusStringName = bonusStringNames[index_bonus]
                val valueBonus = bonuses[bonusStringName]!!
                if (bonusPlayerId == index_player)
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