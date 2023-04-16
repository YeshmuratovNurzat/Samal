package kz.fime.samal.ui.profile.address

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kz.fime.samal.R
import kz.fime.samal.data.models.City
import kz.fime.samal.databinding.FragmentAddressesBinding
import kz.fime.samal.ui.base.observeEvent
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.utils.MessageUtils
import kz.fime.samal.utils.binding.BindingFragment
import kz.fime.samal.utils.extensions.InnerItem
import kz.fime.samal.utils.extensions.getOrNull
import timber.log.Timber

@AndroidEntryPoint
class AddressesFragment: BindingFragment<FragmentAddressesBinding>(FragmentAddressesBinding::inflate) {

    private val viewModel: AddressesViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

            viewModel.load()
            srl.setOnRefreshListener { viewModel.load() }

            val addressesAdapter = AddressesAdapter {
                findNavController().navigate(R.id.action_global_addressDetailDialog,
                    bundleOf(Pair("address_slug", it.getOrNull("address_slug", ""))))
            }
            rvItems.adapter = addressesAdapter
            viewModel.addresses.observeState(viewLifecycleOwner, {
                if (it.result.isNullOrEmpty()) {
                    vgMain.visibility = View.GONE
                    contentEmpty.root.visibility = View.VISIBLE
                } else {
                    vgMain.visibility = View.VISIBLE
                    contentEmpty.root.visibility = View.GONE
                }
                addressesAdapter.submitList(it.result)
            }, {}, { srl.isRefreshing = it })

            viewModel.addAddress.observe(viewLifecycleOwner){
                Log.d("MyLog","addAddress == ${it.peekContent()}")
            }

            viewModel.updateAddress.observe(viewLifecycleOwner){
                Log.d("MyLog","updateAddress == ${it.peekContent()}")
            }

            viewModel.deleteAddress.observeEvent(viewLifecycleOwner, {
                Log.d("MyLog","deleteAddress == ${it.result}")
            })

            val addAddress = { _: View ->
                findNavController().navigate(R.id.action_global_add_address)
            }
            btnAddAddress.setOnClickListener(addAddress)
            contentEmpty.btnAddAddress.setOnClickListener(addAddress)
        }
    }
}