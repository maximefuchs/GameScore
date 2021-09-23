package com.example.gamescore.belote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.gamescore.Game
import com.example.gamescore.R
import com.example.gamescore.ScoreFragment
import kotlinx.android.synthetic.main.activity_game.view.*
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

        var placeholder : String = resources.getText(R.string.placeholder_team).toString()
        v.T1.text = placeholder.format(listPlayers[0].take(2),listPlayers[1].take(2))
        placeholder = resources.getText(R.string.placeholder_team).toString()
        v.T2.text = placeholder.format(listPlayers[2].take(2),listPlayers[3].take(2))


        val onItemClick = { _ : Game ->
            Toast.makeText(
                act.context,
                act.resources.getString(R.string.long_click_hint),
                Toast.LENGTH_SHORT
            ).show()
        }
        val onItemLongClick = {position : Int ->
            act.editBeloteGame(position)
        }
        val adapter = BeloteListAdapter(listGames,onItemClick,onItemLongClick)
        v.RV_games.adapter = adapter

        return v
    }

    companion object {}
}