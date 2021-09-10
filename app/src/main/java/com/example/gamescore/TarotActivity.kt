package com.example.gamescore

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_tarot.*
import kotlinx.android.synthetic.main.fragment_score.*
import java.util.ArrayList

class TarotActivity : AppCompatActivity() {
    lateinit var context: Context
    var NbPlayers: Int = 0
    var names: List<String> = listOf<String>()
    lateinit var listGames: ArrayList<Game>
    var contrats: HashMap<String, Int> = hashMapOf(
        "Petite" to 10,
        "Pousse" to 20,
        "Garde" to 40,
        "Garde Sans" to 80,
        "Garde Contre" to 160
    )
    val ADDGAME = 1
    val EDITGAME = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarot)
        context = this

        // quitting
        RLquit.visibility = View.GONE
        btnQuit.setOnClickListener { finish() }
        btnNoQuit.setOnClickListener { RLquit.visibility = View.GONE }

        supportFragmentManager.beginTransaction().replace(R.id.container, NbPlayersFragment())
            .commit()
    }

    fun getName(nb_players: Int) {
        NbPlayers = nb_players
        supportFragmentManager.beginTransaction().replace(R.id.container, NameFragment()).commit()
    }

    fun startGame(list_names: List<String>) {
        names = list_names
        val f = ScoreFragment()
        listGames = ArrayList<Game>()
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
        startActivityForResult(intent, ADDGAME)
    }

    fun editTarotGame(game_id: Int): Boolean {
        val intent = Intent(this, AddGameTarotActivity::class.java)
        val bundle = Bundle()
        bundle.putBoolean("edit",true)
        bundle.putStringArrayList("players", ArrayList(names))
        bundle.putSerializable("lastGame",listGames[game_id])
        intent.putExtras(bundle)
        startActivityForResult(intent, EDITGAME)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADDGAME) {
            if (resultCode == Activity.RESULT_OK) {
                val b: Bundle? = data?.extras
                val preneur = b?.getInt("preneur")
                val contrat = b?.getString("contrat")
                val valContrat = contrats.get(contrat)
                val result = b?.getBoolean("result")
                val ecart = b?.getInt("ecart")
                val bonus = b?.getInt("bonus")
                var toAdd = valContrat?.plus(ecart!!)
                if (!result!!) toAdd = -toAdd!!
//                Log.w("contrat", contrat)

                val g: Game
                var game_id = 0
                val newScore: MutableList<Int>
                if (listGames.size > 0)
                {
                    newScore = listGames.last().score.toMutableList()
//                    newScore = listGames.get(0).score.toMutableList()
                    game_id = listGames.last().game_id + 1
                }
                else {
                    if (names.size == 4) newScore = mutableListOf<Int>(0, 0, 0, 0)
                    else newScore = mutableListOf<Int>(0, 0, 0, 0, 0)
                }
                if (names.size == 4) {
                    for (i in 0..3) {
                        if (i == preneur) newScore[i] = newScore[i] + 3 * toAdd!!
                        else newScore[i] = newScore[i] - toAdd!!

                        if (bonus != -1) // -1 means no bonus
                        {
                            if (i == bonus) newScore[i] = newScore[i] + 30
                            else newScore[i] = newScore[i] - 10
                        }
                    }
                    g = Game(game_id,names.size,preneur!!,contrat!!,result,ecart!!,bonus!!,newScore)
                }
                else {
                    val appel = b.getInt("appel")
                    for (i in 0..4) {
                        if (appel == preneur) {
                            if (i == preneur) newScore[i] = newScore[i] + 4 * toAdd!!
                            else newScore[i] = newScore[i] - toAdd!!
                        } else {
                            if (i == preneur) newScore[i] = newScore[i] + 2 * toAdd!!
                            else if (i == appel) newScore[i] = newScore[i] + toAdd!!
                            else newScore[i] = newScore[i] - toAdd!!
                        }

                        if (bonus != -1) // -1 means no bonus
                        {
                            if (i == bonus) newScore[i] = newScore[i] + 40
                            else newScore[i] = newScore[i] - 10
                        }
                    }
                    g = Game(game_id,names.size,preneur!!,contrat!!,appel,result,ecart!!,bonus!!,newScore)
                }

                listGames.add(g)

                Log.w("size", listGames.size.toString())
                val f = ScoreFragment()
                f.listPlayers = names
                f.listGames = listGames
                supportFragmentManager.beginTransaction().replace(R.id.container, f).commit()
            }
        }
        if (requestCode == EDITGAME) {
            if (resultCode == Activity.RESULT_OK) {
                val b: Bundle? = data?.extras
                val preneur = b?.getInt("preneur")
                val contrat = b?.getString("contrat")
                val result = b?.getBoolean("result")
                val ecart = b?.getInt("ecart")
                val bonus = b?.getInt("bonus")

                val g = listGames[b!!.getInt("game_id")]
                g.player_take = preneur!!
                g.contract = contrat!!
                g.success = result!!
                g.difference = ecart!!
                g.bonus = bonus!!


                val newScore: MutableList<Int>
                if (names.size == 4) newScore = mutableListOf<Int>(0, 0, 0, 0)
                else newScore = mutableListOf<Int>(0, 0, 0, 0, 0)
                for (game_id in 0..listGames.size - 1) {
                    val g = listGames[game_id]
                    val valContrat = contrats[g.contract]
                    var toAdd = valContrat?.plus(g.difference)
                    if (!g.success) toAdd = -toAdd!!
                    if (names.size == 4) {
                        for (i in 0..3) {
                            if (i == g.player_take) newScore[i] = newScore[i] + 3 * toAdd!!
                            else newScore[i] = newScore[i] - toAdd!!

                            if (g.bonus != -1) // -1 means no bonus
                            {
                                if (i == g.bonus) newScore[i] = newScore[i] + 30
                                else newScore[i] = newScore[i] - 10
                            }
                        }
                    } else {
                        val appel = g.teammate
                        val preneur = g.player_take
                        for (i in 0..4) {
                            if (appel == preneur) {
                                if (i == preneur) newScore[i] = newScore[i] + 4 * toAdd!!
                                else newScore[i] = newScore[i] - toAdd!!
                            } else {
                                if (i == preneur) newScore[i] = newScore[i] + 2 * toAdd!!
                                else if (i == appel) newScore[i] = newScore[i] + toAdd!!
                                else newScore[i] = newScore[i] - toAdd!!
                            }

                            if (g.bonus != -1) // -1 means no bonus
                            {
                                if (i == g.bonus) newScore[i] = newScore[i] + 40
                                else newScore[i] = newScore[i] - 10
                            }
                        }
                    }
                    g.score = newScore.toMutableList()
                }

                val f = ScoreFragment()
                f.listPlayers = names
                f.listGames = listGames
                supportFragmentManager.beginTransaction().replace(R.id.container, f).commit()
            }
        }
    }

    override fun onBackPressed() {
        RLquit.visibility = View.VISIBLE
//        super.onBackPressed()
    }
}
