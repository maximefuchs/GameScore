package com.example.gamescore.belote

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.gamescore.*
import kotlinx.android.synthetic.main.activity_game.*
import kotlin.math.abs
import java.util.ArrayList

enum class TypeGame {
    NORMALE,
    COINCHEE
}

class BeloteActivity : GameActivity() {

    private var gameType = TypeGame.NORMALE
    var nbPlayers = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        FrameTitle.text = resources.getText(R.string.game_belote)

        fragmentTransition(R.id.container,NbPlayersBeloteFragment())
        add_game.setOnClickListener { addBeloteGame() }
    }

    fun getTypeOfGame() {
//        nbPlayers = 4
        fragmentTransition(R.id.container,TypeBeloteFragment())
    }

    fun typeOfGame(game_of_type: TypeGame) {
        gameType = game_of_type
        getName(4)
    }

    fun getName(nbOfPlayers: Int) {
        nbPlayers = nbOfPlayers
        fragmentTransition(R.id.container,NameFragmentBelote())
    }

    fun startGame(list_names: ArrayList<String>) {
        hideKeyBoard()
        names = list_names
        listGames = ArrayList<Game>()
        fragmentTransition(R.id.container,ScoreBeloteFragment())
    }

    private fun addBeloteGame() {
        val intent = Intent(this, AddGameBeloteActivity::class.java)
        val bundle = Bundle()
        bundle.putBoolean("edit", false)
        bundle.putStringArrayList("players", ArrayList(names))
        intent.putExtras(bundle)
        intent.putExtra("isCoinchee", gameType == TypeGame.COINCHEE)
        resultLauncher.launch(intent)
    }

    fun editBeloteGame(game_id: Int): Boolean {
        val intent = Intent(this, AddGameBeloteActivity::class.java)
        val bundle = Bundle()
        bundle.putBoolean("edit", true)
        bundle.putStringArrayList("players", ArrayList(names))
        bundle.putSerializable("lastGame", listGames[game_id])
        intent.putExtras(bundle)
        intent.putExtra("isCoinchee", gameType == TypeGame.COINCHEE)
        resultLauncher.launch(intent)
        return true
    }


    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { res ->
            if (res.resultCode == Request.ADDGAME.value) {
                val b: Bundle = res.data?.extras!!
                val preneur = b.getInt("preneur")
                val result = b.getBoolean("result")
                val ecart = b.getInt("ecart")
                var bonusT1 = b.getInt("bonusT1")
                var bonusT2 = b.getInt("bonusT2")

                val g: GameBelote
                val game_id: Int
                val newScore: MutableList<Int>
                if (listGames.size > 0) {
                    newScore = listGames.last().score.toMutableList()
                    game_id = listGames.last().gameId + 1
                } else {
                    game_id = 0
                    newScore = if (nbPlayers == 3) mutableListOf(0, 0, 0) else mutableListOf(0, 0)
                }
                val s = if (abs(ecart) == 250) 250 else (162 + ecart) / 2
                var totalPoints = if (abs(ecart) == 250) 250 else 162
                var contrat = 80
                var isCoinchee = false
                if (gameType == TypeGame.COINCHEE && result) {
                    contrat = b.getInt("contrat")
                    isCoinchee = b.getBoolean("coinchee")
                    // if coinchee, double contract value
                    if (preneur == 0 && result) bonusT1 += if (isCoinchee) 2 * contrat else contrat
                    if (preneur == 1 && result) bonusT2 += if (isCoinchee) 2 * contrat else contrat
                    if(isCoinchee && !result) totalPoints *= 2 // double reward for defense if defeat when coinchee
                }
                if (nbPlayers == 4) {
                    for (i in 0..1) {
                        val bonus = if (i == 0) bonusT1 else bonusT2
                        if (result)
                            newScore[i] =
                                newScore[i] + (if (preneur == i) s + bonus else totalPoints - s + bonus)
                        else
                            newScore[i] =
                                newScore[i] + (if (preneur == i) 0 + bonus else totalPoints + bonus)
                    }

                    if (gameType == TypeGame.COINCHEE) {
                        g = GameBeloteCoinchee(
                            game_id,
                            preneur,
                            result,
                            contrat,
                            ecart,
                            bonusT1,
                            bonusT2,
                            isCoinchee,
                            newScore
                        )
                    }
                    else {
                        g = GameBelote(
                            game_id,
                            preneur,
                            result,
                            ecart,
                            bonusT1,
                            bonusT2,
                            newScore
                        )
                    }
                }
                else {
                    if (!result) totalPoints /= 2
                    for (i in 0..2) {
                        if (result)
                            newScore[i] =
                                newScore[i] + (if (preneur == i) s + bonusT1 else totalPoints - s + bonusT2)
                        else
                            newScore[i] =
                                newScore[i] + (if (preneur == i) 0 + bonusT1 else totalPoints + bonusT2)
                    }
                    g = GameBelote3(
                        game_id,
                        preneur,
                        result,
                        ecart,
                        bonusT1,
                        bonusT2,
                        newScore
                    )
                }
                listGames.add(g)

                supportFragmentManager.beginTransaction().replace(R.id.container, ScoreBeloteFragment()).commit()
            }

            if (res.resultCode == Request.EDITGAME.value) {
                val b: Bundle = res.data?.extras!!
                val preneur = b.getInt("preneur")
                val result = b.getBoolean("result")
                val ecart = b.getInt("ecart")
                val bonusT1 = b.getInt("bonusT1")
                val bonusT2 = b.getInt("bonusT2")

                val g: GameBelote
                if (nbPlayers == 4) {
                    if (gameType == TypeGame.NORMALE)
                        g = listGames[b.getInt("game_id")] as GameBelote
                    else { // coinchee
                        g = listGames[b.getInt("game_id")] as GameBeloteCoinchee
                        g.contrat = b.getInt("contrat")
                        g.coinchee = b.getBoolean("coinchee")
                    }
                } else
                    g = listGames[b.getInt("game_id")] as GameBelote3
                g.taker = preneur
                g.success = result
                g.difference = ecart
                g.bonusTeam1 = bonusT1
                g.bonusTeam2 = bonusT2


                val newScore = if (nbPlayers == 4) mutableListOf(0, 0) else mutableListOf(0, 0, 0)
                for (game_id in 0 until listGames.size) {
                    val gi: GameBelote = listGames[game_id] as GameBelote
                    val ecart_i = gi.difference
                    val s = if (abs(ecart_i) == 250) 250 else (162 + ecart_i) / 2
                    var totalPoints = if (abs(ecart_i) == 250) 250 else 162

                    var bonusT1_i = gi.bonusTeam1
                    var bonusT2_i = gi.bonusTeam2

                    if (gameType == TypeGame.COINCHEE) {
                        val giCoinchee = (listGames[game_id] as GameBeloteCoinchee)
                        val contrat = giCoinchee.contrat
                        val isCoinchee = giCoinchee.coinchee
                        // if coinchee, double contract value
                        if (gi.taker == 0 && gi.success) bonusT1_i += if (isCoinchee) 2 * contrat else contrat
                        if (gi.taker == 1 && gi.success) bonusT2_i += if (isCoinchee) 2 * contrat else contrat
                        if(isCoinchee && !gi.success) totalPoints *= 2 // double reward for defense if defeat when coinchee
                    }

                    if (nbPlayers == 4) {
                        for (i in 0..1) {
                            val bonus = if (i == 0) bonusT1_i else bonusT2_i
                            if (gi.success)
                                newScore[i] =
                                    newScore[i] + (if (gi.taker == i) s + bonus else totalPoints - s + bonus)
                            else
                                newScore[i] =
                                    newScore[i] + (if (gi.taker == i) 0 + bonus else totalPoints + bonus)
                        }
                    } else { // nbPlayers == 3
                        if (!gi.success) totalPoints /= 2
                        for (i in 0..2) {
                            if (gi.success)
                                newScore[i] =
                                    newScore[i] + (if (gi.taker == i) s + bonusT1_i else totalPoints - s + bonusT2_i)
                            else
                                newScore[i] =
                                    newScore[i] + (if (gi.taker == i) 0 + bonusT1_i else totalPoints + bonusT2_i)
                        }
                    }
                    gi.score = newScore.toMutableList()
                }

                supportFragmentManager.beginTransaction().replace(R.id.container, ScoreBeloteFragment()).commit()
            }
        }
}