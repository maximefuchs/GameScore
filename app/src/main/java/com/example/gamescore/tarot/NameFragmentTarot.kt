package com.example.gamescore.tarot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gamescore.NameFragment
import kotlinx.android.synthetic.main.fragment_name.view.*

class NameFragmentTarot : NameFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val v = super.onCreateView(inflater, container, savedInstanceState)!!
        val act = activity as TarotActivity
        if (act.nbPlayers == 5)
            v.llP5.visibility = View.VISIBLE

        if (act.names.size >= 4) {
            v.nameP1.setText(act.names[0])
            v.nameP2.setText(act.names[1])
            v.nameP3.setText(act.names[2])
            v.nameP4.setText(act.names[3])
            if (act.nbPlayers == 5 && act.names.size == 5)
                v.nameP5.setText(act.names[4])
        }

        v.ok_btn.setOnClickListener {
            val list = ArrayList<String>()
            if (v.nameP1.text.isEmpty()) list.add("J1")
            else list.add(v.nameP1.text.toString())
            if (v.nameP2.text.isEmpty()) list.add("J2")
            else list.add(v.nameP2.text.toString())
            if (v.nameP3.text.isEmpty()) list.add("J3")
            else list.add(v.nameP3.text.toString())
            if (v.nameP4.text.isEmpty()) list.add("J4")
            else list.add(v.nameP4.text.toString())
            if (act.nbPlayers == 5) {
                if (v.nameP5.text.isEmpty()) list.add("J5")
                else list.add(v.nameP5.text.toString())
            }
            act.startGame(list)
        }
        return v
    }
}