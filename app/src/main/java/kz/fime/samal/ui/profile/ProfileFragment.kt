package kz.fime.samal.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kz.fime.samal.R
import kz.fime.samal.data.SessionManager
import kz.fime.samal.data.models.CardsResponse
import kz.fime.samal.data.models.custom.Status
import kz.fime.samal.databinding.FragmentProfileBinding
import kz.fime.samal.ui.AuthActivity
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.utils.EditTextUtils
import kz.fime.samal.utils.EventObserver
import kz.fime.samal.utils.FragmentResultKeys.Companion.CHANGE_EMAIL_BUNDLE_KEY
import kz.fime.samal.utils.FragmentResultKeys.Companion.CHANGE_EMAIL_REQUEST_KEY
import kz.fime.samal.utils.FragmentResultKeys.Companion.CHANGE_NAME_BUNDLE_KEY
import kz.fime.samal.utils.FragmentResultKeys.Companion.CHANGE_NAME_REQUEST_KEY
import kz.fime.samal.utils.FragmentResultKeys.Companion.CHANGE_NUMBER_BUNDLE_KEY
import kz.fime.samal.utils.FragmentResultKeys.Companion.CHANGE_NUMBER_REQUEST_KEY
import kz.fime.samal.utils.FragmentResultKeys.Companion.CHANGE_PHOTO_BUNDLE_KEY
import kz.fime.samal.utils.FragmentResultKeys.Companion.CHANGE_PHOTO_REQUEST_KEY
import kz.fime.samal.utils.RequestCodeEnums
import kz.fime.samal.utils.binding.BindingFragment
import kz.fime.samal.utils.components.BottomBar
import kz.fime.samal.utils.extensions.*
import retrofit2.Response

class ProfileFragment : BindingFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val viewModel: ProfileViewModel by activityViewModels()
    private val viewModel2: ProfileViewModel2 by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        BottomBar.getBottomBar()?.showMain()
        binding.run {

            toolbar.setUpMenu(R.id.log_out to { findNavController().navigate(R.id.action_global_log_out) })

            btnAuth.setOnClickListener {
                startActivity(Intent(requireActivity(), AuthActivity::class.java))
                requireActivity().finish()
            }

            tvOrders.setOnClickListener { findNavController().navigate(R.id.action_global_orders) }
            tvNotifications.setOnClickListener { findNavController().navigate(R.id.action_global_notifications) }
            tvBookmarks.setOnClickListener { findNavController().navigate(R.id.action_global_favorites) }
            vgAddress.setOnClickListener{ findNavController().navigate(R.id.action_global_addresses) }

            vgPhone.setOnClickListener { findNavController().navigate(ProfileFragmentDirections.actionGlobalEditPhone(EditTextUtils.getPhoneUnmasked(tvPhone.toString()))) }
            vgName.onClick { findNavController().navigate(R.id.action_global_edit_name) }
            vgEmail.setOnClickListener { findNavController().navigate(R.id.action_global_edit_email) }
            vgPassword.setOnClickListener {  }
            vgCards.setOnClickListener { findNavController().navigate(R.id.toCardsFragment) }

            vgAboutCompany.setOnClickListener { findNavController().navigate(R.id.toAboutFragment) }
            vgFaq.setOnClickListener { findNavController().navigate(R.id.toFaqFragment) }
            vgContacts.setOnClickListener {  }

            val isAuthorizedVisibility = if (SessionManager.token.isEmpty()) View.GONE else View.VISIBLE
            val isNotAuthorizedVisibility = if (isAuthorizedVisibility == View.VISIBLE) View.GONE else View.VISIBLE
            cvAuth.visibility = isNotAuthorizedVisibility
            vgUserInfo.visibility = isAuthorizedVisibility
            cvPages.visibility = isAuthorizedVisibility
            vgProfile.visibility = isAuthorizedVisibility
            vgProfileInfo.visibility = isAuthorizedVisibility
            vgOrders.visibility = isAuthorizedVisibility
            vgOrdersInfo.visibility = isAuthorizedVisibility


            viewModel.loadData(false)
            viewModel2.loadCards()
            srl.setOnRefreshListener {
                viewModel.loadData(true)
            }

            viewModel.profile.observeState(viewLifecycleOwner, {
                it.result?.let { profile ->
                    tvUserName.text = profile.name
                    tvName.text = profile.name
                    tvUserPhone.text = EditTextUtils.getPhoneMasked(profile.phone)
                    tvPhone.text = EditTextUtils.getPhoneMasked(profile.phone)
                    tvUserBalance.text = profile.points.toBalance()
                    tvEmail.text = if (profile.email.isNullOrEmpty()) "Указать email" else profile.email
                    tvPhoto.text = if (profile.photo.isNullOrEmpty()) "Добавить фотографию" else "Обновить фотографию"
                    ivUserPhoto.loadUrl(profile.photo, R.drawable.ic_avatar_placeholder)
                    vgPhoto.setOnClickListener { findNavController().navigate(ProfileFragmentDirections.toChangePhoto(profile.photo)) }
                }
            }, {

            }, {
                srl.isRefreshing = it
            })

            viewModel.logOut.observe(viewLifecycleOwner, EventObserver {
                SessionManager.logOut()
                startActivity(Intent(requireActivity(), AuthActivity::class.java))
                requireActivity().finish()
            })
        }
        observeFragmentResults()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel2.resultLoadCards.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    it.data?.let {
                        drawCards(it)
                    }
                }
                Status.ERROR -> {

                }
            }
        })
    }

    private fun drawCards(data: Response<CardsResponse>) {
        if (data.code() == 204) {
            binding.cardHint.visibility = View.GONE
            binding.tvCard.text = getString(R.string.add_card)
        } else if (data.code() == 200) {
            binding.cardHint.visibility = View.VISIBLE
            data.body()?.let {
                binding.tvCard.text = it.data[0].card_id
            }
        }
    }

    private fun updateData() {
        viewModel.loadData(true)
    }

    private fun observeFragmentResults() {
        setFragmentResultListener(CHANGE_NUMBER_REQUEST_KEY) { _, bundle ->
            bundle.getString(CHANGE_NUMBER_BUNDLE_KEY)?.let { result ->
                when (result) {
                    ChangeNumberDialog.SUCCESS -> {
                        findNavController().navigate(R.id.to_otp_dialog)
                    }
                }
            }
        }
        setFragmentResultListener(CHANGE_NAME_REQUEST_KEY) {_, bundle ->
            bundle.getString(CHANGE_NAME_BUNDLE_KEY)?.let { result ->
                when (result) {
                    ChangeNameDialog.SUCCESS -> {
                        updateData()
                    }
                }
            }
        }
        setFragmentResultListener(CHANGE_EMAIL_REQUEST_KEY) { _, bundle ->
            bundle.getString(CHANGE_EMAIL_BUNDLE_KEY)?.let { result ->
                when (result) {
                    ChangeEmailDialog.SUCCESS -> {
                        updateData()
                    }
                }
            }
        }
        setFragmentResultListener(CHANGE_PHOTO_REQUEST_KEY) { _, bundle ->
            bundle.getString(CHANGE_PHOTO_BUNDLE_KEY)?.let { result ->
                when (result) {
                    "success" -> {
                        updateData()
                    }
                }
            }
        }
    }

}