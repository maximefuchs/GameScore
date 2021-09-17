package com.example.gamescore.tarot

import com.example.gamescore.Game
import java.io.Serializable

open class GameTarot : Game {
    constructor()
    constructor(game_id: Int, player_take: Int, contract: String, success: Boolean, difference : Int, bonus: Int, score: List<Int>) {
        this.game_id = game_id
        this.nb_players = 4
        this.player_take = player_take
        this.contract = contract
        this.difference = difference
        this.success = success
        this.bonus = bonus
        this.score = score
    }

    var player_take: Int = 0
    var contract : String = "Garde"
    var success : Boolean = true
    var difference: Int = 0
    var bonus: Int = -1

    override fun toString(): String {
        return super.toString() + "| Tarot | nb_players=$nb_players, player_take=$player_take, contract='$contract', score=$score "
    }

}