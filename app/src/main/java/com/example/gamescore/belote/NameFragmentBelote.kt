package com.example.gamescore.belote

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gamescore.NameFragment
import com.example.gamescore.R
import kotlinx.android.synthetic.main.fragment_name.*
import kotlinx.android.synthetic.main.fragment_name.view.*

class NameFragmentBelote : NameFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = super.onCreateView(inflater, container, savedInstanceState)!!
        val act = activity as BeloteActivity
        v.TV_team1.visibility = View.VISIBLE
        v.TV_team2.visibility = View.VISIBLE

        v.ok_btn.setOnClickListener{
            val l = mutableListOf<String>()
            l.add(nameP1.text.toString())
            l.add(nameP2.text.toString())
            l.add(nameP3.text.toString())
            l.add(nameP4.text.toString())
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