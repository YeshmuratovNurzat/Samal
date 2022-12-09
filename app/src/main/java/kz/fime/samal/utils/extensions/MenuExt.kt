package kz.fime.samal.utils.extensions

import android.graphics.Color
import android.util.SparseArray
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import androidx.core.util.containsKey

fun Toolbar.changeIconTint(@IdRes itemId: Int, ratio: Float){
    val iconDrawable = menu.findItem(itemId).icon
    iconDrawable.mutate()
    iconDrawable.setTint(Color.argb(255, (ratio * 255).toInt(), (ratio * 255).toInt(), (ratio * 255).toInt()))
    menu.findItem(itemId).icon = iconDrawable
}

fun Toolbar.setUpMenu(vararg actions: Pair<Int, ()->Unit>){
    val map = HashMap<Int, Long>()
    setOnMenuItemClickListener { menuItem ->
        actions.forEach {
            if (it.first == menuItem.itemId) {
                if (System.currentTimeMillis()-map.getOrOther(it.first, 0L) >= MIN_BTN_REFRESH_INTERVAL) {
                    map[it.first] = System.currentTimeMillis()
                    it.second.invoke()
                }
            }
        }
        false
    }
}
