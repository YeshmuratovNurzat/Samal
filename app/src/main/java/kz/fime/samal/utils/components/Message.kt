package kz.fime.samal.utils.components

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.google.android.material.behavior.SwipeDismissBehavior
import kz.fime.samal.databinding.ContentMessageBinding
import kz.fime.samal.utils.extensions.spring
import java.lang.ref.WeakReference

class Message(private val messagesContainer: CoordinatorLayout) {

    private val dismissBehavior = SwipeDismissBehavior<CardView>().apply {
        setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_START_TO_END)
    }


    fun postMessage(message: String){
        val cvMessage = ContentMessageBinding.inflate(LayoutInflater.from(messagesContainer.context), messagesContainer, true)
        (cvMessage.vgContainer.layoutParams as CoordinatorLayout.LayoutParams).behavior = dismissBehavior
        cvMessage.tvMessage.text = message
        dismissBehavior.listener = object : SwipeDismissBehavior.OnDismissListener {
            override fun onDismiss(view: View?) {
                messagesContainer.removeView(view)
            }
            override fun onDragStateChanged(state: Int) {}
        }
        cvMessage.root.scaleX = 0.1f
        cvMessage.root.scaleY = 0.1f
        cvMessage.root.visibility = View.VISIBLE
        cvMessage.root.spring(DynamicAnimation.SCALE_X).animateToFinalPosition(1.0f)
        cvMessage.root.spring(DynamicAnimation.SCALE_Y).animateToFinalPosition(1.0f)

        val weakRef = WeakReference<View>(cvMessage.root)
        val weakContainer = WeakReference<ViewGroup>(messagesContainer)
        cvMessage.root.postDelayed({
            weakContainer.get()?.let { container ->
                weakRef.get()?.let {
                    TransitionManager.beginDelayedTransition(container, TransitionSet().addTransition(Fade()).addTransition(Slide(Gravity.TOP)))
                    container.removeView(it)
                }
            }

        }, 2000)
    }

}