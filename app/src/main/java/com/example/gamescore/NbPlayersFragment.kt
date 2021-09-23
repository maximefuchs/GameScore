package com.example.gamescore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gamescore.tarot.TarotActivity
import kotlinx.android.synthetic.main.fragment_nb_players.view.*

open class NbPlayersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nb_players, container, false)
    }

    companion object {

        fun newInstance(): NbPlayersFragment {
            return NbPlayersFragment()
        }
    }
}