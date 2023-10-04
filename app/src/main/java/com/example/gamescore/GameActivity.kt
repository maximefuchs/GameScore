package com.example.gamescore

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.help_layout.view.*
import java.util.ArrayList

enum class Request(val value: Int) {
    ADDGAME(0),
    EDITGAME(1),
    CANCEL(2)
}

enum class TypeGameSaved {
    BELOTE, TAROT
}

open class GameActivity : AppCompatActivity() {
    lateinit var context: Context
    var names: ArrayList<String> = arrayListOf()
    lateinit var listGames: ArrayList<Game>
    var nbPlayers: Int = 4
    private val stateNames = "names"
    private val stateGames = "games"

    private val offSetQuit = 150f
    private val animDelay: Long = 200
    private var backPressed: Boolean = false
    var showAddGameButton: Boolean = false
    var namesSaved: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // If we have a saved state then we can restore it now
        if (savedInstanceState != null) {
            names = savedInstanceState.getSerializable(stateNames) as ArrayList<String>
            listGames = savedInstanceState.getSerializable(stateGames) as ArrayList<Game>
            nbPlayers = names.size
        }

        // quitting
        btnQuit.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
        }
        btnNoQuit.setOnClickListener {
            hideBackPressedMenu()
        }


        // Inflate the dynamic layout
        val inflater = LayoutInflater.from(this)
        var boolShowHelp = false
        val dynamicLayout = inflater.inflate(R.layout.help_layout, null)
        // Set layout parameters for the inflated view
        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        dynamicLayout.layoutParams = layoutParams
        // Set text
        val rulesText = getString(R.string.tarot_game_rules)

        val textView = dynamicLayout.text_help
        textView.text = Html.fromHtml(rulesText, Html.FROM_HTML_MODE_COMPACT)

        help_btn.setOnClickListener {
            if (!boolShowHelp) {
                GameActivityId.addView(dynamicLayout)
                boolShowHelp = true
            } else {
                GameActivityId.removeView(dynamicLayout)
                boolShowHelp = false
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // save state
        outState.putSerializable(stateNames, names)
        outState.putSerializable(stateGames, listGames)
    }

    override fun onBackPressed() {
        if (!backPressed) {
            showBackPressedMenu()
        } else {
            hideBackPressedMenu()
        }
    }

    private fun showBackPressedMenu() {
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
        backPressed = true
    }

    private fun hideBackPressedMenu() {
        ObjectAnimator.ofFloat(LLquit, "translationY", offSetQuit).apply {
            duration = animDelay
            start()
        }
        ObjectAnimator.ofFloat(RLquit, "alpha", 0f).apply {
            duration = animDelay
            start()
        }.doOnEnd {
            RLquit.visibility = View.GONE
            if (showAddGameButton)
                add_game.visibility = View.VISIBLE
        }
        backPressed = false
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
