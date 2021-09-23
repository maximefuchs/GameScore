package com.example.gamescore.belote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.example.gamescore.R
import com.example.gamescore.Request
import com.example.gamescore.tarot.GameTarot
import com.example.gamescore.tarot.GameTarot5
import kotlinx.android.synthetic.main.activity_add_game_belote.*
import kotlinx.android.synthetic.main.activity_add_game_belote.btnLose
import kotlinx.android.synthetic.main.activity_add_game_belote.btnValider
import kotlinx.android.synthetic.main.activity_add_game_belote.btnWin
import kotlinx.android.synthetic.main.activity_add_game_belote.spinner_contrat
import kotlinx.android.synthetic.main.activity_add_game_belote.spinner_preneur
import kotlinx.android.synthetic.main.activity_add_game_tarot.*
import kotlinx.android.synthetic.main.fragment_score.view.*
import java.util.ArrayList

class AddGameBeloteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_game_belote)

        var gameId: Int = 0

        val b = intent.extras!!
        val isCoinchee = b.getBoolean("isCoinchee")
        val contracts = resources.getStringArray(R.array.coinchee)
        if (isCoinchee) {
            val adapterContract = ArrayAdapter(this, R.layout.spinner_item, contracts)
            spinner_contrat.adapter = adapterContract
            contrat_coinchee.visibility = View.VISIBLE
        }
        val players = b.getStringArrayList("players") as ArrayList<String>
        var teams = ArrayList<String>()
        if (players.size == 3) {
            teams = players
            LL_OandD.visibility = View.VISIBLE
        }
        else { // 4 players
            var placeholder: String = resources.getText(R.string.placeholder_team).toString()
            placeholder = placeholder.format(players[0].take(2), players[1].take(2))
            scoreT1.hint = placeholder
            teams.add(placeholder)
            placeholder = resources.getText(R.string.placeholder_team).toString()
            placeholder = placeholder.format(players[2].take(2), players[3].take(2))
            scoreT2.hint = placeholder
            teams.add(placeholder)
        }

        val adapterPreneur = ArrayAdapter(this, R.layout.spinner_item, teams)
        spinner_preneur.adapter = adapterPreneur

        // set step of 10 of bonus picker
        bonus_T1.setFormatter { (it * 10).toString() }
        bonus_T2.setFormatter { (it * 10).toString() }
        bonus_T1.minValue = 0
        bonus_T2.minValue = 0


        scoreT1.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                val scoreForT2 = (162 - scoreT1.text.toString().toInt())
                scoreT2.setText(scoreForT2.toString())
                return@OnKeyListener true
            }
            false
        })
        scoreT2.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                val scoreForT1 = (162 - scoreT1.text.toString().toInt())
                scoreT1.setText(scoreForT1.toString())
                return@OnKeyListener true
            }
            false
        })

        var party_is_won = changeResult(true)
        btnLose.setOnClickListener {
            party_is_won = changeResult(false)
            if(spinner_preneur.selectedItemPosition == 0){
                scoreT1.setText("0")
                scoreT2.setText("162")
            } else {
                scoreT2.setText("0")
                scoreT1.setText("162")
            }
        }
        btnWin.setOnClickListener { party_is_won = changeResult(true) }


        val edit = b.getBoolean("edit")
        if (edit) {
            var game: GameBelote
            if (isCoinchee) {
                game = b.getSerializable("lastGame") as GameBeloteCoinchee
                spinner_contrat.setSelection(contracts.indexOf(game.contrat.toString()))
            } else {
                game = b.getSerializable("lastGame") as GameBelote
                if (game.nb_players == 3)
                    game = b.getSerializable("lastGame") as GameBelote3
            }
            bonus_T1.value = game.bonusTeam1
            bonus_T2.value = game.bonusTeam2
            val calculus =
                if (game.difference == 250) 250 else (game.difference + 162) / 2 // ! if capot
            if (game.taker == 0) {
                // team left on the scoreboard
                if (game.success) {
                    scoreT1.setText(calculus.toString())
                    scoreT2.setText(if (calculus == 250) "0" else (162 - calculus).toString())
                }
            } else {
                if (game.success) {
                    scoreT2.setText(calculus.toString())
                    scoreT1.setText(if (calculus == 250) "0" else (162 - calculus).toString())
                }
            }
            spinner_preneur.setSelection(game.taker)
            party_is_won = changeResult(game.success)

            gameId = game.game_id
        }

        spinner_preneur.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(!party_is_won){
                    if(position == 0){
                        scoreT1.setText("0")
                        scoreT2.setText("162")
                    } else {
                        scoreT2.setText("0")
                        scoreT1.setText("162")
                    }
                }
            }
        }

        btnValider.setOnClickListener {
            val intent = Intent()
            intent.putExtra("preneur", spinner_preneur.selectedItemPosition)
            intent.putExtra("bonusT1", bonus_T1.value)
            intent.putExtra("bonusT2", bonus_T2.value)
            if (isCoinchee)
                intent.putExtra("contrat", spinner_contrat.selectedItem.toString())
            intent.putExtra("result", party_is_won)
            val valueScoreT1 = scoreT1.text.toString().toInt()
            val valueScoreT2 = scoreT2.text.toString().toInt()
            intent.putExtra(
                "ecart",
                if (valueScoreT1 > valueScoreT2) valueScoreT1 - valueScoreT2 else valueScoreT2 - valueScoreT1
            )
            if (edit)
                intent.putExtra("game_id", gameId)
            setResult(if (edit) Request.EDITGAME.value else Request.ADDGAME.value, intent)
            finish()
        }
    }

    private fun changeResult(is_won: Boolean): Boolean {
        btnLose.setBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                if (is_won) R.color.colorPrimaryLight else R.color.colorDefeat
            )
        )
        btnWin.setBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                if (is_won) R.color.colorVictory else R.color.colorPrimaryLight
            )
        )
        return is_won;
    }
}