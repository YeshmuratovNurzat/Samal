package kz.fime.samal.ui.profile

import android.os.Bundle
import android.view.View
import kz.fime.samal.databinding.DialogCardDeleteBinding
import kz.fime.samal.utils.binding.BindingBottomSheetFragment

class CardDeleteDialog:  BindingBottomSheetFragment<DialogCardDeleteBinding>(DialogCardDeleteBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            val cardId = arguments?.getString("card_id")
            val cardHash = arguments?.getString("card_hash")

            cardName.text = cardId.toString()
            cardNumber.text = cardHash.toString()

        }
    }
}