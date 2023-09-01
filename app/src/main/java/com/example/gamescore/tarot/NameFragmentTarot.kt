package com.example.gamescore.tarot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gamescore.NameFragment
import kotlinx.android.synthetic.main.fragment_name.*
import kotlinx.android.synthetic.main.fragment_name.view.*

class NameFragmentTarot : NameFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = super.onCreateView(inflater, container, savedInstanceState)!!
        val act = activity as TarotActivity
        if (act.nbPlayers == 5)
            v.llP5.visibility = View.VISIBLE

        v.ok_btn.setOnClickListener {
            val list = ArrayList<String>()
            if (nameP1.text.isEmpty()) list.add("1")
            else list.add(nameP1.text.toString())
            if (nameP2.text.isEmpty()) list.add("2")
            else list.add(nameP2.text.toString())
            if (nameP3.text.isEmpty()) list.add("3")
            else list.add(nameP3.text.toString())
            if (nameP4.text.isEmpty()) list.add("4")
            else list.add(nameP4.text.toString())
            if (act.nbPlayers == 5) {
                if (nameP5.text.isEmpty()) list.add("5")
                else list.add(nameP5.text.toString())
            }
            act.startGame(list)
        }
        return v
    }

    companion object {
        fun newInstance(): NameFragmentTarot {
            return NameFragmentTarot()
        }
    }
}