package com.example.gamescore.tarot

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.view.allViews
import com.example.gamescore.AddGameActivity
import com.example.gamescore.R
import com.example.gamescore.Request
import kotlinx.android.synthetic.main.activity_add_game_tarot.*
import kotlinx.android.synthetic.main.bonus_layout.view.*
import java.util.*
import kotlin.collections.ArrayList

class AddGameTarotActivity : AddGameActivity() {
    private var bonuses: HashMap<String, Int> = hashMapOf(
        "Petit au bout" to 10,
        "Poignée" to 10,
        "Double Poignée" to 20,
        "Triple Poignée" to 30,
        "Misère" to 10,
        "Double Misère" to 20
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_game_tarot)
        var game: GameTarot
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

        // Inflate the dynamic layout
        val inflater = LayoutInflater.from(this)
        val dynamicLayout = inflater.inflate(R.layout.bonus_layout, null)
        btnAddBonus.setOnClickListener {

            val playersName = ArrayList(players.map { it }) // create copy
            val adapterBonusName = ArrayAdapter(this, R.layout.spinner_item, playersName)
            dynamicLayout.spinner_bonus_name.adapter = adapterBonusName

            val bonuses = resources.getStringArray(R.array.bonuses)
            val adapterBonusValue = ArrayAdapter(this, R.layout.spinner_item, bonuses)
            dynamicLayout.spinner_bonus_value.adapter = adapterBonusValue

            dynamicLayout.btn_remove.setOnClickListener {
                LL_bonus.removeView(dynamicLayout)
            }

            // Add the dynamic layout to the parent layout
            LL_bonus.addView(dynamicLayout)
        }

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

        if (edit) {
            game = b.getSerializable("lastGame") as GameTarot
            if (game.nbPlayers == 5) {
                game = b.getSerializable("lastGame") as GameTarot5
                spinner_called.setSelection(game.teammate)
            }
            spinner_preneur.setSelection(game.playerTake)
            spinner_contrat.setSelection(contracts.indexOf(game.contract))
            ecartScore = changeEcart(game.difference)
            partyIsWon = changeResult(game.success)
            for (index in 0 until game.bonusName.size) {
                val playersName = ArrayList(players.map { it }) // create copy
                val adapterBonusName = ArrayAdapter(this, R.layout.spinner_item, playersName)
                dynamicLayout.spinner_bonus_name.adapter = adapterBonusName
                dynamicLayout.spinner_bonus_name.setSelection(game.bonusName[index])

                val bonuses = resources.getStringArray(R.array.bonuses)
                val adapterBonusValue = ArrayAdapter(this, R.layout.spinner_item, bonuses)
                dynamicLayout.spinner_bonus_value.adapter = adapterBonusValue
                dynamicLayout.spinner_bonus_value.setSelection(game.bonusName[index])

                dynamicLayout.btn_remove.setOnClickListener {
                    LL_bonus.removeView(dynamicLayout)
                }

                // Add the dynamic layout to the parent layout
                LL_bonus.addView(dynamicLayout)
            }
            gameId = game.gameId
        }


        btnValider.setOnClickListener {
            val intent = Intent()
            intent.putExtra("preneur", spinner_preneur.selectedItemPosition)
            val listNames = mutableListOf<Int>()
            val listValues = mutableListOf<Int>()
            for (view in LL_bonus.allViews) {
                val positionName: Int = view.spinner_bonus_name.selectedItemPosition
                val positionValue: String = view.spinner_bonus_value.selectedItem as String
                val bonusValue = bonuses[positionValue]
                listNames.add(positionName)
                listValues.add(bonusValue!!)
            }
            intent.putExtra("list_bonus_names", ArrayList(listNames))
            intent.putExtra("list_bonus_values", ArrayList(listValues))

            if (players.size == 5)
                intent.putExtra("appel", spinner_called.selectedItemPosition)
            intent.putExtra("contrat", spinner_contrat.selectedItem.toString())
            intent.putExtra("result", partyIsWon)
            intent.putExtra("ecart", ecartScore)
            if (edit)
                intent.putExtra("game_id", gameId)
            setResult(if (edit) Request.EDITGAME.value else Request.ADDGAME.value, intent)
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
        val button =
            if (score_id == 0) btn0 else if (score_id == 10) btn10 else if (score_id == 20) btn20 else btn30
        button.setBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                colorForScore
            )
        )
        return is_won
    }
}