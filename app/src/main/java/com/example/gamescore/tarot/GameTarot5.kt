package com.example.gamescore.tarot

class GameTarot5 : GameTarot {
    constructor()
    constructor(
        game_id: Int,
        player_take: Int,
        contract: String,
        teammate: Int,
        success: Boolean,
        difference: Int,
        bonus_players_id: IntArray, bonus_string_names: Array<String>
    ) : super(
        game_id,
        player_take,
        contract,
        success,
        difference,
        bonus_players_id, bonus_string_names
    ) {
        this.nbPlayers = 5
        this.teammate = teammate
    }

    var teammate: Int = 1

    override fun toString(): String {
        return super.toString() + "| Tarot à 5 | teammate=$teammate"
    }

    override fun updateScore(previousScore: IntArray) {
        val contractValue = contracts[contract]!!
        var toAdd = contractValue + difference
        if (!success) toAdd = -toAdd
        score = previousScore.clone()

        for (index_player in 0..4) {
            if (teammate == playerTake) {
                if (index_player == playerTake) score[index_player] =
                    score[index_player] + 4 * toAdd
                else score[index_player] = score[index_player] - toAdd
            } else {
                when (index_player) {
                    playerTake -> score[index_player] = score[index_player] + 2 * toAdd
                    teammate -> score[index_player] = score[index_player] + toAdd
                    else -> score[index_player] = score[index_player] - toAdd
                }
            }

            for (index_bonus in bonusPlayersId.indices) {
                val bonusPlayerId = bonusPlayersId[index_bonus]
                val bonusStringName = bonusStringNames[index_bonus]
                val valueBonus = bonuses[bonusStringName]!!
                if (bonusPlayerId == index_player)
                    score[index_player] = score[index_player] + valueBonus * 4
                else
                    score[index_player] = score[index_player] - valueBonus
            }
        }
    }
}