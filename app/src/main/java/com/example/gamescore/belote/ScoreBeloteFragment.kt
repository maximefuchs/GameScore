package com.example.gamescore.belote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.gamescore.Game
import com.example.gamescore.R
import com.example.gamescore.ScoreFragment
import com.example.gamescore.TypeGameSaved
import kotlinx.android.synthetic.main.fragment_score.view.*


class ScoreBeloteFragment : ScoreFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val v = super.onCreateView(inflater, container, savedInstanceState)!!
        val act = activity as BeloteActivity
        act.inSettings = false
        v.LL_belote.visibility = View.VISIBLE

        if (act.nbPlayers == 4) {
            var placeholder: String = resources.getText(R.string.placeholder_team).toString()
            v.T1.text = placeholder.format(listPlayers[0].take(2), listPlayers[1].take(2))
            placeholder = resources.getText(R.string.placeholder_team).toString()
            v.T2.text = placeholder.format(listPlayers[2].take(2), listPlayers[3].take(2))
        } else { // 3 players
            v.T3.visibility = View.VISIBLE
            v.T1.text = listPlayers[0].take(2)
            v.T2.text = listPlayers[1].take(2)
            v.T3.text = listPlayers[2].take(2)
        }

        saveLastGame(if (act.gameType == TypeGame.COINCHEE) TypeGameSaved.BELOTE_COINCHEE else TypeGameSaved.BELOTE)

        val onItemClick = { game: Game ->
            handleItemShortClick(game)
        }
        val onItemLongClick = { position: Int ->
            handleItemLongClick(position)
            true // because onItemLongClick is of time (Int) -> Boolean
        }

        val adapter = BeloteListAdapter(listGames, onItemClick, onItemLongClick)
        v.RV_games.adapter = adapter

        return v
    }

}