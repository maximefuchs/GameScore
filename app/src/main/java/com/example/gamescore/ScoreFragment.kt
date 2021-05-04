package com.example.gamescore

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_score.view.*

/*// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

*//**
 * A simple [Fragment] subclass.
 * Use the [ScoreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScoreFragment : Fragment() {
    lateinit var list_players : List<String>
    lateinit var list_games : List<Game>
    // TODO: Rename and change types of parameters
/*    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_score, container, false)
        val act = activity as TarotActivity
        val nbPlayers = act.NbPlayers
        list_players = act.names
        if (nbPlayers == 4){
            v.P5.visibility = View.GONE
            v.called.visibility = View.GONE
        }
        v.P1.text = list_players.get(0).take(2)
        v.P2.text = list_players.get(1).take(2)
        v.P3.text = list_players.get(2).take(2)
        v.P4.text = list_players.get(3).take(2)
        if (nbPlayers == 5)
            v.P5.text = list_players.get(4).take(2)

        v.add_game.setOnClickListener {

        }



        return v
    }

    companion object {

        fun newInstance(): ScoreFragment {
            var s = ScoreFragment()
            s.list_games = listOf<Game>()
            return s
        }
    }

    /*companion object {
        *//**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ScoreFragment.
         *//*
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScoreFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }*/
}
