package com.example.gamescore

import java.io.Serializable

open class Game() : Serializable {

    var gameId: Int = 0
    var nbPlayers: Int = 4
    var score: List<Int> = listOf<Int>()

    override fun toString(): String {
        return "|| Game || game_id=$gameId, nb_players=$nbPlayers, score=$score "
    }

}