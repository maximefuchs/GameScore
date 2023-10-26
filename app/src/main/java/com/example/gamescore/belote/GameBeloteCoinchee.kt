package com.example.gamescore.belote

import kotlin.math.abs

open class GameBeloteCoinchee : GameBelote {
    constructor()

    constructor(
        game_id: Int,
        taker: Int,
        success: Boolean,
        contract: Int,
        difference: Int,
        bonus1: Int,
        bonus2: Int,
        isCoinchee: Boolean
    ) : super(
        game_id,
        taker,
        success,
        difference,
        bonus1,
        bonus2
    ) {
        this.nbPlayers = 4
        this.coinchee = isCoinchee
        this.contract = contract
    }

    var contract: Int = 80
    var coinchee: Boolean = false

    override fun toString(): String {
        return super.toString() + "| Belote | taker $takerId, success $success, difference $difference"
    }

    override fun updateScore(previousScore: IntArray) {
        var totalPoints = if (abs(difference) == 250) 250 else 162
        if (success) {
            // if coinchee, double contract value
            if (takerId == 0 && success) bonusTeam1 += if (coinchee) 2 * contract else contract
            if (takerId == 1 && success) bonusTeam1 += if (coinchee) 2 * contract else contract
            if (coinchee && !success) totalPoints *= 2 // double reward for defense if defeat when coinchee
        }
        score = previousScore.clone()
        computeScore(totalPoints)
        // double points if coinchée and game is lost
        if (!success && coinchee) {
            score[(takerId + 1) % 2] += 162
            // takerId + 1 % 2 gives 1 if takerId = 0 and 0 if takerId = 1
        }
    }
}