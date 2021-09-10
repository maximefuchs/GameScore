package com.example.gamescore

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_add_game_tarot.*
import kotlinx.android.synthetic.main.fragment_score.*
import kotlinx.android.synthetic.main.fragment_score.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ScoreFragment : Fragment() {
    lateinit var listPlayers: List<String>
    var listGames: ArrayList<Game> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_score, container, false)
        val act = activity as TarotActivity
        val nbPlayers = act.NbPlayers
        listPlayers = act.names
        if (nbPlayers == 4)
            v.P5.visibility = View.GONE
        v.P1.text = listPlayers[0].take(2)
        v.P2.text = listPlayers[1].take(2)
        v.P3.text = listPlayers[2].take(2)
        v.P4.text = listPlayers[3].take(2)
        if (nbPlayers == 5)
            v.P5.text = listPlayers[4].take(2)

        if(listGames.size > 0)
            Log.w("LIST", listGames[0].toString())

//        listGames.reverse()
        val adapter = TarotListAdapter(act.context,listGames)
        v.LV_games.adapter = adapter


        v.add_game.setOnClickListener {
            act.addTarotGame()
        }

        v.LV_games.setOnItemLongClickListener { parent, view, position, id ->
            act.editTarotGame(position)
        }
        v.LV_games.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(act.context,act.resources.getString(R.string.long_click_hint), Toast.LENGTH_SHORT).show()
        }

//        Log.w("size", listGames.size.toString())

        return v
    }

    companion object {
        fun newInstance(): ScoreFragment {
            val s = ScoreFragment()
            s.listGames = ArrayList<Game>()
            return s
        }
    }
}