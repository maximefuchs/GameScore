package com.example.gamescore.belote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gamescore.R
import kotlinx.android.synthetic.main.fragment_type_belote.view.*


class TypeBeloteFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_type_belote, container, false)
        val act = activity as BeloteActivity
        act.previousFragment = NbPlayersBeloteFragment()

        v.normale.setOnClickListener {
            act.typeOfGame(TypeGame.NORMALE)
        }
        v.coinchee.setOnClickListener {
            act.typeOfGame(TypeGame.COINCHEE)
        }

        return v
    }

    companion object {

        fun newInstance(): TypeBeloteFragment {
            return TypeBeloteFragment()
        }
    }
}