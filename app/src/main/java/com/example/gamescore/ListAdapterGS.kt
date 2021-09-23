package com.example.gamescore

import android.content.Context
import android.database.DataSetObserver
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gamescore.tarot.GameTarot
import com.example.gamescore.tarot.TarotListAdapter
import kotlinx.android.synthetic.main.tarot_list_item.view.*
import java.util.*

abstract class ListAdapterGS(private var games: ArrayList<Game>) :
    RecyclerView.Adapter<ListAdapterGS.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(game: Game){}
    }

    abstract override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListAdapterGS.ViewHolder
    abstract override fun onBindViewHolder(viewHolder: ListAdapterGS.ViewHolder, position: Int)

    override fun getItemViewType(position: Int): Int { return position }
    override fun getItemId(position: Int): Long { return position.toLong() }
    override fun getItemCount() = games.size
}