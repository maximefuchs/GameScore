package com.example.gamescore.belote

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.gamescore.Game
import com.example.gamescore.ListAdapterGS
import com.example.gamescore.R
import kotlinx.android.synthetic.main.belote_list_item.view.*
import java.util.*

class BeloteListAdapter(private var games: ArrayList<Game>,
                        private val onItemClicked: (Game) -> Unit,
                        private val onItemLongClicked: (Int) -> Boolean) :
    ListAdapterGS(games) {

    class ViewHolder(view: View) : ListAdapterGS.ViewHolder(view) {
        val tvT1 : TextView = view.scoreT1
        val tvT2 : TextView = view.scoreT2
        val tvT3 : TextView = view.scoreT3
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BeloteListAdapter.ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.belote_list_item, viewGroup, false)
        return BeloteListAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ListAdapterGS.ViewHolder, position: Int) {
        onBindViewHolder(viewHolder as BeloteListAdapter.ViewHolder, position)
    }

    private fun onBindViewHolder(viewHolder: BeloteListAdapter.ViewHolder, position: Int) {
        viewHolder.tvT1.text = games[position].score[0].toString()
        viewHolder.tvT2.text = games[position].score[1].toString()
        if (games[position].nbPlayers == 3) {
            viewHolder.tvT3.visibility = View.VISIBLE
            viewHolder.tvT3.text = games[position].score[2].toString()
        }

        val item = games[position]
        viewHolder.bind(item)
        viewHolder.itemView.setOnClickListener { onItemClicked(item) }
        viewHolder.itemView.setOnLongClickListener { onItemLongClicked(position) }

    }

}