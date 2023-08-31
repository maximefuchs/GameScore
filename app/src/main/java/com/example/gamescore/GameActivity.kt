package com.example.gamescore

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_game.*
import java.util.ArrayList

enum class Request(val value: Int) {
    ADDGAME(0),
    EDITGAME(1),
    CANCEL(2);

    companion object {
        fun fromInt(value: Int) = Request.values().first { it.value == value }
    }
}

open class GameActivity : AppCompatActivity() {
    lateinit var context: Context
    var names: ArrayList<String> = arrayListOf<String>()
    lateinit var listGames: ArrayList<Game>
    val stateNames = "names"
    val stateGames = "games"

    private val offSetQuit = 150f
    private val animDelay: Long = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // quitting
        btnQuit.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
        }
        btnNoQuit.setOnClickListener {
            ObjectAnimator.ofFloat(LLquit, "translationY", offSetQuit).apply {
                duration = animDelay
                start()
            }
            ObjectAnimator.ofFloat(RLquit, "alpha", 0f).apply {
                duration = animDelay
                start()
            }.doOnEnd {
                RLquit.visibility = View.GONE
                add_game.visibility = View.VISIBLE
            }
        }

    }

    override fun onBackPressed() {
        // TODO: don't reshow when back is pressed again
        LLquit.translationY = offSetQuit
        ObjectAnimator.ofFloat(LLquit, "translationY", 0f).apply {
            duration = animDelay
            start()
        }
        RLquit.alpha = 0f
        ObjectAnimator.ofFloat(RLquit, "alpha", 1f).apply {
            duration = animDelay
            start()
        }
        RLquit.visibility = View.VISIBLE
        add_game.visibility = View.GONE
    }

    fun hideKeyBoard() {
        val view: View? = this.currentFocus
        if (view != null) {
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showKeyBoard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    fun fragmentTransition(container: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right)
            .replace(container, fragment)
            .commit()
    }
}
