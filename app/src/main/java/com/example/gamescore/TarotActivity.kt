package com.example.gamescore

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarot)
        context = this

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
        bundle.putStringArrayList("players", ArrayList(names))
        intent.putExtras(bundle)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val b: Bundle? = data?.extras
                val contrat = b?.getString("contrat")
                val valContrat = contrats.get(contrat)
                val result = b?.getBoolean("result")
                val ecart = b?.getInt("ecart")
                var toAdd = valContrat?.plus(ecart!!)
                if (!result!!) toAdd = -toAdd!!
//                Log.w("contrat", contrat)

                val g: Game
                val newScore: MutableList<Int>
                if (listGames.size > 0)
                {
//                    newScore = listGames.last().score.toMutableList()
                    newScore = listGames.get(0).score.toMutableList()
                }
                else {
                    if (names.size == 4) newScore = mutableListOf<Int>(0, 0, 0, 0)
                    else newScore = mutableListOf<Int>(0, 0, 0, 0, 0)
                }
                val preneur = b.getInt("preneur")
                val bonus = b.getInt("bonus")
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
                    g = Game(
                        names.size,
                        preneur,
                        contrat!!,
                        newScore
                    )
                } else {
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
                    g = Game(
                        names.size,
                        preneur,
                        contrat!!,
                        appel,
                        newScore
                    )
                }

                listGames.add(g)

                Log.w("size", listGames.size.toString())
                val f = ScoreFragment()
                f.listPlayers = names
                f.listGames = listGames
                supportFragmentManager.beginTransaction().replace(R.id.container, f).commit()
            }
        }
    }
}
