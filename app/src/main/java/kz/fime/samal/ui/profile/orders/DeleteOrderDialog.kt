package kz.fime.samal.ui.profile.orders

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.navigateUp
import kz.fime.samal.R
import kz.fime.samal.databinding.DialogCancelOrderBinding
import kz.fime.samal.utils.binding.BindingBottomSheetFragment

class DeleteOrderDialog: BindingBottomSheetFragment<DialogCancelOrderBinding>(DialogCancelOrderBinding::inflate) {

    private val viewModel: OrdersViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {

            val id = arguments?.getString("id")

            btnDelete.setOnClickListener {
                viewModel.cancelOrder(id.toString())
                dismiss()
                findNavController().popBackStack(R.id.orders, false)
            }
        }
    }
}