package com.example.gamescore.belote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.gamescore.R
import com.example.gamescore.ScoreFragment
import kotlinx.android.synthetic.main.fragment_score.view.*


class ScoreBeloteFragment : ScoreFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = super.onCreateView(inflater, container, savedInstanceState)!!
        val act = activity as BeloteActivity
        v.LL_belote.visibility = View.VISIBLE

        val placeholder : String = resources.getText(R.string.placeholder_team).toString()
        v.T1.text = placeholder.format(listPlayers[0].take(2),listPlayers[1].take(2))
        v.T1.text = placeholder.format(listPlayers[2].take(2),listPlayers[3].take(2))


//        val adapter = TarotListAdapter(act.context, listGames)
//        v.LV_games.adapter = adapter


//        v.add_game.setOnClickListener {
//            act.addTarotGame()
//        }
//
//        v.LV_games.setOnItemLongClickListener { _, _, position, _ ->
//            act.editTarotGame(position)
//        }
        v.LV_games.setOnItemClickListener { _, _, _, _ ->
            Toast.makeText(act.context,act.resources.getString(R.string.long_click_hint), Toast.LENGTH_SHORT).show()
        }

        return v
    }

    companion object {}
}