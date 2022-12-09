package kz.fime.samal.utils.extensions

import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.*
import java.util.concurrent.TimeUnit

fun Fragment.setUpSharedElementTransitions(){
//    sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    sharedElementEnterTransition = TransitionSet().apply {
        addTransition(ChangeImageTransform())
        addTransition(ChangeBounds())
        addTransition(ChangeTransform())
//        addTransition(ChangeOutlineRadiusTransition(10, 50)) // add this Transition
    }
//    sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    sharedElementReturnTransition = TransitionSet().apply {
        addTransition(ChangeImageTransform())
        addTransition(ChangeBounds())
        addTransition(ChangeTransform())
//        addTransition(ChangeOutlineRadiusTransition(10, 50)) // add this Transition
    }
}

fun RecyclerView.verticalWithSharedElementTransition(rvAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>, fragment: Fragment) {
    fragment.postponeEnterTransition(200, TimeUnit.MILLISECONDS)
    apply {
        layoutManager = LinearLayoutManager(context)
        adapter = rvAdapter
        doOnPreDraw {
            fragment.startPostponedEnterTransition()
        }
    }
}