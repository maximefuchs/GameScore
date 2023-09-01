package com.example.gamescore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.fragment_score.view.*
import java.util.*


open class ScoreFragment : Fragment() {
    lateinit var listPlayers: List<String>
    var listGames: ArrayList<Game> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_score, container, false)
        val act = activity as GameActivity
        listPlayers = act.names
        listGames = act.listGames
        act.showAddGameButton = true
        act.add_game.visibility = View.VISIBLE
        v.RV_games.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
        return v
    }
}