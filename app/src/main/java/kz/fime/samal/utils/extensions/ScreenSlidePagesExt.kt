package kz.fime.samal.utils.extensions

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sm.dinio.utils.extensions.removeOverScroll
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import kz.fime.samal.utils.binding.BindingRvAdapter

fun<M: Any, V: ViewBinding> Fragment.setUpVpAdapter(vp: ViewPager2, adapter: BindingRvAdapter<M, V>, dotsIndicator: DotsIndicator? = null){
    vp.removeOverScroll()
    vp.adapter = adapter
    dotsIndicator?.setViewPager2(vp)
}

fun Fragment.setUpPagesAdapter(vp: ViewPager2, pages: List<Fragment>, tabs: Pair<List<String>, TabLayout>? = null){
    vp.removeOverScroll()
    vp.adapter = ScreenSlidePagerAdapter(this, pages)
    tabs?.let {
        TabLayoutMediator(tabs.second, vp, true) { tab: TabLayout.Tab, position: Int ->
            tab.text = tabs.first[position]
        }.attach()
    }
}

private class ScreenSlidePagerAdapter(fa: Fragment, private val pages: List<Fragment>) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = pages.size

    override fun createFragment(position: Int): Fragment {
        return pages[position]
    }

}