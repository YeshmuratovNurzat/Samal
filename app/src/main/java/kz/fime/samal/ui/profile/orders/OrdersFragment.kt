package kz.fime.samal.ui.profile.orders

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kz.fime.samal.data.models.custom.Status
import kz.fime.samal.data.models.order_detail.Order
import kz.fime.samal.databinding.FragmentOrdersBinding
import kz.fime.samal.ui.base.observeEvent
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.utils.binding.BindingFragment
import kz.fime.samal.utils.components.BottomBar
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull
import timber.log.Timber

@AndroidEntryPoint
class OrdersFragment: BindingFragment<FragmentOrdersBinding>(FragmentOrdersBinding::inflate) {

    private val viewModel: OrdersViewModel by viewModels()
    private var objects: MutableList<Item> = mutableListOf()
    private var isLoading = false
    private var page: String = "0"
    private var nextPage: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

            contentEmpty.btnGoToCatalog.setOnClickListener {
                BottomBar.getBottomBar()?.selectItemIndex(1)
            }

            srl.setOnRefreshListener {
                srl.isRefreshing = false
                //viewModel.load()
            }

            val ordersAdapter = OrdersAdapter {
                findNavController().navigate(OrdersFragmentDirections.toOrderDetailsFragment(it.getOrNull("order_uuid", "")!!, it.getOrNull("number", 0) as Int))
            }
            rvItems.adapter = ordersAdapter


            viewModel.resultOrders.observe(viewLifecycleOwner){
                Timber.d("Orders: %s", it)

                when(it.status) {

                    Status.LOADING -> {}

                    Status.SUCCESS -> {
                        it.data?.let {
                            it.data?.let { it1 ->
                                if (objects.containsAll(it1)) {
                                } else {
                                    objects.addAll(it1)
                                }
                            }
                            ordersAdapter.submitList(objects)
                            srl.isRefreshing = false
                            isLoading = false
                            nextPage = it.links.getOrNull("next", "")
                            rvItems.addOnScrollListener(object :
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
                                        if (nextPage != null) {
                                            if (!isLoading) {
                                                if (visibleItemCount != null && pastVisibleItems != null && totalItemCount != null) {
                                                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                                        val pages = nextPage!!.split("?")
                                                        if (page.toInt() < pages[pages.lastIndex].substring(5).toInt()) {
                                                            page = pages[pages.lastIndex].substring(5)
                                                            viewModel.getOrders(page)
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
                }
            }

//            viewModel.orders.observeState(viewLifecycleOwner, {
//                Timber.d("Orders: %s", it)
//                if (it.result.isNullOrEmpty()) {
//                    rvItems.visibility = View.GONE
//                    contentEmpty.root.visibility = View.VISIBLE
//                } else {
//                    rvItems.visibility = View.VISIBLE
//                    contentEmpty.root.visibility = View.GONE
//                }
//                //ordersAdapter.submitList(it.result)
//            }, {}, { srl.isRefreshing = it })

            viewModel.cancelOrder.observe(viewLifecycleOwner){
                //viewModel.getOrders("")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.cancelOrder.observeEvent(viewLifecycleOwner){
            //viewModel.getOrders("")
        }
    }
}