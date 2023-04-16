package kz.fime.samal.ui.profile.notifications

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
import kz.fime.samal.databinding.FragmentNotificationsBinding
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.utils.binding.BindingFragment
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull
import kz.fime.samal.utils.extensions.setUpMenu
import timber.log.Timber
 
@AndroidEntryPoint
class NotificationsFragment: BindingFragment<FragmentNotificationsBinding>(FragmentNotificationsBinding::inflate) {

    private val viewModel: NotificationsViewModel by viewModels()
    private var objects: MutableList<Item> = mutableListOf()
    private var page: String = "0"
    private var isLoading = false
    private var nextPage: String? = null



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            toolbar.setUpMenu(R.id.check to { viewModel.notificationsReadAll() })

            srl.setOnRefreshListener {
                srl.isRefreshing = false
            }

            val adapter = NotificationsAdapter {
                val url = it.getOrNull("url","")
                if(url != null){
                    val bundle = bundleOf("url" to it.getOrNull("url", ""))
                    findNavController().navigate(R.id.notificationsInfoFragment, bundle)
                }else{
                    val bundle = bundleOf("message" to it.getOrNull("message", ""))
                    findNavController().navigate(R.id.notificationDetailFragment, bundle)
                }
            }
            rvItems.adapter = adapter

            viewModel.resultNotifications.observe(viewLifecycleOwner) {
                Timber.d("Notifications: %s", it)
                when(it.status) {

                    Status.LOADING -> {}

                    Status.SUCCESS -> {
                        it.data?.let {
                            it.data?.let { it1 ->
                                if (objects.containsAll(it1)) {
                                    Log.d("my", "true")
                                } else {
                                    objects.addAll(it1)
                                    Log.d("my", "false")
                                }
                            }
                            adapter.submitList(objects)
                            srl.isRefreshing = false
                            isLoading = false
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
                                        nextPage = it.links.getOrNull("next", "")
                                        if (nextPage != null) {
                                            if (!isLoading) {
                                                if (visibleItemCount != null && pastVisibleItems != null && totalItemCount != null) {
                                                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                                        val pages = nextPage!!.split("?")
                                                        if (page.toInt() < pages[pages.lastIndex].substring(5).toInt()) {
                                                            page = pages[pages.lastIndex].substring(5)
                                                            viewModel.getNotifications(page)
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

            viewModel.notificationsReadAll.observeState(viewLifecycleOwner, {
                Timber.d("notificationsReadAll: %s", it)
            },{},{
                srl.isRefreshing = it
            })
        }
    }
}