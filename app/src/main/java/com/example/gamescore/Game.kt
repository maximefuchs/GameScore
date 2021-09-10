package com.example.gamescore

import java.io.Serializable

class Game : Serializable {
    constructor()
    constructor(game_id: Int, nb_players: Int, player_take: Int, contract: String, teammate: Int, success: Boolean, difference : Int, bonus: Int, score: List<Int>) {
        this.game_id = game_id
        this.nb_players = nb_players
        this.player_take = player_take
        this.teammate = teammate
        this.contract = contract
        this.success = success
        this.difference = difference
        this.bonus = bonus
        this.score = score
    }

    constructor(game_id: Int, nb_players: Int, player_take: Int, contract: String, success: Boolean, difference : Int, bonus: Int, score: List<Int>) {
        this.game_id = game_id
        this.nb_players = nb_players
        this.player_take = player_take
        this.contract = contract
        this.difference = difference
        this.success = success
        this.bonus = bonus
        this.score = score
    }

    var game_id: Int = 0
    var nb_players: Int = 4
    var player_take: Int = 0
    var teammate: Int = 1
    var contract : String = "Garde"
    var success : Boolean = true
    var difference: Int = 0
    var bonus: Int = -1
    var score: List<Int> = listOf<Int>()

    override fun toString(): String {
        return "Game(nb_players=$nb_players, player_take=$player_take, contract='$contract', teammate=$teammate, score=$score)"
    }

}