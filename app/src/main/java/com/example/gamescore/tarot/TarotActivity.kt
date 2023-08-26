package com.example.gamescore.tarot

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.example.gamescore.*
import kotlinx.android.synthetic.main.activity_game.*
import java.util.*

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

        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE)
        val gameId = sharedPreferences.getInt("id", -1)

        if (gameId != -1) {
            val numberOfPlayers = sharedPreferences.getInt("numberOfPlayers",4)
            val score = mutableListOf<Int>()
            val listNames = mutableListOf<String>()
            for (i in 0..numberOfPlayers) {
                score.add(sharedPreferences.getInt("playerScore_$i",0))
                sharedPreferences.getString("Name_$i",i.toString())?.let { listNames.add(it) }
            }

            btnNoSaved.setOnClickListener(){
                RLsaved.visibility = View.GONE
                fragmentTransition(R.id.container, NbPlayersTarotFragment())
                add_game.setOnClickListener { addTarotGame() }
            }
            btnSaved.setOnClickListener(){
                RLsaved.visibility = View.GONE
                val f = ScoreTarotFragment()
                val game = Game()
                game.gameId = gameId
                game.nbPlayers = numberOfPlayers
                game.score = score
                game.restart = true
                listGames = ArrayList<Game>()
                listGames.add(game)
                f.listPlayers = listNames
                f.listGames = listGames
                fragmentTransition(R.id.container, f)
            }
            RLsaved.visibility = View.VISIBLE

        } else {
            fragmentTransition(R.id.container, NbPlayersTarotFragment())
            add_game.setOnClickListener { addTarotGame() }
        }
    }

    fun getName(nb_players: Int) {
        nbPlayers = nb_players
        fragmentTransition(R.id.container, NameFragmentTarot())
    }

    fun startGame(list_names: List<String>) {
        hideKeyBoard()

        val sharedPreferences: SharedPreferences = context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        list_names.forEachIndexed { index, value ->
            editor.putString("Name_$index", value)
        }
        editor.apply()

        names = list_names
        val f = ScoreTarotFragment()
        listGames = ArrayList<Game>()
        f.listPlayers = names
        f.listGames = listGames
        fragmentTransition(R.id.container, f)
    }

    private fun addTarotGame() {
        val intent = Intent(this, AddGameTarotActivity::class.java)
        val bundle = Bundle()
        bundle.putBoolean("edit", false)
        bundle.putStringArrayList("players", ArrayList(names))
        intent.putExtras(bundle)
        resultLauncher.launch(intent)
    }

    fun editTarotGame(game_id: Int): Boolean {
        val intent = Intent(this, AddGameTarotActivity::class.java)
        val bundle = Bundle()
        bundle.putBoolean("edit", true)
        bundle.putStringArrayList("players", ArrayList(names))
        bundle.putSerializable("lastGame", listGames[game_id])
        intent.putExtras(bundle)
        resultLauncher.launch(intent)
        return true
    }


    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { res ->
        if (res.resultCode == Request.ADDGAME.value) {
            val b: Bundle = res.data?.extras!!
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
            if (listGames.size > 0) {
                newScore = listGames.last().score.toMutableList()
                game_id = listGames.last().gameId + 1
            } else {
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
            } else {
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

        if (res.resultCode == Request.EDITGAME.value) {
            val b: Bundle = res.data?.extras!!
            val preneur = b.getInt("preneur")
            val contrat = b.getString("contrat")!!
            val result = b.getBoolean("result")
            val ecart = b.getInt("ecart")
            val bonus = b.getInt("bonus")

            val g: GameTarot
            if (names.size == 4)
                g = listGames[b.getInt("game_id")] as GameTarot
            else {
                g = listGames[b.getInt("game_id")] as GameTarot5
                g.teammate = b.getInt("appel")
            }
            g.playerTake = preneur
            g.contract = contrat
            g.success = result
            g.difference = ecart
            g.bonus = bonus


            val newScore: MutableList<Int>
            if (names.size == 4) newScore = mutableListOf<Int>(0, 0, 0, 0)
            else newScore = mutableListOf<Int>(0, 0, 0, 0, 0)
            for (game_id in 0..listGames.size - 1) {
                var gi: GameTarot = listGames[game_id] as GameTarot
                val valContrat = contrats[gi.contract]
                var toAdd = valContrat?.plus(gi.difference)
                if (!gi.success) toAdd = -toAdd!!
                if (names.size == 4) {
                    for (i in 0..3) {
                        if (i == gi.playerTake) newScore[i] = newScore[i] + 3 * toAdd!!
                        else newScore[i] = newScore[i] - toAdd!!

                        if (gi.bonus != -1) // -1 means no bonus
                        {
                            if (i == gi.bonus) newScore[i] = newScore[i] + 30
                            else newScore[i] = newScore[i] - 10
                        }
                    }
                } else {
                    gi = listGames[game_id] as GameTarot5
                    val appel_i = gi.teammate
                    val preneur_i = gi.playerTake
                    for (i in 0..4) {
                        if (appel_i == preneur_i) {
                            if (i == preneur_i) newScore[i] = newScore[i] + 4 * toAdd!!
                            else newScore[i] = newScore[i] - toAdd!!
                        } else {
                            if (i == preneur_i) newScore[i] = newScore[i] + 2 * toAdd!!
                            else if (i == appel_i) newScore[i] = newScore[i] + toAdd!!
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
