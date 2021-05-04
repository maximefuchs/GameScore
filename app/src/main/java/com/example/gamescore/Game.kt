package com.example.gamescore

class Game {
    constructor()
    constructor(
        nb_players: Int,
        player_take: Int,
        contract: String,
        teammate: Int,
        score: List<Int>
    ) {
        this.nb_players = nb_players
        this.player_take = player_take
        this.contract = contract
        this.teammate = teammate
        this.score = score
    }

    constructor(nb_players: Int, player_take: Int, contract: String, score: List<Int>) {
        this.nb_players = nb_players
        this.player_take = player_take
        this.contract = contract
        this.score = score
    }

    var nb_players: Int = 4
    var player_take: Int = 0
    var contract : String = "Garde"
    var teammate: Int = 1
    var score: List<Int> = listOf<Int>()

    override fun toString(): String {
        return "Game(nb_players=$nb_players, player_take=$player_take, contract='$contract', teammate=$teammate, score=$score)"
    }

}