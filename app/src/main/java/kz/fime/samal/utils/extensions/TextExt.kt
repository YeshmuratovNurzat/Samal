package kz.fime.samal.utils.extensions

import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import java.text.DecimalFormat

fun String?.asHtml(): Spanned {
    if (this == null) return SpannableString("")
    return Html.fromHtml(this)
}

fun String?.toPriceFormat(): String {
    if (this.isNullOrEmpty()) return ""
    return this.toInt().toPriceFormat()
}

fun Int?.toPriceFormat(): String {
    return DecimalFormat("###,###,###").format(this).replace(",", " ")
}

fun String?.toBalance() = "${toPriceFormat()} Б"

fun String?.toKzt() = "${toPriceFormat()} ₸"