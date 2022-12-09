package kz.fime.samal.ui.profile.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kz.fime.samal.databinding.FragmentOrdersBinding
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.utils.binding.BindingFragment
import kz.fime.samal.utils.extensions.getOrNull
import timber.log.Timber

@AndroidEntryPoint
class OrdersFragment: BindingFragment<FragmentOrdersBinding>(FragmentOrdersBinding::inflate) {

    private val viewModel: OrdersViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

            viewModel.load()
            srl.setOnRefreshListener { viewModel.load() }

            val ordersAdapter = OrdersAdapter {
                findNavController().navigate(OrdersFragmentDirections.toOrderDetailsFragment(it.getOrNull("order_uuid", "")!!, it.getOrNull("number", 0) as Int))
            }
            rvItems.adapter = ordersAdapter
            viewModel.orders.observeState(viewLifecycleOwner, {
                Timber.d("Orders: %s", it)
                if (it.result.isNullOrEmpty()) {
                    rvItems.visibility = View.GONE
                    contentEmpty.root.visibility = View.VISIBLE
                } else {
                    rvItems.visibility = View.VISIBLE
                    contentEmpty.root.visibility = View.GONE
                }
                ordersAdapter.submitList(it.result)
            }, {}, { srl.isRefreshing = it })


        }
    }
}