package com.example.gamescore.tarot

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.example.gamescore.AddGameActivity
import com.example.gamescore.R
import com.example.gamescore.Request
import kotlinx.android.synthetic.main.activity_add_game_tarot.*
import kotlinx.android.synthetic.main.bonus_layout.view.*
import java.util.*
import kotlin.collections.ArrayList

class AddGameTarotActivity : AddGameActivity() {

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



        val context = this
        val adapterPreneur = ArrayAdapter(this, R.layout.spinner_item, players)
        spinner_preneur.adapter = adapterPreneur

        if (players.size == 4)
            llcalled.visibility = View.GONE
        else { // 5 players
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
                    val previousPosition = spinner_called.selectedItemPosition // to keep the previous position on the spinner
                    // this is useful is case we are editing a game.
                    // Otherwise, when we change the spinner_preneur, the spinner_called will change too
                    updatedPlayers = players.clone() as ArrayList<String>
                    updatedPlayers[position] = context.getString(R.string.player_alone)
                    spinner_called.adapter =
                        ArrayAdapter(context, R.layout.spinner_item, updatedPlayers)
                    spinner_called.setSelection(previousPosition)

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // nothing, but function needs to be override
                }
            }
        }

        // Inflate the dynamic layout
        val inflater = LayoutInflater.from(this)
        btnAddBonus.setOnClickListener {

            val dynamicLayout = inflater.inflate(R.layout.bonus_layout, null)
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
//            if (dynamicLayout.parent != null)
//                (dynamicLayout.parent as ViewGroup).removeView(dynamicLayout)
            LL_bonus.addView(dynamicLayout)
        }

        val contracts = resources.getStringArray(R.array.contrats)
        val adapterContrat = ArrayAdapter(this, R.layout.spinner_item, contracts)
        spinner_contrat.adapter = adapterContrat

        var ecartScore = changeEcart(0,10)
        btn0.setOnClickListener { ecartScore = changeEcart(0,ecartScore) }
        btn10.setOnClickListener { ecartScore = changeEcart(10,ecartScore) }
        btn20.setOnClickListener { ecartScore = changeEcart(20,ecartScore) }
        btn30.setOnClickListener { ecartScore = changeEcart(30,ecartScore) }

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
            partyIsWon = changeResult(game.success)
            for (index in game.bonusPlayersId.indices) {
                val dynamicLayout = inflater.inflate(R.layout.bonus_layout, null)
                val playersName = ArrayList(players.map { it }) // create copy
                val adapterBonusName = ArrayAdapter(this, R.layout.spinner_item, playersName)
                dynamicLayout.spinner_bonus_name.adapter = adapterBonusName

                val bonuses = resources.getStringArray(R.array.bonuses)
                val adapterBonusValue = ArrayAdapter(this, R.layout.spinner_item, bonuses)
                dynamicLayout.spinner_bonus_value.adapter = adapterBonusValue

                dynamicLayout.btn_remove.setOnClickListener {
                    LL_bonus.removeView(dynamicLayout)
                }
                val playerId = game.bonusPlayersId[index]
                val bonusName = game.bonusStringNames[index]
                val bonusValueId = bonuses.indexOf(bonusName)
                dynamicLayout.spinner_bonus_name.setSelection(playerId)
                dynamicLayout.spinner_bonus_value.setSelection(bonusValueId)

                // Add the dynamic layout to the parent layout
                LL_bonus.addView(dynamicLayout)
            }
            gameId = game.gameId
        }


        btnValider.setOnClickListener {
            val intent = Intent()
            intent.putExtra("preneur", spinner_preneur.selectedItemPosition)
            val size = LL_bonus.childCount
            val listPlayersId = IntArray(size)
            val listStringBonus = arrayOfNulls<String>(size)
            for (childIndex in 0 until size) {
                val child = LL_bonus.getChildAt(childIndex)
                val bonusIdPlayer: Int = child.spinner_bonus_name.selectedItemPosition
                val bonusStringName: String = child.spinner_bonus_value.selectedItem as String
                listPlayersId[childIndex] = bonusIdPlayer
                listStringBonus[childIndex] = bonusStringName
            }
            intent.putExtra("list_bonus_names", listPlayersId)
            intent.putExtra("list_bonus_values", listStringBonus)

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

    private fun changeEcart(new_score_id: Int, current_score_id : Int): Int {
        if (current_score_id == new_score_id)
            return new_score_id
        val clickedButton = if (new_score_id == 0) btn0 else if (new_score_id == 10) btn10 else if (new_score_id == 20) btn20 else btn30
        val previousButton = if (current_score_id == 0) btn0 else if (current_score_id == 10) btn10 else if (current_score_id == 20) btn20 else btn30
        previousButton.setBackgroundResource(R.color.colorPrimaryLight)
        previousButton.setTextAppearance(R.style.ButtonStyleDifferenceUnselected)
        clickedButton.setBackgroundResource(colorForScore)
        clickedButton.setTextAppearance(R.style.ButtonStyleDifferenceSelected)

//        btn0.setBackgroundColor(
//            ContextCompat.getColor(
//                applicationContext,
//                if (score_id == 0) colorForScore else R.color.colorPrimaryLight
//            )
//        )
//        btn10.setBackgroundColor(
//            ContextCompat.getColor(
//                applicationContext,
//                if (score_id == 10) colorForScore else R.color.colorPrimaryLight
//            )
//        )
//        btn20.setBackgroundColor(
//            ContextCompat.getColor(
//                applicationContext,
//                if (score_id == 20) colorForScore else R.color.colorPrimaryLight
//            )
//        )
//        btn30.setBackgroundColor(
//            ContextCompat.getColor(
//                applicationContext,
//                if (score_id == 30) colorForScore else R.color.colorPrimaryLight
//            )
//        )
        return new_score_id
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