package com.example.gamescore

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.gamescore.tarot.GameTarot
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.fragment_score.view.*
import java.util.*


open class ScoreFragment : Fragment() {
    lateinit var listPlayers: List<String>
    var listGames: ArrayList<Game> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_score, container, false)
        val act = activity as GameActivity
        listPlayers = act.names
        listGames = act.listGames
        act.showAddGameButton = true
        act.add_game.visibility = View.VISIBLE
        v.RV_games.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.HORIZONTAL
            )
        )

        return v
    }

    fun saveLastGame(typeGameSaved: TypeGameSaved) {
        if (listGames.size > 0) {
//            Log.w("LIST", listGames[0].toString())
            if (!listGames.last().restart) {
                val act = activity as GameActivity
                val lastGame = listGames.last()
                val editor: SharedPreferences.Editor =
                    act.context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE).edit()
                lastGame.saveGameToSharedPreferences(editor, typeGameSaved)
                if (!act.namesSaved) {
                    act.names.forEachIndexed { index, value ->
                        editor.putString("Name_$index", value)
                    }
                    editor.apply()
                    act.namesSaved = true
                }
            }
        }
    }
}