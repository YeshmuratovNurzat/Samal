package kz.fime.samal.ui.catalog

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import kz.fime.samal.R
import kz.fime.samal.databinding.FragmentCatalogBinding
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.ui.catalog.pages.CategoriesFragment
import kz.fime.samal.ui.catalog.pages.ShopsFragment
import kz.fime.samal.ui.home.product.pages.DescriptionFragment
import kz.fime.samal.ui.home.product.pages.ReviewsFragment
import kz.fime.samal.utils.binding.BindingFragment
import kz.fime.samal.utils.components.BottomBar
import kz.fime.samal.utils.extensions.setUpPagesAdapter

@AndroidEntryPoint
class CatalogFragment: BindingFragment<FragmentCatalogBinding>(FragmentCatalogBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        BottomBar.getBottomBar()?.showMain()
        binding.run {

            setUpPagesAdapter(vp2, listOf(CategoriesFragment(), ShopsFragment()))

            swCategories.setOnCheckedChangeListener { _, isChecked ->
                updateSwIcon(!isChecked)
                vp2.currentItem = if (isChecked) 1 else 0
            }

            vp2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    swCategories.isChecked = position == 1
                }
            })

        }
    }

    private fun updateSwIcon(isCategory: Boolean) {
        binding.toolbar.title = if (isCategory) "Категории" else "Магазины"
        binding.swCategories.thumbDrawable = ResourcesCompat.getDrawable(resources, if (isCategory) R.drawable.bg_sw else R.drawable.bg_sw_2, null)
    }

}