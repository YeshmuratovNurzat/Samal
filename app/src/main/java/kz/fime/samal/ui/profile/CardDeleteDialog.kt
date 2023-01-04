package kz.fime.samal.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import kz.fime.samal.data.models.custom.Status
import kz.fime.samal.databinding.DialogCardDeleteBinding
import kz.fime.samal.utils.binding.BindingBottomSheetFragment
import kz.fime.samal.utils.extensions.isLoading
import kz.fime.samal.utils.extensions.progressButton

class CardDeleteDialog:  BindingBottomSheetFragment<DialogCardDeleteBinding>(DialogCardDeleteBinding::inflate) {

    private val viewModel: ProfileViewModel2 by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            progressButton(btnDeleteCard)
            val cardId = arguments?.getString("card_id")
            val cardHash = arguments?.getString("card_hash")

            cardName.text = cardId.toString()
            cardNumber.text = cardHash.toString()

            btnDeleteCard.setOnClickListener {
                viewModel.deleteCard(cardId.toString())
            }

            observeViewModel()
        }
    }

    private fun observeViewModel() {
        viewModel.resultDeleteCard.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.btnDeleteCard.isLoading(true)
                }
                Status.SUCCESS -> {
                    binding.btnDeleteCard.isLoading(false)
                }
                Status.ERROR -> {
                    binding.btnDeleteCard.isLoading(false)
                }
            }
        })
    }
}