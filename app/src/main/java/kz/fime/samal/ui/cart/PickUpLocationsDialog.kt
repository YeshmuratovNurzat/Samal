package kz.fime.samal.ui.cart


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import kz.fime.samal.databinding.DialogPickUpLocationsBinding
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.ui.cart.adapters.PickUpAdapter
import kz.fime.samal.utils.binding.BindingBottomSheetFragment
import kz.fime.samal.utils.extensions.loadMapScreenshot

class PickUpLocationsDialog: BindingBottomSheetFragment<DialogPickUpLocationsBinding>(DialogPickUpLocationsBinding::inflate) {

    private val viewModel: CartViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {

            viewModel.pickUpLocations.call()

            val adapter = PickUpAdapter {
                val location = it["location"]
                val loc = location as List<*>
                val latitude = loc[0] as Double
                val longitude = loc[1] as Double
                imagePickUp.loadMapScreenshot(latitude, longitude)
            }
            rvPickUp.adapter = adapter

            viewModel.pickUpLocations.liveData.observeState(viewLifecycleOwner, {
                adapter.submitList(it.result)
            })
        }
    }
}