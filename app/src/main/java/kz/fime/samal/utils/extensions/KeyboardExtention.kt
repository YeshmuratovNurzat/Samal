package kz.fime.samal.utils.extensions

import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.getSystemService
import androidx.core.view.postDelayed

fun EditText.requestFocusAndShowKeyboard() {
    requestFocus()
    postDelayed(200) {
        val keyboard: InputMethodManager = context.getSystemService()!!
        keyboard.showSoftInput(this, 0)
    }
}