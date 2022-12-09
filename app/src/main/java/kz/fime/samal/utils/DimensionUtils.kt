package kz.fime.samal.utils

import android.content.res.Resources
import android.util.TypedValue
import kz.fime.samal.utils.extensions.pxToDp

object DimensionUtils {

    fun displayHeightPx() = Resources.getSystem().displayMetrics.heightPixels

    fun displayWidthPx() = Resources.getSystem().displayMetrics.widthPixels

//    fun resDimenPx(resId: Int) = App.context.resources.getDimensionPixelSize(resId)
//
//    fun resDimen(resId: Int) = App.context.resources.getDimension(resId)

    fun dpToPx(value: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, Resources.getSystem().displayMetrics)
    }

    fun pxToDp(value: Float): Float {
        return value / Resources.getSystem().displayMetrics.density
    }

}