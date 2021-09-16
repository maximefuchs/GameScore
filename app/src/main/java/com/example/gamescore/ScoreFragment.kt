package com.example.gamescore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gamescore.tarot.GameTarot
import com.example.gamescore.tarot.TarotActivity
import com.example.gamescore.tarot.TarotListAdapter
import kotlinx.android.synthetic.main.fragment_score.view.*
import kotlin.collections.ArrayList


class ScoreFragment : Fragment() {
    lateinit var listPlayers: List<String>
    var listGames: ArrayList<GameTarot> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_score, container, false)
        val act = activity as TarotActivity
        val nbPlayers = act.nbPlayers
        listPlayers = act.names
        if (nbPlayers == 5)
            v.P5.visibility = View.VISIBLE
        v.P1.text = listPlayers[0].take(2)
        v.P2.text = listPlayers[1].take(2)
        v.P3.text = listPlayers[2].take(2)
        v.P4.text = listPlayers[3].take(2)
        if (nbPlayers == 5)
            v.P5.text = listPlayers[4].take(2)

        if(listGames.size > 0)
            Log.w("LIST", listGames[0].toString())

//        listGames.reverse()
        val adapter =
            TarotListAdapter(act.context, listGames)
        v.LV_games.adapter = adapter


        v.add_game.setOnClickListener {
            act.addTarotGame()
        }

        v.LV_games.setOnItemLongClickListener { _, _, position, _ ->
            act.editTarotGame(position)
        }
        v.LV_games.setOnItemClickListener { _, _, _, _ ->
            Toast.makeText(act.context,act.resources.getString(R.string.long_click_hint), Toast.LENGTH_SHORT).show()
        }

        return v
    }

    companion object {}
}