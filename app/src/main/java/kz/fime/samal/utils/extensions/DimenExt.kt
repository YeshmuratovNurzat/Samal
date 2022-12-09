package kz.fime.samal.utils.extensions

import kz.fime.samal.utils.DimensionUtils

fun Int.dpToPx() = DimensionUtils.dpToPx(this.toFloat()).toInt()

fun Int.pxToDp() = DimensionUtils.pxToDp(this.toFloat()).toInt()

fun Float.dpToPx() = DimensionUtils.dpToPx(this)

fun Float.pxToDp() = DimensionUtils.pxToDp(this)