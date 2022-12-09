package kz.fime.samal.utils.extensions

import android.view.View

const val MIN_BTN_REFRESH_INTERVAL = 300L

fun View.onClick(action: () -> Unit) {
    var lastClick = 0L
    setOnClickListener {
        if (System.currentTimeMillis()-lastClick >= MIN_BTN_REFRESH_INTERVAL) {
            lastClick = System.currentTimeMillis()
            action.invoke()
        }
    }
}