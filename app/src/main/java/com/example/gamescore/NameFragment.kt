package com.example.gamescore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_name.view.*


open class NameFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_name, container, false)
        val act = activity as GameActivity
        if (act.names.size > 0) {
            v.nameP1.setText(act.names[0])
            v.nameP2.setText(act.names[1])
            v.nameP3.setText(act.names[2])
            if (act.names.size > 3) {
                v.nameP4.setText(act.names[3])
                if (act.names.size > 4)
                    v.nameP5.setText(act.names[4])
            }
        } else { // dont put focus if names are prefilled
            v.nameP1.requestFocus()
            act.showKeyBoard(v.nameP1)
        }

        v.erase_btn.setOnClickListener {
            v.nameP1.setText("")
            v.nameP2.setText("")
            v.nameP3.setText("")
            v.nameP4.setText("")
            v.nameP5.setText("")
        }

        return v
    }
}