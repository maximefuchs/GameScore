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
        bonus_name: IntArray,
        bonus_value: IntArray
    ) : super(
        game_id,
        player_take,
        contract,
        success,
        difference,
        bonus_name, bonus_value
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
        score = previousScore

        for (index_player in 0..4) {
            if (teammate == playerTake) {
                if (index_player == playerTake) score[index_player] = score[index_player] + 4 * toAdd
                else score[index_player] = score[index_player] - toAdd
            } else {
                when (index_player) {
                    playerTake -> score[index_player] = score[index_player] + 2 * toAdd
                    teammate -> score[index_player] = score[index_player] + toAdd
                    else -> score[index_player] = score[index_player] - toAdd
                }
            }

            for (index_bonus in bonusName.indices) {
                val nameBonus = bonusName[index_bonus]
                val valueBonus = bonusValue[index_bonus]
                if (nameBonus == index_player)
                    score[index_player] = score[index_player] + valueBonus * 4
                else
                    score[index_player] = score[index_player] - valueBonus
            }
        }
    }
}