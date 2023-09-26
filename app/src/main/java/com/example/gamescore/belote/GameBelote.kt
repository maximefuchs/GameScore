package com.example.gamescore.belote

import com.example.gamescore.Game
import kotlin.math.abs

open class GameBelote : Game {
    constructor()

    constructor(game_id: Int, playerTake: Int, success: Boolean, difference : Int, bonus1: Int, bonus2: Int) {
        this.gameId = game_id
        this.nbPlayers = 4
        this.takerId = playerTake
        this.difference = difference
        this.success = success
        this.bonusTeam1 = bonus1
        this.bonusTeam2 = bonus2
    }

    var takerId: Int = 0
    var success : Boolean = true
    var difference: Int = 0
    var bonusTeam1: Int = -1
    var bonusTeam2: Int = -1

    override fun toString(): String {
        return super.toString() + "| Belote | taker $takerId, success $success, difference $difference"
    }

    open fun updateScore(previousScore : IntArray) {
        val totalPoints = if (abs(difference) == 250) 250 else 162
        score = previousScore.clone()
        computeScore(totalPoints)
    }

    fun computeScore(totalPoints: Int) {
        val scoreWritten = if (abs(difference) == 250) 250 else (162 + difference) / 2
        for (teamId in 0..1) {
            val bonus = if (teamId == 0) bonusTeam1 else bonusTeam2
            if (success)
                score[teamId] =
                    score[teamId] + (if (takerId == teamId) scoreWritten + bonus else totalPoints - scoreWritten + bonus)
            else
                score[teamId] =
                    score[teamId] + (if (takerId == teamId) bonus else totalPoints + bonus)
        }
    }
}