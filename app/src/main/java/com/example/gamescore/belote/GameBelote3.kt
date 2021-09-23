package com.example.gamescore.belote

import com.example.gamescore.Game
import java.io.Serializable

class GameBelote3 : GameBelote {
    constructor()

    constructor(game_id: Int, taker: Int, success: Boolean, difference : Int, bonus1: Int, bonus2: Int, score: List<Int>) {
        this.game_id = game_id
        this.nb_players = 3
        this.taker = taker
        this.difference = difference
        this.success = success
        this.bonusTeam1 = bonus1
        this.bonusTeam2 = bonus2
        this.score = score
    }

    override fun toString(): String {
        return super.toString() + "| Belote à 3 | "
    }

}