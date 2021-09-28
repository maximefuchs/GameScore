package com.example.gamescore.belote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gamescore.NameFragment
import kotlinx.android.synthetic.main.fragment_name.*
import kotlinx.android.synthetic.main.fragment_name.view.*

class NameFragmentBelote : NameFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val v = super.onCreateView(inflater, container, savedInstanceState)!!
        val act = activity as BeloteActivity
        if (act.nbPlayers == 3)
            v.LLP4.visibility = View.GONE
        else {
            v.TV_team1.visibility = View.VISIBLE
            v.TV_team2.visibility = View.VISIBLE
        }

        v.ok_btn.setOnClickListener {
            val l = mutableListOf<String>()
            if (nameP1.text.isEmpty()) l.add("1")
            else l.add(nameP1.text.toString())
            if (nameP2.text.isEmpty()) l.add("2")
            else l.add(nameP2.text.toString())
            if (nameP3.text.isEmpty()) l.add("3")
            else l.add(nameP3.text.toString())
            if (act.nbPlayers == 4) {
                if (nameP4.text.isEmpty()) l.add("4")
                else l.add(nameP4.text.toString())
            }
            act.startGame(l)
        }

        return v
    }

    companion object {
        fun newInstance(): NameFragmentBelote {
            return NameFragmentBelote()
        }
    }
}