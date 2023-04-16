package kz.fime.samal.ui.profile.notifications

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kz.fime.samal.R
import kz.fime.samal.databinding.FragmentNotificationDetailBinding
import kz.fime.samal.databinding.FragmentNotificationsInfoBinding
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.utils.binding.BindingBottomSheetFragment
import kz.fime.samal.utils.extensions.getOrNull

@AndroidEntryPoint
class NotificationDetailFragment : BindingBottomSheetFragment<FragmentNotificationDetailBinding>(FragmentNotificationDetailBinding::inflate) {

    private val viewModel: NotificationsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            val message = arguments?.getString("message")
            tvDescription.text = message.toString()

//            viewModel.notificationRead(message!!)
//
//            viewModel.notificationRead.observeState(viewLifecycleOwner,{
//                tvDescription.text = it.result.getOrNull("message","")
//            }){
//            }

            btnClose.setOnClickListener {
                dismiss()
            }
        }
    }
}