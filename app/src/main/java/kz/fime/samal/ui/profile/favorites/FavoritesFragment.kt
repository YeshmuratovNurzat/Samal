package kz.fime.samal.ui.profile.favorites

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kz.fime.samal.R
import kz.fime.samal.data.models.Product
import kz.fime.samal.databinding.FragmentFavoritesBinding
import kz.fime.samal.ui.base.observeEvent
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.utils.binding.BindingFragment
import kz.fime.samal.utils.components.BottomBar
import kz.fime.samal.utils.extensions.ITEM_ARG
import kz.fime.samal.utils.extensions.getOrNull
import kz.fime.samal.utils.extensions.setUpMenu

@AndroidEntryPoint
class FavoritesFragment: BindingFragment<FragmentFavoritesBinding>(FragmentFavoritesBinding::inflate) {

    private val viewModel: FavoritesViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        BottomBar.getBottomBar()?.showMain()
        binding.run {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            toolbar.setUpMenu(R.id.clear to { findNavController().navigate(R.id.action_global_clear_favorites) })
            contentEmpty.btnGoToCatalog.setOnClickListener { BottomBar.getBottomBar()?.selectItemIndex(1) }

            viewModel.load()
            srl.setOnRefreshListener { viewModel.load() }

            val favoriteProductsAdapter = FavoriteProductsAdapter({
                findNavController().navigate(
                    R.id.action_global_product_details, bundleOf(Pair(
                        ITEM_ARG, Product(
                            shop_slug = it.getOrNull("shop_uuid", ""),
                            product_slug = it.getOrNull("product_slug", ""),
                            price = null, name = null, rating = null, people_voted = null, image = null
                        )
                    ))
                )
            }, { item, _ ->
                viewModel.toggleFavorites(item.getOrNull("shop_uuid", "")!!, item.getOrNull("product_slug", "")!!)
            })

            viewModel.toggleFavorites.observeEvent(viewLifecycleOwner, {
                viewModel.load()
            }, {
                viewModel.load()
            })


            rvItems.adapter = favoriteProductsAdapter
            viewModel.favorites.observeState(viewLifecycleOwner, {
                if (it.result.isNullOrEmpty()) {
                    rvItems.visibility = View.GONE
                    contentEmpty.root.visibility = View.VISIBLE
                } else {
                    rvItems.visibility = View.VISIBLE
                    contentEmpty.root.visibility = View.GONE
                }
                favoriteProductsAdapter.submitList(it.result)
                Favorites.updateList(it.result)
            }, {}, { srl.isRefreshing = it })
        }
    }
}