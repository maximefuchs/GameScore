package com.example.gamescore

import android.content.Context
import android.database.DataSetObserver
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import com.example.gamescore.tarot.GameTarot

abstract class ListAdapterGS(private var context: Context, private var games: ArrayList<GameTarot>) : ListAdapter {

    override fun isEmpty(): Boolean {
        return games.isEmpty()
    }

    abstract override fun getView(position: Int, view: View?, parent: ViewGroup): View

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