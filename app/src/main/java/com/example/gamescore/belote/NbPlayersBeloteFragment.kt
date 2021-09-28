package com.example.gamescore.belote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gamescore.NbPlayersFragment
import com.example.gamescore.R
import kotlinx.android.synthetic.main.fragment_nb_players.view.*

class NbPlayersBeloteFragment : NbPlayersFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val v = super.onCreateView(inflater, container, savedInstanceState)!!
        val threeP = v.btn_option1
        threeP.setText(R.string.nb_players_belote_1)
        val fourP = v.btn_option2
        fourP.setText(R.string.nb_players_belote_2)
        val act = activity as BeloteActivity

        threeP.setOnClickListener {
            act.getName(3)
        }
        fourP.setOnClickListener {
            act.getTypeOfGame()
        }

        return v
    }
}