package kz.fime.samal.utils.extensions

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kz.fime.samal.R
import kz.fime.samal.utils.BASE_URL

fun ImageView.loadMapScreenshot(lat: Double, lng: Double, zoom: Int=16){
    val lang = "en-US"
    val width = 600
    val height = 450
    loadUrl("http://static-maps.yandex.ru/1.x/?lang=$lang&ll=$lng,$lat&size=$width,$height&z=$zoom&l=map")
}

fun ImageView.loadUrl(url: String?, @DrawableRes resId: Int? = null) {
    if (url.isNullOrEmpty()) {
        loadDrawable(resId ?: R.drawable.bg_image_placeholder)
    } else {
        Glide.with(this)
            .load(BASE_URL + url)
            .placeholder(resId ?: R.drawable.ic_image_placeholder)
            .fallback(resId ?: R.drawable.ic_image_placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(this)
    }
}

fun ImageView.loadDrawable(@DrawableRes resId: Int) {
    Glide.with(this)
        .load(resId)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}