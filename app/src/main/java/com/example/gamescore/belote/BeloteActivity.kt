package com.example.gamescore.belote

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.example.gamescore.*
import com.example.gamescore.tarot.AddGameTarotActivity
import com.example.gamescore.tarot.GameTarot
import com.example.gamescore.tarot.GameTarot5
import com.example.gamescore.tarot.ScoreTarotFragment
import kotlinx.android.synthetic.main.activity_game.*
import java.util.ArrayList

enum class TypeGame {
    NORMALE,
    COINCHEE
}

class BeloteActivity : GameActivity() {

    var gameType = TypeGame.NORMALE
    var nbPlayers = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        FrameTitle.text = resources.getText(R.string.game_belote)

        supportFragmentManager.beginTransaction().replace(
            R.id.container,
            TypeBeloteFragment()
        ).commit()

        add_game.setOnClickListener { addBeloteGame() }
    }

    fun typeOfGame(game_of_type: TypeGame) {
        gameType = game_of_type
        if (gameType == TypeGame.NORMALE)
            supportFragmentManager.beginTransaction().replace(
                R.id.container,
                NbPlayersFragment()
            ).commit()
        else
            getName(4)
    }
    fun getName(nbOfPlayers : Int) {
        nbPlayers = nbOfPlayers
        supportFragmentManager.beginTransaction().replace(
            R.id.container,
            NameFragmentBelote()
        ).commit()
    }

    fun startGame(list_names: List<String>) {
        names = list_names
        val f = ScoreBeloteFragment()
        listGames = ArrayList<Game>()
        f.listPlayers = names
        f.listGames = listGames
        supportFragmentManager.beginTransaction().replace(R.id.container, f).commit()
        add_game.visibility = View.VISIBLE
    }

    fun addBeloteGame() {
        val intent = Intent(this, AddGameBeloteActivity::class.java)
        val bundle = Bundle()
        bundle.putBoolean("edit", false)
        bundle.putStringArrayList("players", ArrayList(names))
        intent.putExtras(bundle)
        resultLauncher.launch(intent)
    }

    fun editBeloteGame(game_id: Int): Boolean {
        val intent = Intent(this, AddGameBeloteActivity::class.java)
        val bundle = Bundle()
        bundle.putBoolean("edit", true)
        bundle.putStringArrayList("players", ArrayList(names))
        bundle.putSerializable("lastGame", listGames[game_id])
        intent.putExtras(bundle)
        resultLauncher.launch(intent)
        return true
    }


    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result ->
        if (result.resultCode == Request.ADDGAME.value) {
            val b: Bundle = result.data?.extras!!
            val preneur = b.getInt("preneur")
            val result = b.getBoolean("result")
            val ecart = b.getInt("ecart")
            var bonusT1 = b.getInt("bonusT1")
            var bonusT2 = b.getInt("bonusT2")

            val g: GameBelote
            var game_id = 0
            val newScore: MutableList<Int>
            if (listGames.size > 0) {
                newScore = listGames.last().score.toMutableList()
                game_id = listGames.last().game_id + 1
            } else {
                if (nbPlayers == 3) newScore = mutableListOf<Int>(0, 0, 0)
                else newScore = mutableListOf<Int>(0, 0)
            }
            val s = if (ecart == 250) 250 else (162 + ecart) / 2
            var contrat: Int = 80
            if (gameType == TypeGame.COINCHEE && result) {
                contrat = b.getString("contrat")!!.toInt()
                if (preneur == 0 && result) bonusT1 += contrat
                if (preneur == 1 && result) bonusT2 += contrat
            }
            var totalPoints = if (ecart == 250) 250 else 162
            if (nbPlayers == 4) {
                for (i in 0..1) {
                    if (result) {
                        newScore[i] =
                            newScore[i] + (if (preneur == i) s + bonusT1 else totalPoints - s + bonusT2)
                    } else
                        newScore[i] =
                            newScore[i] + (if (preneur == i) 0 + bonusT1 else totalPoints + bonusT2)
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
                        newScore
                    )
                } else {
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
            } else {
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

            val f = ScoreBeloteFragment()
            f.listPlayers = names
            f.listGames = listGames
            supportFragmentManager.beginTransaction().replace(R.id.container, f).commit()
        }

//        if (result.resultCode == Request.EDITGAME.value) {
//            val b: Bundle = result.data?.extras!!
//            val preneur = b.getInt("preneur")
//            val contrat = b.getString("contrat")!!
//            val result = b.getBoolean("result")
//            val ecart = b.getInt("ecart")
//            val bonus = b.getInt("bonus")
//
//            val g: GameTarot
//            if (names.size == 4)
//                g = listGames[b.getInt("game_id")] as GameTarot
//            else {
//                g = listGames[b.getInt("game_id")] as GameTarot5
//                g.teammate = b.getInt("appel")
//            }
//            g.player_take = preneur
//            g.contract = contrat
//            g.success = result
//            g.difference = ecart
//            g.bonus = bonus
//
//
//            val newScore: MutableList<Int>
//            if (names.size == 4) newScore = mutableListOf<Int>(0, 0, 0, 0)
//            else newScore = mutableListOf<Int>(0, 0, 0, 0, 0)
//            for (game_id in 0..listGames.size - 1) {
//                var gi: GameTarot = listGames[game_id] as GameTarot
//                val valContrat = contrats[gi.contract]
//                var toAdd = valContrat?.plus(gi.difference)
//                if (!gi.success) toAdd = -toAdd!!
//                if (names.size == 4) {
//                    for (i in 0..3) {
//                        if (i == gi.player_take) newScore[i] = newScore[i] + 3 * toAdd!!
//                        else newScore[i] = newScore[i] - toAdd!!
//
//                        if (gi.bonus != -1) // -1 means no bonus
//                        {
//                            if (i == gi.bonus) newScore[i] = newScore[i] + 30
//                            else newScore[i] = newScore[i] - 10
//                        }
//                    }
//                } else {
//                    gi = listGames[game_id] as GameTarot5
//                    val appel = gi.teammate
//                    val preneur_i = gi.player_take
//                    for (i in 0..4) {
//                        if (appel == preneur_i) {
//                            if (i == preneur_i) newScore[i] = newScore[i] + 4 * toAdd!!
//                            else newScore[i] = newScore[i] - toAdd!!
//                        } else {
//                            if (i == preneur_i) newScore[i] = newScore[i] + 2 * toAdd!!
//                            else if (i == appel) newScore[i] = newScore[i] + toAdd!!
//                            else newScore[i] = newScore[i] - toAdd!!
//                        }
//
//                        if (gi.bonus != -1) // -1 means no bonus
//                        {
//                            if (i == gi.bonus) newScore[i] = newScore[i] + 40
//                            else newScore[i] = newScore[i] - 10
//                        }
//                    }
//                }
//                gi.score = newScore.toMutableList()
//            }
//
//            val f = ScoreTarotFragment()
//            f.listPlayers = names
//            f.listGames = listGames
//            supportFragmentManager.beginTransaction().replace(R.id.container, f).commit()
//
//        }
    }
}