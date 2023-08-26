package com.example.gamescore.belote

import com.example.gamescore.Game

open class GameBelote : Game {
    constructor()

    constructor(game_id: Int, taker: Int, success: Boolean, difference : Int, bonus1: Int, bonus2: Int, score: List<Int>) {
        this.gameId = game_id
        this.nbPlayers = 4
        this.taker = taker
        this.difference = difference
        this.success = success
        this.bonusTeam1 = bonus1
        this.bonusTeam2 = bonus2
        this.score = score
    }

    var taker: Int = 0
    var success : Boolean = true
    var difference: Int = 0
    var bonusTeam1: Int = -1
    var bonusTeam2: Int = -1

    override fun toString(): String {
        return super.toString() + "| Belote | taker $taker, success $success, difference $difference"
    }

}