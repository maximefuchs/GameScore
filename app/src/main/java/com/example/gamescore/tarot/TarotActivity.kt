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
    private var contrats: HashMap<String, Int> = hashMapOf(
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

        // If we have a saved state then we can restore it now
        if (savedInstanceState != null) {
            names = savedInstanceState.getSerializable(stateNames) as ArrayList<String>
            listGames = savedInstanceState.getSerializable(stateGames) as ArrayList<Game>
            nbPlayers = names.size
            startSavedGame()
        } else {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE)
            val gameId = sharedPreferences.getInt("id", -1)

            if (gameId != -1) {
                val numberOfPlayers = sharedPreferences.getInt("numberOfPlayers", 4)
                val score = mutableListOf<Int>()
                val listNames = arrayListOf<String>()
                for (i in 0 until numberOfPlayers) {
                    score.add(sharedPreferences.getInt("playerScore_$i", 0))
                    sharedPreferences.getString("Name_$i", i.toString())?.let { listNames.add(it) }
                }
                names = listNames

                btnNoSaved.setOnClickListener {
                    RLsaved.visibility = View.GONE
                    fragmentTransition(R.id.container, NbPlayersTarotFragment())
                    add_game.setOnClickListener { addTarotGame() }
                }
                btnSaved.setOnClickListener {
                    RLsaved.visibility = View.GONE
                    val game = Game()
                    game.gameId = gameId
                    game.nbPlayers = numberOfPlayers
                    game.score = score
                    game.restart = true
                    listGames = ArrayList<Game>()
                    listGames.add(game)
                    // TODO: check between parent class variables and variables passed from activity to fragment
                    fragmentTransition(R.id.container, ScoreTarotFragment())
                }
                RLsaved.visibility = View.VISIBLE
            } else {
                fragmentTransition(R.id.container, NbPlayersTarotFragment())
            }
        }

        add_game.setOnClickListener { addTarotGame() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // save state
        outState.putSerializable(stateNames, names)
        outState.putSerializable(stateGames, listGames)
    }

    fun getName(nb_players: Int) {
        nbPlayers = nb_players
        fragmentTransition(R.id.container, NameFragmentTarot())
    }

    fun startGame(list_names: ArrayList<String>) {
        hideKeyBoard()
        names = list_names
        listGames = arrayListOf()
        startSavedGame()
    }

    private fun startSavedGame() {
        fragmentTransition(R.id.container, ScoreTarotFragment())
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


    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
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
                var gameId = 0
                val newScore: MutableList<Int>
                if (listGames.size > 0) {
                    newScore = listGames.last().score.toMutableList()
                    gameId = listGames.last().gameId + 1
                } else {
                    newScore = if (names.size == 4) mutableListOf(0, 0, 0, 0)
                    else mutableListOf(0, 0, 0, 0, 0)
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
                        gameId,
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
                            when (i) {
                                preneur -> newScore[i] = newScore[i] + 2 * toAdd
                                appel -> newScore[i] = newScore[i] + toAdd
                                else -> newScore[i] = newScore[i] - toAdd
                            }
                        }

                        if (bonus != -1) // -1 means no bonus
                        {
                            if (i == bonus) newScore[i] = newScore[i] + 40
                            else newScore[i] = newScore[i] - 10
                        }
                    }
                    g = GameTarot5(
                        gameId,
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
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ScoreTarotFragment()).commit()
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


                val newScore = if (names.size == 4) mutableListOf(0, 0, 0, 0)
                else mutableListOf(0, 0, 0, 0, 0)
                for (game_id in 0 until listGames.size) {
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
                        val appelI = gi.teammate
                        val preneurI = gi.playerTake
                        for (i in 0..4) {
                            if (appelI == preneurI) {
                                if (i == preneurI) newScore[i] = newScore[i] + 4 * toAdd!!
                                else newScore[i] = newScore[i] - toAdd!!
                            } else {
                                when (i) {
                                    preneurI -> newScore[i] = newScore[i] + 2 * toAdd!!
                                    appelI -> newScore[i] = newScore[i] + toAdd!!
                                    else -> newScore[i] = newScore[i] - toAdd!!
                                }
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

                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ScoreTarotFragment()).commit()

            }
        }
}
