package com.example.gamescore.tarot

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.example.gamescore.AddGameActivity
import com.example.gamescore.R
import com.example.gamescore.Request
import kotlinx.android.synthetic.main.activity_add_game_tarot.*
import java.util.*

class AddGameTarotActivity : AddGameActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_game_tarot)
        var game : GameTarot
        var gameId = 0

        winBtn = btnWin
        loseBtn = btnLose

        val b = intent.extras!!
        val players = b.getStringArrayList("players") as ArrayList<String>
        val edit = b.getBoolean("edit")


        if (players.size == 4)
            llcalled.visibility = View.GONE

        val context = this
        val adapterPreneur = ArrayAdapter(this, R.layout.spinner_item, players)
        spinner_preneur.adapter = adapterPreneur

        if (players.size == 5) {
            var updatedPlayers = players.clone() as ArrayList<String>
            updatedPlayers[0] = context.getString(R.string.player_alone)

            val adapterCalled = ArrayAdapter(context, R.layout.spinner_item, updatedPlayers)
            spinner_called.adapter = adapterCalled

            spinner_preneur.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    updatedPlayers = players.clone() as ArrayList<String>
                    updatedPlayers[position] = context.getString(R.string.player_alone)
                    spinner_called.adapter =
                        ArrayAdapter(context, R.layout.spinner_item, updatedPlayers)

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // nothing, but function needs to be override
                }
            }
        }

        val playersNone = ArrayList(players.map { it }) // create copy
        playersNone.add(0, " - ")
        val adapterBonus = ArrayAdapter(this, R.layout.spinner_item, playersNone)
        spinner_bonus.adapter = adapterBonus

        val contracts = resources.getStringArray(R.array.contrats)
        val adapterContrat = ArrayAdapter(this, R.layout.spinner_item, contracts)
        spinner_contrat.adapter = adapterContrat

        var ecartScore = changeEcart(0)
        btn0.setOnClickListener { ecartScore = changeEcart(0) }
        btn10.setOnClickListener { ecartScore = changeEcart(10) }
        btn20.setOnClickListener { ecartScore = changeEcart(20) }
        btn30.setOnClickListener { ecartScore = changeEcart(30) }

        var partyIsWon = changeResult(true)
        btnLose.setOnClickListener { partyIsWon = changeResult(false, ecartScore) }
        btnWin.setOnClickListener { partyIsWon = changeResult(true, ecartScore) }

        if (edit)
        {
            game = b.getSerializable("lastGame") as GameTarot
            if (game.nbPlayers == 5){
                game = b.getSerializable("lastGame") as GameTarot5
                spinner_called.setSelection(game.teammate)
            }
            spinner_preneur.setSelection(game.playerTake)
            spinner_contrat.setSelection(contracts.indexOf(game.contract))
            ecartScore = changeEcart(game.difference)
            partyIsWon = changeResult(game.success)
            spinner_bonus.setSelection(game.bonus + 1)
            gameId = game.gameId
        }


        btnValider.setOnClickListener {
            val intent = Intent()
            intent.putExtra("preneur",spinner_preneur.selectedItemPosition)
            if (spinner_bonus.selectedItemPosition != 0)
                intent.putExtra("bonus",spinner_bonus.selectedItemPosition)
            else // no bonus
                intent.putExtra("bonus", -1)
            if (players.size == 5)
                intent.putExtra("appel",spinner_called.selectedItemPosition)
            intent.putExtra("contrat", spinner_contrat.selectedItem.toString())
            intent.putExtra("result", partyIsWon)
            intent.putExtra("ecart", ecartScore)
            if(edit)
                intent.putExtra("game_id",gameId)
            setResult(if(edit) Request.EDITGAME.value else Request.ADDGAME.value, intent)
            finish()
        }

    }

    private fun changeEcart(score_id: Int): Int {
        btn0.setBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                if (score_id == 0) colorForScore else R.color.colorPrimaryLight
            )
        )
        btn10.setBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                if (score_id == 10) colorForScore else R.color.colorPrimaryLight
            )
        )
        btn20.setBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                if (score_id == 20) colorForScore else R.color.colorPrimaryLight
            )
        )
        btn30.setBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                if (score_id == 30) colorForScore else R.color.colorPrimaryLight
            )
        )
        return score_id
    }

    private fun changeResult(is_won: Boolean, score_id: Int): Boolean {
        this.changeResult(is_won)
        var button = if (score_id == 0) btn0 else if (score_id == 10) btn10 else if (score_id == 20) btn20 else btn30
        button.setBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                colorForScore
            )
        )
        return is_won
    }
}