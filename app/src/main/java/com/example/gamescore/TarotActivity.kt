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
    var NbPlayers : Int = 0
    var names : List<String> = listOf<String>()
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

        supportFragmentManager.beginTransaction().replace(R.id.container,NbPlayersFragment()).commit()
    }

    fun getName(nb_players: Int){
        NbPlayers = nb_players
        supportFragmentManager.beginTransaction().replace(R.id.container,NameFragment()).commit()
    }
    fun startGame(list_names : List<String>){
        names = list_names
        val f = ScoreFragment()
        listGames = ArrayList<Game>()
        f.listPlayers = names
        f.listGames = listGames
        supportFragmentManager.beginTransaction().replace(R.id.container,f).commit()
    }
    fun addTarotGame(){
        val intent = Intent(this, AddGameTarotActivity::class.java)
        val bundle = Bundle()
        bundle.putStringArrayList("players", ArrayList(names))
        intent.putExtras(bundle)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 1) {
//            if (resultCode == Activity.RESULT_OK) {
                val b: Bundle? = data?.extras
                val contrat = b?.getString("contrat")
                Log.w("contrat", contrat)
                val valContrat = contrats.get(contrat)
                Log.w("val contrat", valContrat.toString())
                val result = b?.getBoolean("result")
                Log.w("RES", result.toString())
                val ecart = b?.getInt("ecart")
                Log.w("Ecart", ecart.toString())
                var toAdd = valContrat?.plus(ecart!!)
                if (!result!!) toAdd = -toAdd!!
                Log.w("to ADD", toAdd.toString())

                val g: Game
                val newScore : MutableList<Int>
                if (listGames.size > 0)
                    newScore = listGames.last().score.toMutableList()
                else {
                    if (names.size == 4)
                        newScore = mutableListOf<Int>(0, 0, 0, 0)
                    else
                        newScore = mutableListOf<Int>(0, 0, 0, 0, 0)
                }
                if (names.size == 4) {
                    for (i in 0..3) {
                        if (i == b.getInt("preneur"))
                            newScore[i] = newScore[i] + 3 * toAdd!!
                        else
                            newScore[i] = newScore[i] - toAdd!!
                    }
                    g = Game(names.size, b.getInt("preneur"), contrat!!, newScore)
                } else {
                    for (i in 0..4) {
                        if (i == b.getInt("preneur"))
                            newScore[i] = newScore[i] + 2 * toAdd!!
                        else if (i == b.getInt("appel"))
                            newScore[i] = newScore[i] + toAdd!!
                        else
                            newScore[i] = newScore[i] - toAdd!!
                    }
                    g = Game(
                        names.size,
                        b.getInt("preneur"),
                        contrat!!,
                        b.getInt("appel"),
                        newScore
                    )
                }

                listGames.add(g)

//                Log.w("H", "////////////////////////////////////////////////////// HEY")
                Log.w("size", listGames.size.toString())
                var f = ScoreFragment()
                f.listPlayers = names
                f.listGames = listGames
                supportFragmentManager.beginTransaction().replace(R.id.container,f).commit()
//            }
//        }
    }

}
