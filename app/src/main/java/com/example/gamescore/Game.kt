package com.example.gamescore

abstract class Game {
    abstract var id: Int
    abstract var player_take: Int
    abstract var contract : Int
    abstract var teammate: Int
    abstract var score: List<Int>
}