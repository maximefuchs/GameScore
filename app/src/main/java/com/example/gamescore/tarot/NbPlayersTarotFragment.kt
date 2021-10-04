package com.example.gamescore.tarot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gamescore.NbPlayersFragment
import com.example.gamescore.R
import kotlinx.android.synthetic.main.fragment_nb_players.view.*

class NbPlayersTarotFragment : NbPlayersFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = super.onCreateView(inflater, container, savedInstanceState)!!
        val fourP = v.btn_option1
        fourP.setText(R.string.nb_players_tarot_1)
        val fiveP = v.btn_option2
        fiveP.setText(R.string.nb_players_tarot_2)
        val act = activity as TarotActivity

        fourP.setOnClickListener {
            act.getName(4)
        }
        fiveP.setOnClickListener {
            act.getName(5)
        }

        return v
    }
}