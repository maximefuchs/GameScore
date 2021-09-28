package com.example.gamescore.belote

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.gamescore.AddGameActivity
import com.example.gamescore.R
import com.example.gamescore.Request
import kotlinx.android.synthetic.main.activity_add_game_belote.*
import kotlinx.android.synthetic.main.fragment_score.view.*
import java.util.ArrayList

class AddGameBeloteActivity : AddGameActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_game_belote)

        var gameId = 0
        winBtn = btnWin
        loseBtn = btnLose

        val b = intent.extras!!
        val isCoinchee = b.getBoolean("isCoinchee")
        val contracts = resources.getStringArray(R.array.coinchee)
        if (isCoinchee) {
            val adapterContract = ArrayAdapter(this, R.layout.spinner_item, contracts)
            spinner_contrat.adapter = adapterContract
            LL_contrat_coinchee.visibility = View.VISIBLE
            LL_isCoinchee.visibility = View.VISIBLE
            TV_bonus.visibility = View.GONE
            LL_bonus.visibility = View.GONE
        }
        val players = b.getStringArrayList("players") as ArrayList<String>
        var teams = ArrayList<String>()
        if (players.size == 3) {
            teams = players
            LL_OandD.visibility = View.VISIBLE
        } else { // 4 players
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
        val nums: Array<String> = Array(51) { i: Int -> "${i * 10}" }
        bonus_T1.value = 0
        bonus_T1.minValue = 0
        bonus_T1.maxValue = nums.size - 1
        bonus_T1.wrapSelectorWheel = false
        bonus_T1.displayedValues = nums
        bonus_T2.value = 0
        bonus_T2.minValue = 0
        bonus_T2.maxValue = nums.size - 1
        bonus_T2.wrapSelectorWheel = false
        bonus_T2.displayedValues = nums

        scoreT1.setOnKeyListener(getViewOnclickListener(true))
        scoreT2.setOnKeyListener(getViewOnclickListener(false))

        var partyWon = changeResult(true)
        btnLose.setOnClickListener {
            partyWon = changeResult(false)
            if (players.size == 3) {
                scoreT1.setText("0")
                scoreT2.setText(R.string.topScoreBelote)
                return@setOnClickListener
            }
            // 4 players
            if (spinner_preneur.selectedItemPosition == 0) {
                scoreT1.setText("0")
                scoreT2.setText(R.string.topScoreBelote)
            } else {
                scoreT2.setText("0")
                scoreT1.setText(R.string.topScoreBelote)
            }
        }
        btnWin.setOnClickListener { partyWon = changeResult(true) }


        val edit = b.getBoolean("edit")
        if (edit) {
            var game: GameBelote
            if (isCoinchee) {
                game = b.getSerializable("lastGame") as GameBeloteCoinchee
                spinner_contrat.setSelection(contracts.indexOf(game.contrat.toString()))
                CBisCoinchee.isChecked = game.coinchee
                bonus_T1.value = 0
                bonus_T2.value = 0
            } else {
                game = b.getSerializable("lastGame") as GameBelote
                if (game.nb_players == 3)
                    game = b.getSerializable("lastGame") as GameBelote3
                bonus_T1.value = game.bonusTeam1 / 10
                bonus_T2.value = game.bonusTeam2 / 10
            }
            val calculus =
                if (game.difference == 250) 250 else (game.difference + 162) / 2 // ! if capot
            if (players.size == 3) {
                if (game.success) {
                    scoreT1.setText(calculus.toString())
                    scoreT2.setText(if (calculus == 250) "0" else (162 - calculus).toString())
                } else {
                    scoreT1.setText("0")
                    scoreT2.setText(if (calculus == 250) "250" else "162")
                }
            } else { // 4 players
                val scoreTX = if (game.taker == 0) scoreT1 else scoreT2
                val scoreTX_ = if (game.taker == 0) scoreT2 else scoreT1
                if (game.success) {
                    scoreTX.setText(calculus.toString())
                    scoreTX_.setText(if (calculus == 250) "0" else (162 - calculus).toString())
                } else {
                    scoreTX.setText("0")
                    scoreTX_.setText(if (calculus == 250) "250" else "162")
                }
            }
            spinner_preneur.setSelection(game.taker)
            partyWon = changeResult(game.success)

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
                if (players.size == 3)
                    return // no change for 3 players
                if (!partyWon) {
                    if (position == 0) {
                        scoreT1.setText("0")
                        scoreT2.setText(R.string.topScoreBelote)
                    } else {
                        scoreT2.setText("0")
                        scoreT1.setText(R.string.topScoreBelote)
                    }
                }
            }
        }

        btnValider.setOnClickListener {
            // score string null
            if (scoreT1.text.toString() == "" || scoreT2.text.toString() == "") {
                score_warning.setText(R.string.hint_fill_score)
                score_warning.visibility = View.VISIBLE
                return@setOnClickListener
            }
            // losing team score higher than winners
            val taker = spinner_preneur.selectedItemPosition
            var valueScoreT1 =
                if (scoreT1.text.toString() == "") 0 else scoreT1.text.toString().toInt()
            var valueScoreT2 =
                if (scoreT2.text.toString() == "") 0 else scoreT2.text.toString().toInt()
            val scoreSumError =
                (valueScoreT1 + valueScoreT2 != 162 && valueScoreT1 + valueScoreT2 != 250)
            val scoreOutOfBounds =
                (valueScoreT1 < 0 || valueScoreT2 < 0 || valueScoreT1 > 250 || valueScoreT2 > 250
                        || (valueScoreT1 in 163..249)
                        || (valueScoreT2 in 163..249))

            val ecart = if (players.size == 3)
                valueScoreT1 - valueScoreT2
            else
                if (taker == 0) valueScoreT1 - valueScoreT2 else valueScoreT2 - valueScoreT1
            valueScoreT1 += bonus_T1.value * 10
            valueScoreT2 += bonus_T2.value * 10
            val scoreWinnerError = if (isCoinchee) {
                    // will be set to true if team on offense has less than defense or less than contrat and game is set to be won by offense
                ((taker == 0 && partyWon && (valueScoreT2 >= valueScoreT1 || valueScoreT1 < spinner_contrat.selectedItem.toString().toInt() )) ||
                        (taker == 1 && partyWon && (valueScoreT1 >= valueScoreT2 || valueScoreT2 < spinner_contrat.selectedItem.toString().toInt() )))
            } else
                if (players.size == 3)
                    partyWon && valueScoreT1 <= valueScoreT2
                else
                    ((taker == 0 && partyWon && valueScoreT2 >= valueScoreT1) || (taker == 1 && partyWon && valueScoreT1 >= valueScoreT2))

            if (scoreSumError || scoreOutOfBounds || scoreWinnerError) {
                score_warning.setText(R.string.score_invalid)
                score_warning.visibility = View.VISIBLE
                return@setOnClickListener
            }

            val intent = Intent()
            intent.putExtra("preneur", taker)
            intent.putExtra("bonusT1", bonus_T1.value * 10)
            intent.putExtra("bonusT2", bonus_T2.value * 10)
            if (isCoinchee) {
                intent.putExtra("contrat", spinner_contrat.selectedItem.toString().toInt())
                intent.putExtra("coinchee", CBisCoinchee.isChecked)
            }
            intent.putExtra("result", partyWon)

            intent.putExtra("ecart", ecart)
            if (edit)
                intent.putExtra("game_id", gameId)
            setResult(if (edit) Request.EDITGAME.value else Request.ADDGAME.value, intent)
            finish()
        }
    }

    private fun getViewOnclickListener(isT1: Boolean): View.OnKeyListener {
        return View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                updateScore(isT1)
                hideKeyBoard()
                scoreT1.clearFocus()
                scoreT2.clearFocus()
                return@OnKeyListener true
            }
            false
        }
    }

    private fun updateScore(isT1: Boolean) {
        val scoreString: String
        var scoreForTX: Int
        val scoreTX = if (isT1) scoreT1 else scoreT2
        val scoreTXX = if (isT1) scoreT2 else scoreT1
        scoreString = scoreTX.text.toString()
        if (scoreString != "" && scoreTXX.text.toString() != "") {
            if (scoreString.toInt() + scoreTXX.text.toString().toInt() == 162
                || scoreString.toInt() + scoreTXX.text.toString().toInt() == 250
            )
                return
        }
        scoreForTX = when (scoreString) {
            "" -> 162
            "250" -> 0
            else -> (162 - scoreString.toInt())
        }
        if (scoreForTX < 0) scoreForTX = 0
        scoreTXX.setText(scoreForTX.toString())
    }

}