package com.example.gamescore

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.gamescore.tarot.GameTarot
import kotlinx.android.synthetic.main.activity_game.*
import java.util.ArrayList

enum class Request(val value: Int) {
    ADDGAME(0),
    EDITGAME(1);

    companion object {
        fun fromInt(value: Int) = Request.values().first { it.value == value }
    }
}

open class GameActivity : AppCompatActivity() {
    lateinit var context: Context
    var names: List<String> = listOf<String>()
    lateinit var listGames: ArrayList<Game>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // quitting
        btnQuit.setOnClickListener { finish() }
        btnNoQuit.setOnClickListener { RLquit.visibility = View.GONE }

    }

    override fun onBackPressed() {
        RLquit.visibility = View.VISIBLE
    }

    fun hideKeyBoard() {
        val view: View? = this.currentFocus
        if (view != null) {
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
