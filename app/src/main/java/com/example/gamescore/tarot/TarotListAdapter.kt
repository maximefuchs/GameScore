package com.example.gamescore.tarot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.gamescore.Game
import com.example.gamescore.ListAdapterGS
import com.example.gamescore.R
import kotlinx.android.synthetic.main.tarot_list_item.view.*
import java.util.*

class TarotListAdapter(private var games: ArrayList<Game>,
                       private val onItemClicked: (Game) -> Unit,
                       private val onItemLongClicked: (Int) -> Boolean
):
    ListAdapterGS(games) {

    class ViewHolder(view: View) : ListAdapterGS.ViewHolder(view) {
        val tvP1 : TextView = view.scoreP1
        val tvP2 : TextView = view.scoreP2
        val tvP3 : TextView = view.scoreP3
        val tvP4 : TextView = view.scoreP4
        val tvP5 : TextView = view.scoreP5
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.tarot_list_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ListAdapterGS.ViewHolder, position: Int) {
        onBindViewHolder(viewHolder as ViewHolder, position)
    }

    private fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.tvP1.text = games[position].score[0].toString()
        viewHolder.tvP2.text = games[position].score[1].toString()
        viewHolder.tvP3.text = games[position].score[2].toString()
        viewHolder.tvP4.text = games[position].score[3].toString()
        if (games[position].nbPlayers == 5) {
            viewHolder.tvP5.visibility = View.VISIBLE
            viewHolder.tvP5.text = games[position].score[4].toString()
        }
        val item = games[position]
        viewHolder.bind(item)
        viewHolder.itemView.setOnClickListener { onItemClicked(item) }
        viewHolder.itemView.setOnLongClickListener { onItemLongClicked(position) }
    }
}