package com.example.gamescore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlinx.android.synthetic.main.activity_add_game_tarot.*

class AddGameTarotActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_game_tarot)

        val b = intent.extras
        val players = b?.getStringArrayList("players") as ArrayList<String>

        if(players.size == 4)
            llcalled.visibility = View.GONE

        val adapterPreneur = ArrayAdapter(this,android.R.layout.simple_spinner_item,players)
        spinner_preneur.adapter = adapterPreneur
        val adapterCalled = ArrayAdapter(this,android.R.layout.simple_spinner_item,players)
        spinner_called.adapter = adapterCalled

        var bool : Boolean = false
        success_switch.setOnClickListener {
            if(bool){
                resultat.text = "Chutée"
                bool = false
            } else {
                resultat.text = "Faite"
                bool = true
            }
        }

        btnValider.setOnClickListener {
            val intent = Intent()
            intent.putExtra("preneur",players.indexOf(spinner_preneur.selectedItem.toString()))
            if(players.size == 5)
                intent.putExtra("appel",players.indexOf(spinner_called.selectedItem.toString()))
            intent.putExtra("contrat",spinner_contrat.selectedItem.toString())
            intent.putExtra("result",bool)
            intent.putExtra("ecart", ecart.text.toString().toInt())
            setResult(RESULT_OK, intent)
            finish()
        }



    }
}