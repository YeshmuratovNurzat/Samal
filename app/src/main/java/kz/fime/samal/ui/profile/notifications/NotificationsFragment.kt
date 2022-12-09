package kz.fime.samal.ui.profile.notifications

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kz.fime.samal.databinding.FragmentNotificationsBinding
import kz.fime.samal.databinding.FragmentOrdersBinding
import kz.fime.samal.utils.binding.BindingFragment

@AndroidEntryPoint
class NotificationsFragment: BindingFragment<FragmentNotificationsBinding>(FragmentNotificationsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            srl.setOnRefreshListener {
                srl.isRefreshing = false
            }
        }
    }
}