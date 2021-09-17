package com.example.gamescore.tarot

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.gamescore.*
import kotlinx.android.synthetic.main.activity_game.*
import java.util.ArrayList

class TarotActivity : GameActivity() {
    var nbPlayers: Int = 0
    var contrats: HashMap<String, Int> = hashMapOf(
        "Petite" to 10,
        "Pousse" to 20,
        "Garde" to 40,
        "Garde Sans" to 80,
        "Garde Contre" to 160
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        FrameTitle.text = resources.getText(R.string.game_tarot)

        supportFragmentManager.beginTransaction().replace(
            R.id.container,
            NbPlayersFragment()
        )
            .commit()
    }

    fun getName(nb_players: Int) {
        nbPlayers = nb_players
        supportFragmentManager.beginTransaction().replace(
            R.id.container,
            NameFragmentTarot()
        ).commit()
    }

    fun startGame(list_names: List<String>) {
        names = list_names
        val f = ScoreTarotFragment()
        listGames = ArrayList<GameTarot>()
        f.listPlayers = names
        f.listGames = listGames
        supportFragmentManager.beginTransaction().replace(R.id.container, f).commit()
    }

    fun addTarotGame() {
        val intent = Intent(this, AddGameTarotActivity::class.java)
        val bundle = Bundle()
        bundle.putBoolean("edit",false)
        bundle.putStringArrayList("players", ArrayList(names))
        intent.putExtras(bundle)
        startActivityForResult(intent, Request.ADDGAME.value)
    }

    fun editTarotGame(game_id: Int): Boolean {
        val intent = Intent(this, AddGameTarotActivity::class.java)
        val bundle = Bundle()
        bundle.putBoolean("edit",true)
        bundle.putStringArrayList("players", ArrayList(names))
        bundle.putSerializable("lastGame",listGames[game_id])
        intent.putExtras(bundle)
        startActivityForResult(intent, Request.EDITGAME.value)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Request.ADDGAME.value) {
            if (resultCode == Activity.RESULT_OK) {
                val b: Bundle = data?.extras!!
                val preneur = b.getInt("preneur")
                val contrat = b.getString("contrat")!!
                val valContrat = contrats[contrat]!!
                val result = b.getBoolean("result")
                val ecart = b.getInt("ecart")
                val bonus = b.getInt("bonus")
                var toAdd = valContrat.plus(ecart)
                if (!result) toAdd = -toAdd

                val g: GameTarot
                var game_id = 0
                val newScore: MutableList<Int>
                if (listGames.size > 0)
                {
                    newScore = listGames.last().score.toMutableList()
                    game_id = listGames.last().game_id + 1
                }
                else {
                    if (names.size == 4) newScore = mutableListOf<Int>(0, 0, 0, 0)
                    else newScore = mutableListOf<Int>(0, 0, 0, 0, 0)
                }
                if (names.size == 4) {
                    for (i in 0..3) {
                        if (i == preneur) newScore[i] = newScore[i] + 3 * toAdd
                        else newScore[i] = newScore[i] - toAdd

                        if (bonus != -1) // -1 means no bonus
                        {
                            if (i == bonus) newScore[i] = newScore[i] + 30
                            else newScore[i] = newScore[i] - 10
                        }
                    }
                    g = GameTarot(
                        game_id,
                        preneur,
                        contrat,
                        result,
                        ecart,
                        bonus,
                        newScore
                    )
                }
                else {
                    val appel = b.getInt("appel")
                    for (i in 0..4) {
                        if (appel == preneur) {
                            if (i == preneur) newScore[i] = newScore[i] + 4 * toAdd
                            else newScore[i] = newScore[i] - toAdd
                        } else {
                            if (i == preneur) newScore[i] = newScore[i] + 2 * toAdd
                            else if (i == appel) newScore[i] = newScore[i] + toAdd
                            else newScore[i] = newScore[i] - toAdd
                        }

                        if (bonus != -1) // -1 means no bonus
                        {
                            if (i == bonus) newScore[i] = newScore[i] + 40
                            else newScore[i] = newScore[i] - 10
                        }
                    }
                    g = GameTarot5(
                        game_id,
                        preneur,
                        contrat,
                        appel,
                        result,
                        ecart,
                        bonus,
                        newScore
                    )
                }
                listGames.add(g)

//                Log.w("size", listGames.size.toString())
                val f = ScoreTarotFragment()
                f.listPlayers = names
                f.listGames = listGames
                supportFragmentManager.beginTransaction().replace(R.id.container, f).commit()
            }
        }
        if (requestCode == Request.EDITGAME.value) {
            if (resultCode == Activity.RESULT_OK) {
                val b: Bundle = data?.extras!!
                val preneur = b.getInt("preneur")
                val contrat = b.getString("contrat")!!
                val result = b.getBoolean("result")
                val ecart = b.getInt("ecart")
                val bonus = b.getInt("bonus")

                val g :GameTarot
                if (names.size == 4)
                    g = listGames[b.getInt("game_id")]
                else {
                    g = listGames[b.getInt("game_id")] as GameTarot5
                    g.teammate = b.getInt("appel")
                }
                g.player_take = preneur
                g.contract = contrat
                g.success = result
                g.difference = ecart
                g.bonus = bonus


                val newScore: MutableList<Int>
                if (names.size == 4) newScore = mutableListOf<Int>(0, 0, 0, 0)
                else newScore = mutableListOf<Int>(0, 0, 0, 0, 0)
                for (game_id in 0..listGames.size - 1) {
                    var gi : GameTarot = listGames[game_id]
                    val valContrat = contrats[gi.contract]
                    var toAdd = valContrat?.plus(gi.difference)
                    if (!gi.success) toAdd = -toAdd!!
                    if (names.size == 4) {
                        for (i in 0..3) {
                            if (i == gi.player_take) newScore[i] = newScore[i] + 3 * toAdd!!
                            else newScore[i] = newScore[i] - toAdd!!

                            if (gi.bonus != -1) // -1 means no bonus
                            {
                                if (i == gi.bonus) newScore[i] = newScore[i] + 30
                                else newScore[i] = newScore[i] - 10
                            }
                        }
                    } else {
                        gi = listGames[game_id] as GameTarot5
                        val appel = gi.teammate
                        val preneur_i = gi.player_take
                        for (i in 0..4) {
                            if (appel == preneur_i) {
                                if (i == preneur_i) newScore[i] = newScore[i] + 4 * toAdd!!
                                else newScore[i] = newScore[i] - toAdd!!
                            } else {
                                if (i == preneur_i) newScore[i] = newScore[i] + 2 * toAdd!!
                                else if (i == appel) newScore[i] = newScore[i] + toAdd!!
                                else newScore[i] = newScore[i] - toAdd!!
                            }

                            if (gi.bonus != -1) // -1 means no bonus
                            {
                                if (i == gi.bonus) newScore[i] = newScore[i] + 40
                                else newScore[i] = newScore[i] - 10
                            }
                        }
                    }
                    gi.score = newScore.toMutableList()
                }

                val f = ScoreTarotFragment()
                f.listPlayers = names
                f.listGames = listGames
                supportFragmentManager.beginTransaction().replace(R.id.container, f).commit()
            }
        }
    }
}
