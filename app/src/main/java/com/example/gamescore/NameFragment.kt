package com.example.gamescore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_name.*
import kotlinx.android.synthetic.main.fragment_name.view.*


class NameFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_name, container, false)
        val act = activity as TarotActivity
        if (act.NbPlayers == 4)
            v.llP5.visibility = View.GONE

        v.ok_btn.setOnClickListener {
            val l = mutableListOf<String>()
            l.add(nameP1.text.toString())
            l.add(nameP2.text.toString())
            l.add(nameP3.text.toString())
            l.add(nameP4.text.toString())
            if (act.NbPlayers == 5)
                l.add(nameP5.text.toString())
            act.startGame(l)
        }

        return v
    }

    companion object {

        fun newInstance(): NameFragment {
            return NameFragment()
        }
    }
}