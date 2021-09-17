package com.example.gamescore

import java.io.Serializable

open class Game() : Serializable {

    var game_id: Int = 0
    var nb_players: Int = 4
    var score: List<Int> = listOf<Int>()

    override fun toString(): String {
        return "|| Game || game_id=$game_id, nb_players=$nb_players, score=$score "
    }

}