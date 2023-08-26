package com.example.gamescore.tarot

class GameTarot5 : GameTarot {
    constructor()
    constructor(game_id: Int, player_take: Int, contract: String, teammate: Int, success: Boolean, difference : Int, bonus: Int, score: List<Int>) {
        this.gameId = game_id
        this.nbPlayers = 5
        this.playerTake = player_take
        this.teammate = teammate
        this.contract = contract
        this.success = success
        this.difference = difference
        this.bonus = bonus
        this.score = score
    }

    var teammate: Int = 1

    override fun toString(): String {
        return super.toString() + "| Tarot à 5 | teammate=$teammate"
    }

}