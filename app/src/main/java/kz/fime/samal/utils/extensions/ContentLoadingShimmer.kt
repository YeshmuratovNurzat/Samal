package kz.fime.samal.utils.extensions

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.facebook.shimmer.ShimmerFrameLayout


fun Fragment.setShimmerView(shimmerContent: ShimmerFrameLayout) {
    if (shimmerContent.visibility == View.GONE) {
        shimmerContent.stopShimmer()
    }
    viewLifecycleOwner.lifecycle.addObserver(object: LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onResume(){
            if (shimmerContent.visibility == View.VISIBLE) {
                shimmerContent.startShimmer()
            } else {
                shimmerContent.stopShimmer()
            }
        }
        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun onPause(){
            if (shimmerContent.visibility == View.VISIBLE) {
                shimmerContent.stopShimmer()
            }
        }
    })
}