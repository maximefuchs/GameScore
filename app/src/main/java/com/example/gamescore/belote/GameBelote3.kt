package com.example.gamescore.belote

import kotlin.math.abs

class GameBelote3 : GameBelote {
    constructor()

    constructor(
        game_id: Int,
        taker: Int,
        success: Boolean,
        difference: Int,
        bonus1: Int,
        bonus2: Int
    ) : super(
        game_id,
        taker,
        success,
        difference,
        bonus1,
        bonus2
    ) {
        this.nbPlayers = 3
    }

    override fun toString(): String {
        return super.toString() + "| Belote à 3 | "
    }

    override fun updateScore(previousScore: IntArray) {
        var totalPoints = if (abs(difference) == 250) 250 else 162
        val scoreWritten = if (abs(difference) == 250) 250 else (162 + difference) / 2
        score = previousScore.clone()
        if (!success) totalPoints /= 2
        for (playerId in 0..2) {
            if (success)
                score[playerId] =
                    score[playerId] + (if (takerId == playerId) scoreWritten + bonusTeam1 else totalPoints - scoreWritten + bonusTeam2)
            else
                score[playerId] =
                    score[playerId] + (if (takerId == playerId) bonusTeam1 else totalPoints + bonusTeam2)
        }
    }

}