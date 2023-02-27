package kz.fime.samal.ui.catalog.pages

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kz.fime.samal.R
import kz.fime.samal.data.models.custom.Status
import kz.fime.samal.databinding.FragmentCatalogShopsBinding
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.ui.catalog.adapters.ShopsAdapter
import kz.fime.samal.utils.binding.BindingFragment
import kz.fime.samal.utils.components.BottomBar
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull
import kz.fime.samal.utils.extensions.setShimmerView
import timber.log.Timber

@AndroidEntryPoint
class ShopsFragment: BindingFragment<FragmentCatalogShopsBinding>(FragmentCatalogShopsBinding::inflate) {

    private val viewModel: ShopsViewModel by viewModels()
    private var objects: MutableList<Item> = mutableListOf()
    private var isLoading = false
    private var page: String = "0"
    private var nextPage: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        BottomBar.getBottomBar()?.showMain()
        binding.run {
            setShimmerView(contentLoading.root)

            val shopsAdapter = ShopsAdapter {
                findNavController().navigate(
                    R.id.action_global_shop,
                    bundleOf(Pair("id", it.getOrNull("shop_uuid", "")))
                )
            }
            rvShops.adapter = shopsAdapter

            srl.setOnRefreshListener {
                srl.isRefreshing = false
            }

            etSearch.setOnClickListener {
                findNavController().navigate(R.id.search, bundleOf(Pair("type", "shop")))
            }

            viewModel.resultShops.observe(viewLifecycleOwner) {
                Timber.d("resultShops: %s", it)
                val size = objects.size
                when (it.status) {
                    Status.LOADING -> {
                        if (size == 0) {
                            rvShops.visibility = View.GONE
                            contentLoading.root.visibility = View.VISIBLE
                        }
                    }
                    Status.SUCCESS -> {
                        contentLoading.root.stopShimmer()
                        contentLoading.root.visibility = View.GONE
                        rvShops.visibility = View.VISIBLE
                        it.data?.let {
                            it.data?.let { it1 ->
                                if (objects.containsAll(it1)) {

                                } else {
                                    objects.addAll(it1)
                                }
                            }
                            shopsAdapter.submitList(objects)
                            srl.isRefreshing = false
                            isLoading = false
                            rvShops.addOnScrollListener(object :
                                RecyclerView.OnScrollListener() {
                                override fun onScrolled(
                                    recyclerView: RecyclerView,
                                    dx: Int,
                                    dy: Int
                                ) {
                                    super.onScrolled(recyclerView, dx, dy)
                                    if (dy > 0) {
                                        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                                        val visibleItemCount = linearLayoutManager?.childCount
                                        val totalItemCount = linearLayoutManager?.itemCount
                                        val pastVisibleItems = linearLayoutManager?.findFirstVisibleItemPosition()
                                        nextPage = it.links.getOrNull("next", "")
                                        if (nextPage != null) {
                                            if (!isLoading) {  
                                                if (visibleItemCount != null && pastVisibleItems != null && totalItemCount != null) {
                                                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                                        val pages = nextPage!!.split("?")
                                                        if (page.toInt() < pages[pages.lastIndex].substring(5).toInt()) {
                                                            page = pages[pages.lastIndex].substring(5)
                                                            viewModel.getShops(page)
                                                            srl.isRefreshing = true
                                                            isLoading = true
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            })
                        }
                    }
                    Status.ERROR -> {}
                }
            }
        }
    }
}
