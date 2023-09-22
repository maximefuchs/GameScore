package com.example.gamescore.belote

class GameBelote3 : GameBelote {
    constructor()

    constructor(
        game_id: Int,
        taker: Int,
        success: Boolean,
        difference: Int,
        bonus1: Int,
        bonus2: Int,
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
        this.nbPlayers = 3
    }

    override fun toString(): String {
        return super.toString() + "| Belote à 3 | "
    }

}