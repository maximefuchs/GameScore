package com.example.gamescore.belote

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
        isCoinchee: Boolean,
        score: IntArray
    ) : super(
        game_id,
        taker,
        success,
        difference,
        bonus1,
        bonus2,
        score
    ) {
        this.nbPlayers = 4
        this.coinchee = isCoinchee
        this.contract = contract
    }

    var contract: Int = 80
    var coinchee: Boolean = false

    override fun toString(): String {
        return super.toString() + "| Belote | taker $taker, success $success, difference $difference"
    }

}