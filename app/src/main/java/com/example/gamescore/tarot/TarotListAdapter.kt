package com.example.gamescore.tarot

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gamescore.ListAdapterGS
import com.example.gamescore.R
import kotlinx.android.synthetic.main.tarot_list_item.view.*

class TarotListAdapter(private var context: Context, private var games: ArrayList<GameTarot>) :
    ListAdapterGS(context, games) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val v = inflater.inflate(R.layout.tarot_list_item, null, true)

        v.scoreP1.text = games[position].score[0].toString()
        v.scoreP2.text = games[position].score[1].toString()
        v.scoreP3.text = games[position].score[2].toString()
        v.scoreP4.text = games[position].score[3].toString()
        if (games[position].nb_players == 5) {
            v.scoreP5.visibility = View.VISIBLE
            v.scoreP5.text = games[position].score[4].toString()
        }

        return v
    }

}