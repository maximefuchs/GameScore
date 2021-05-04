package com.example.gamescore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_nb_players.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters

/**
 * A simple [Fragment] subclass.
 * Use the [NbPlayersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NbPlayersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_nb_players, container, false)
        val fourP = v.fourPlayers
        val fiveP = v.fivePlayers
        val act = activity as TarotActivity


        fourP.setOnClickListener {
            act.getName(4)
        }
        fiveP.setOnClickListener {
            act.getName(5)
        }

        return v
    }

    companion object {

        fun newInstance(): NbPlayersFragment {
            return NbPlayersFragment()
        }
    }
}