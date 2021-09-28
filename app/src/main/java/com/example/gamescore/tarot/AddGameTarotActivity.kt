package com.example.gamescore.tarot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
        var game_id = 0

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

//        spinner_preneur.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                var players_minus_one = ArrayList(players.map { it })
//                players_minus_one.removeAt(position)
//                val adapterCalled = ArrayAdapter(context, R.layout.spinner_item, players_minus_one)
//                spinner_called.adapter = adapterCalled
//            }
//        }

        val adapterCalled = ArrayAdapter(context, R.layout.spinner_item, players)
        spinner_called.adapter = adapterCalled

        val players_none = ArrayList(players.map { it }) // create copy
        players_none.add(0, " - ")
        val adapterBonus = ArrayAdapter(this, R.layout.spinner_item, players_none)
        spinner_bonus.adapter = adapterBonus

        val contracts = resources.getStringArray(R.array.contrats)
        val adapterContrat = ArrayAdapter(this, R.layout.spinner_item, contracts)
        spinner_contrat.adapter = adapterContrat

        var ecart_score = changeEcart(0)
        btn0.setOnClickListener { ecart_score = changeEcart(0) }
        btn10.setOnClickListener { ecart_score = changeEcart(10) }
        btn20.setOnClickListener { ecart_score = changeEcart(20) }
        btn30.setOnClickListener { ecart_score = changeEcart(30) }

        var party_is_won = changeResult(true)
        btnLose.setOnClickListener { party_is_won = changeResult(false) }
        btnWin.setOnClickListener { party_is_won = changeResult(true) }

        if (edit)
        {
            game = b.getSerializable("lastGame") as GameTarot
            if (game.nb_players == 5){
                game = b.getSerializable("lastGame") as GameTarot5
                spinner_called.setSelection(game.teammate)
            }
            spinner_preneur.setSelection(game.player_take)
            spinner_contrat.setSelection(contracts.indexOf(game.contract))
            ecart_score = changeEcart(game.difference)
            party_is_won = changeResult(game.success)
            spinner_bonus.setSelection(game.bonus + 1)
            game_id = game.game_id
        }


        btnValider.setOnClickListener {
            val intent = Intent()
            intent.putExtra("preneur", players.indexOf(spinner_preneur.selectedItem.toString()))
            if (spinner_bonus.selectedItemPosition != 0)
                intent.putExtra("bonus", players.indexOf(spinner_bonus.selectedItem.toString()))
            else // no bonus
                intent.putExtra("bonus", -1)
            if (players.size == 5)
                intent.putExtra("appel", players.indexOf(spinner_called.selectedItem.toString()))
            intent.putExtra("contrat", spinner_contrat.selectedItem.toString())
            intent.putExtra("result", party_is_won)
            intent.putExtra("ecart", ecart_score)
            if(edit)
                intent.putExtra("game_id",game_id)
            setResult(if(edit) Request.EDITGAME.value else Request.ADDGAME.value, intent)
            finish()
        }

    }

    private fun changeEcart(score_id: Int): Int {
        btn0.setBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                if (score_id == 0) R.color.colorPrimaryLightUp else R.color.colorPrimaryLight
            )
        )
        btn10.setBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                if (score_id == 10) R.color.colorPrimaryLightUp else R.color.colorPrimaryLight
            )
        )
        btn20.setBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                if (score_id == 20) R.color.colorPrimaryLightUp else R.color.colorPrimaryLight
            )
        )
        btn30.setBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                if (score_id == 30) R.color.colorPrimaryLightUp else R.color.colorPrimaryLight
            )
        )
        return score_id
    }
}