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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        FrameTitle.text = resources.getText(R.string.game_tarot)

        // If we have a saved state then we can restore it now
        if (savedInstanceState != null) {
            startSavedGame()
        } else {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE)
            val gameType = sharedPreferences.getString("type", null)
            val enumGameType = gameType?.let { enumValueOf<TypeGameSaved>(it) }

            if (enumGameType == TypeGameSaved.TAROT) {
                val numberOfPlayers = sharedPreferences.getInt("numberOfPlayers", 4)
                val score = if( numberOfPlayers == 4) intArrayOf(0,0,0,0) else intArrayOf(0,0,0,0,0)
                val listNames = arrayListOf<String>()
                for (i in 0 until numberOfPlayers) {
                    score[i] = sharedPreferences.getInt("playerScore_$i", 0)
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
                    game.gameId = 0
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

    fun getName(nb_players: Int) {
        nbPlayers = nb_players
        fragmentTransition(R.id.container, NameFragmentTarot())
    }

    fun startGame(list_names: ArrayList<String>) {
        hideKeyBoard()
        names = list_names
        listGames = arrayListOf()
        fragmentTransition(R.id.container, ScoreTarotFragment())
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
            val b: Bundle = res.data?.extras!!
            val preneur = b.getInt("preneur")
            val contrat = b.getString("contrat")!!
            val result = b.getBoolean("result")
            val ecart = b.getInt("ecart")
            val bonusNames = b.getIntArray("list_bonus_names")!!
            val bonusValues = b.getIntArray("list_bonus_values")!!


            if (res.resultCode == Request.ADDGAME.value) {
                val g: GameTarot
                var gameId = 0
                val previousScore: IntArray
                if (listGames.size > 0) {
                    previousScore = listGames.last().score
                    gameId = listGames.last().gameId + 1
                } else {
                    previousScore = if (names.size == 4) intArrayOf(0, 0, 0, 0)
                    else intArrayOf(0, 0, 0, 0, 0)
                }
                if (names.size == 4) {
                    g = GameTarot(
                        gameId,
                        preneur,
                        contrat,
                        result,
                        ecart,
                        bonusNames,
                        bonusValues
                    )
                } else {
                    val appel = b.getInt("appel")
                    g = GameTarot5(
                        gameId,
                        preneur,
                        contrat,
                        appel,
                        result,
                        ecart,
                        bonusNames,
                        bonusValues
                    )
                }
                g.updateScore(previousScore)
                listGames.add(g)

//                Log.w("size", listGames.size.toString())
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ScoreTarotFragment()).commit()
            }

            if (res.resultCode == Request.EDITGAME.value) {
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
                g.bonusName = bonusNames
                g.bonusValue = bonusValues


                var previousScore = if (names.size == 4) intArrayOf(0, 0, 0, 0)
                else intArrayOf(0, 0, 0, 0, 0)
                for (game_id in 0 until listGames.size) {
                    val gi: GameTarot = listGames[game_id] as GameTarot
                    gi.updateScore(previousScore)
                    previousScore = gi.score
                }

                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ScoreTarotFragment()).commit()

            }
        }
}
