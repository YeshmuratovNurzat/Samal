package kz.fime.samal.utils.extensions

import android.graphics.Color
import android.widget.Button
import androidx.fragment.app.Fragment
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import kz.fime.samal.R

fun Fragment.progressButton(button: Button){
    viewLifecycleOwner.bindProgressButton(button)
    button.attachTextChangeAnimator()
}

fun Button.isLoading(isLoading: Boolean) {
    if (isLoading) {
        setTag(R.id.button_text, text.toString())
        showProgress {
            progressColor = Color.BLACK
        }
    } else {
        if (getTag(R.id.button_text)!=null) {
            hideProgress(getTag(R.id.button_text).toString())
        } else {
            hideProgress()
        }
    }
}