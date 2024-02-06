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
import com.example.gamescore.belote.ScoreBeloteFragment
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.help_layout.view.*
import java.util.ArrayList

enum class Request(val value: Int) {
    ADDGAME(0),
    EDITGAME(1),
    CANCEL(2)
}

enum class TypeGameSaved {
    BELOTE, BELOTE_COINCHEE, TAROT
}

abstract class GameActivity : AppCompatActivity() {
    lateinit var context: Context
    var names: ArrayList<String> = arrayListOf()
    var listGames: ArrayList<Game> = arrayListOf()
    var nbPlayers: Int = 4

    // quit layout on back pressed
    private val offSetQuit = 150f
    private val animDelay: Long = 200
    private var backPressed: Boolean = false
    var showAddGameButton: Boolean = false

    // saved state
    private val stateNames = "names"
    private val stateGames = "games"
    var namesSaved: Boolean = false

    // back to previous fragemnt
    var inSettings = true
    var firstFragment = true
    lateinit var previousFragment: Fragment

    // help layout variables
    private var showHelp: Boolean = false
    private lateinit var helpLayout: View
    var helpText: String = ""
    var helpText2: String = ""


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
        }
        btnNoQuit.setOnClickListener {
            hideBackPressedMenu()
        }


        // Inflate the dynamic layout
        val inflater = LayoutInflater.from(this)
        val dynamicLayout = inflater.inflate(R.layout.help_layout, null)
        // Set layout parameters for the inflated view
        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        dynamicLayout.layoutParams = layoutParams
        helpLayout = dynamicLayout

        help_btn.setOnClickListener {
            showHelpPopUp()
        }

    }

    abstract fun editGame(game_id: Int): Boolean

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (inSettings)
            // dont save anything because it hasnt been initialized
            return
        // save state
        outState.putSerializable(stateNames, names)
        outState.putSerializable(stateGames, listGames)
    }

    override fun onBackPressed() {
        if (firstFragment) {
            finish()
            return
        }
        if (inSettings) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left)
            transaction.replace(R.id.container, previousFragment) // Replace with your fragment
            transaction.addToBackStack(null) // Add to the back stack for navigation
            transaction.commit()
        } else {
            if (showHelp) {
                showHelpPopUp()
                return
            }
            if (!backPressed) {
                showBackPressedMenu()
            } else {
                hideBackPressedMenu()
            }
        }
    }

    private fun showBackPressedMenu() {
        help_btn.visibility = View.GONE
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
            help_btn.visibility = View.VISIBLE
            if (showAddGameButton)
                add_game.visibility = View.VISIBLE
        }
        backPressed = false
    }

    private fun hideKeyBoard() {
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

    private fun showHelpPopUp() {
        if (!showHelp) {
            val textView = helpLayout.text_help
            textView.text = Html.fromHtml(helpText, Html.FROM_HTML_MODE_COMPACT)
            val textView2 = helpLayout.text_help2
            textView2.text = Html.fromHtml(helpText2, Html.FROM_HTML_MODE_COMPACT)
            GameActivityId.addView(helpLayout)
            showHelp = true
            add_game.visibility = View.GONE
        } else {
            GameActivityId.removeView(helpLayout)
            showHelp = false
            add_game.visibility = View.VISIBLE
        }
    }

    open fun startGame(list_names: ArrayList<String>) {
        hideKeyBoard()
        names = list_names
        listGames = arrayListOf()
        showHelpButton()
    }

    open fun showHelpButton() {
        help_btn.visibility = View.VISIBLE
    }
}
