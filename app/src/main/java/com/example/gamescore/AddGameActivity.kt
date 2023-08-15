package com.example.gamescore

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import androidx.core.content.ContextCompat

open class AddGameActivity : AppCompatActivity() {
    lateinit var loseBtn : ImageButton
    lateinit var winBtn : ImageButton
    var colorForScore = R.color.colorVictoryUp

    open fun changeResult(is_won: Boolean): Boolean {
        loseBtn.setBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                if (is_won) R.color.colorPrimaryLight else R.color.colorDefeat
            )
        )
        winBtn.setBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                if (is_won) R.color.colorVictory else R.color.colorPrimaryLight
            )
        )
        colorForScore = if (is_won) R.color.colorVictoryUp else R.color.colorDefeatUp
        return is_won;
    }

    override fun onBackPressed() {
        val intent = Intent()
        setResult(Request.CANCEL.value, intent)
        finish()
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