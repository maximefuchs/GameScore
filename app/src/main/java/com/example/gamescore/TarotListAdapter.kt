package com.example.gamescore

import android.app.Activity
import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ListAdapter
import kotlinx.android.synthetic.main.tarot_list_item.view.*

class TarotListAdapter(private var context: Context, private var games: ArrayList<Game>) : ListAdapter {

    override fun isEmpty(): Boolean {
        return games.isEmpty()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val v = inflater.inflate(R.layout.tarot_list_item, null, true)

        v.scoreP1.text = games[position].score[0].toString()
        v.scoreP2.text = games[position].score[1].toString()
        v.scoreP3.text = games[position].score[2].toString()
        v.scoreP4.text = games[position].score[3].toString()
        if(games[position].nb_players == 5)
            v.scoreP5.text = games[position].score[4].toString()
        else
            v.scoreP5.visibility = View.GONE

        return v
    }

    override fun registerDataSetObserver(observer: DataSetObserver?) {
        return
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItem(position: Int): Any {
        return games[position]
    }

    override fun getViewTypeCount(): Int {
        return games.size + 1
    }

    override fun isEnabled(position: Int): Boolean {
        return true
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun areAllItemsEnabled(): Boolean {
        return true
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver?) {
        return
    }

    override fun getCount(): Int {
        return games.size
    }

}