package kz.fime.samal.ui.cart


import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kz.fime.samal.R
import kz.fime.samal.data.models.custom.Status
import kz.fime.samal.databinding.DialogPickUpLocationsBinding
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.ui.cart.adapters.PickUpAdapter
import kz.fime.samal.ui.profile.ChangeNameDialog
import kz.fime.samal.utils.FragmentResultKeys
import kz.fime.samal.utils.binding.BindingBottomSheetFragment
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull
import kz.fime.samal.utils.extensions.isLoading
import kz.fime.samal.utils.extensions.loadMapScreenshot

class PickUpLocationsDialog: BindingBottomSheetFragment<DialogPickUpLocationsBinding>(DialogPickUpLocationsBinding::inflate) {

    private val viewModel: CartViewModel by activityViewModels()
    private var selectedItem: Item? = null
    private var latitude = 0.0
    private var longitude = 0.0
    private var address = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {

            viewModel.pickUpLocations.call()

            val adapter = PickUpAdapter {
                selectedItem = it
                val location = it["location"]
                val loc = location as List<*>
                latitude = loc[0] as Double
                longitude = loc[1] as Double
                address = it.getOrNull("address","").toString()
                imagePickUp.loadMapScreenshot(latitude, longitude)
            }
            rvPickUp.adapter = adapter

            btnSave.setOnClickListener {
                viewModel.fetchPickUp?.postValue(selectedItem)
                dismiss()
            }

            viewModel.pickUpLocations.liveData.observeState(viewLifecycleOwner, {
                adapter.submitList(it.result)
            })
        }
    }
}