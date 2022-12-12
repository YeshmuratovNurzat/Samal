package kz.fime.samal.ui.profile.address

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.activityViewModels
import kz.fime.samal.R
import kz.fime.samal.data.base.State
import kz.fime.samal.databinding.DialogAddAddressBinding
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.ui.cart.CartViewModel
import kz.fime.samal.utils.MessageUtils
import kz.fime.samal.utils.binding.BindingBottomSheetFragment
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull
import timber.log.Timber

class AddAddressDialog: BindingBottomSheetFragment<DialogAddAddressBinding>(DialogAddAddressBinding::inflate) {

    private val viewModel: AddressesViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {

            viewModel.load()
            viewModel.cities.observeState(viewLifecycleOwner, {
                val items = mutableListOf<String>()
                it.result?.forEach {
                    items.add(it.getOrNull("name", "")!!)
                }
                val adapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, items)
                etCity.setAdapter(adapter)
            })


            btnSave.setOnClickListener {
                val name = etName.text.toString()
                val city = etCity.text.toString()
                val street = etStreet.text.toString()
                val houseNumber = etHouse.text.toString()
                val apartment = etApartment.text.toString()
                if (name.isEmpty() || city.isEmpty() || street.isEmpty() || houseNumber.isEmpty() || apartment.isEmpty()) {
                    MessageUtils.postMessage("Заполните все поля!")
                    return@setOnClickListener
                }
                if (viewModel.cities.value is State.Success) {
                    val cityId = ((viewModel.cities.value as State.Success<List<Item>>).result?.first { it.getOrNull("name", "")!! == city }).getOrNull("id", 0)!!

                    viewModel.addAddress(cityId, name, street, houseNumber, apartment, false,"43.231633","76.854583")
                    dismiss()
                }

            }
        }
    }

}