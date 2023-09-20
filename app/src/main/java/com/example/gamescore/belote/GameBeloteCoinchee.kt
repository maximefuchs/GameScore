package com.example.gamescore.belote

open class GameBeloteCoinchee : GameBelote {
    constructor()

    constructor(game_id: Int, taker: Int, success: Boolean, contrat: Int, difference : Int, bonus1: Int, bonus2: Int, isCoinchee : Boolean, score: List<Int>) {
        this.gameId = game_id
        this.nbPlayers = 4
        this.taker = taker
        this.difference = difference
        this.success = success
        this.contrat = contrat
        this.bonusTeam1 = bonus1
        this.bonusTeam2 = bonus2
        this.coinchee = isCoinchee
        this.score = score
    }

    var contrat : Int = 80
    var coinchee : Boolean = false

    override fun toString(): String {
        return super.toString() + "| Belote | taker $taker, success $success, difference $difference"
    }

}