package kz.fime.samal.ui.profile.favorites

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kz.fime.samal.databinding.DialogCartClearBinding
import kz.fime.samal.databinding.DialogFavoritesClearBinding
import kz.fime.samal.databinding.DialogQrInfoBinding
import kz.fime.samal.utils.binding.BindingBottomSheetFragment

@AndroidEntryPoint
class ClearFavoritesDialog: BindingBottomSheetFragment<DialogFavoritesClearBinding>(DialogFavoritesClearBinding::inflate) {

    private val viewModel: FavoritesViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            btnOk.setOnClickListener {
                viewModel.clearFavorites()
                dismiss()
            }
        }
    }

}