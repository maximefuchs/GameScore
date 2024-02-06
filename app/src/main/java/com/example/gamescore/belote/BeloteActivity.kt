package com.example.gamescore.belote

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.example.gamescore.*
import com.example.gamescore.tarot.GameTarot
import kotlinx.android.synthetic.main.activity_game.*
import kotlin.math.abs
import java.util.ArrayList

enum class TypeGame {
    NORMALE,
    COINCHEE
}

class BeloteActivity : GameActivity() {

    var gameType = TypeGame.NORMALE
    val stateType = "type"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        FrameTitle.text = resources.getText(R.string.game_belote)

        if (savedInstanceState != null) {
            if (nbPlayers == 4)
                gameType = savedInstanceState.getSerializable(stateType) as TypeGame
            fragmentTransition(R.id.container, ScoreBeloteFragment())
        } else {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE)
            val gameClass = sharedPreferences.getString("type", null)
            val enumGameType = gameClass?.let { enumValueOf<TypeGameSaved>(it) }


            if (enumGameType == TypeGameSaved.BELOTE || enumGameType == TypeGameSaved.BELOTE_COINCHEE) {
                if (enumGameType == TypeGameSaved.BELOTE_COINCHEE)
                    gameType = TypeGame.COINCHEE
                val numberOfPlayers = sharedPreferences.getInt("numberOfPlayers", 4)
                val score =
                    if (numberOfPlayers == 3) intArrayOf(0, 0, 0) else intArrayOf(0, 0, 0, 0)
                val listNames = arrayListOf<String>()
                for (i in 0 until numberOfPlayers) {
                    score[i] = sharedPreferences.getInt("playerScore_$i", 0)
                    sharedPreferences.getString("Name_$i", i.toString())
                        ?.let { listNames.add(it) }
                }
                names = listNames
                nbPlayers = numberOfPlayers

                btnNoSaved.setOnClickListener {
                    RLsaved.visibility = View.GONE
                    fragmentTransition(R.id.container, NbPlayersBeloteFragment())
                    add_game.setOnClickListener { addBeloteGame() }
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

                    fragmentTransition(R.id.container, ScoreBeloteFragment())
                    showHelpButton()
                }
                RLsaved.visibility = View.VISIBLE
            } else {
                fragmentTransition(R.id.container, NbPlayersBeloteFragment())
            }
        }

        add_game.setOnClickListener { addBeloteGame() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState) // save names and games
        outState.putSerializable(stateType, gameType)
    }

    fun getTypeOfGame() {
        fragmentTransition(R.id.container, TypeBeloteFragment())
    }

    fun typeOfGame(game_of_type: TypeGame) {
        gameType = game_of_type
        getName(4)
    }

    fun getName(nbOfPlayers: Int) {
        nbPlayers = nbOfPlayers
        fragmentTransition(R.id.container, NameFragmentBelote())
    }

    override fun startGame(list_names: ArrayList<String>) {
        super.startGame(list_names)
        fragmentTransition(R.id.container, ScoreBeloteFragment())
    }

    override fun showHelpButton() {
        if (nbPlayers == 3) {
            helpText = getString(R.string.belote_rules_3players)
            helpText2 = getString(R.string.belote_game_count)
        } else {
            helpText = getString(R.string.belote_game_count)
            helpText2 = if (gameType == TypeGame.COINCHEE)
                getString(R.string.belote_info_annonce_coinchee)
            else
                getString(R.string.belote_info_annonce_normale)
        }
        super.showHelpButton()
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

    override fun editGame(game_id: Int): Boolean {
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
                val playerTakeId = b.getInt("preneur")
                val result = b.getBoolean("result")
                val difference = b.getInt("ecart")
                val bonusT1 = b.getInt("bonusT1")
                val bonusT2 = b.getInt("bonusT2")

                val g: GameBelote
                val gameId: Int
                val previousScore: IntArray
                if (listGames.size > 0) {
                    previousScore = listGames.last().score
                    gameId = listGames.last().gameId + 1
                } else {
                    gameId = 0
                    previousScore =
                        if (nbPlayers == 3) intArrayOf(0, 0, 0) else intArrayOf(0, 0)
                }

                if (nbPlayers == 4) {
                    if (gameType == TypeGame.COINCHEE) {
                        val contract = b.getInt("contrat")
                        val isCoinchee = b.getBoolean("coinchee")
                        g = GameBeloteCoinchee(
                            gameId,
                            playerTakeId,
                            result,
                            contract,
                            difference,
                            bonusT1,
                            bonusT2,
                            isCoinchee
                        )
                    } else
                        g = GameBelote(
                            gameId,
                            playerTakeId,
                            result,
                            difference,
                            bonusT1,
                            bonusT2
                        )
                } else
                    g = GameBelote3(
                        gameId,
                        playerTakeId,
                        result,
                        difference,
                        bonusT1,
                        bonusT2
                    )
                g.updateScore(previousScore)
                listGames.add(g)

                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ScoreBeloteFragment()).commit()
            }

            if (res.resultCode == Request.EDITGAME.value) {
                val b: Bundle = res.data?.extras!!
                val taker = b.getInt("preneur")
                val result = b.getBoolean("result")
                val difference = b.getInt("ecart")
                val bonusT1 = b.getInt("bonusT1")
                val bonusT2 = b.getInt("bonusT2")

                val g: GameBelote
                if (nbPlayers == 4) {
                    if (gameType == TypeGame.NORMALE)
                        g = listGames[b.getInt("game_id")] as GameBelote
                    else { // coinchee
                        g = listGames[b.getInt("game_id")] as GameBeloteCoinchee
                        g.contract = b.getInt("contrat")
                        g.coinchee = b.getBoolean("coinchee")
                    }
                } else
                    g = listGames[b.getInt("game_id")] as GameBelote3
                g.takerId = taker
                g.success = result
                g.difference = difference
                g.bonusTeam1 = bonusT1
                g.bonusTeam2 = bonusT2


                var previousScore =
                    if (nbPlayers == 4) intArrayOf(0, 0) else intArrayOf(0, 0, 0)
                for (game in listGames) {
                    if (!game.restart)
                        (game as GameBelote).updateScore(previousScore)
                    previousScore = game.score
                }

                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ScoreBeloteFragment()).commit()
            }
        }
}