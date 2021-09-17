package com.example.gamescore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gamescore.tarot.GameTarot
import com.example.gamescore.tarot.TarotActivity
import com.example.gamescore.tarot.TarotListAdapter
import kotlinx.android.synthetic.main.fragment_score.view.*
import kotlin.collections.ArrayList


open class ScoreFragment : Fragment() {
    lateinit var listPlayers: List<String>
    var listGames: ArrayList<GameTarot> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_score, container, false)
    }

    companion object {}
}