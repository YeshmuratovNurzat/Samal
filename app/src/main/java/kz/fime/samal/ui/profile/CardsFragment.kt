package kz.fime.samal.ui.profile

import android.os.Bundle
import android.os.PerformanceHintManager
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kz.fime.samal.R
import kz.fime.samal.data.SessionManager
import kz.fime.samal.data.models.CardsResponse
import kz.fime.samal.data.models.custom.Status
import kz.fime.samal.databinding.FragmentCardsBinding
import kz.fime.samal.ui.base.handleException
import kz.fime.samal.ui.cart.adapters.CardAdapter
import kz.fime.samal.utils.binding.BindingFragment
import retrofit2.Response

class CardsFragment : BindingFragment<FragmentCardsBinding>(FragmentCardsBinding::inflate) {

    private val viewModel: ProfileViewModel2 by activityViewModels()
    private var url: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadCards()
        observeViewModel()
        initView()
    }

    private fun initView() {
        binding.run {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            addCardBtn.setOnClickListener {
                addCard()
            }
            addCardEmptyBtn.setOnClickListener {
                addCard()
            }
        }
    }

    private fun addCard() {
        findNavController().navigate(R.id.addCardFragment, bundleOf("url" to url))
    }

    private fun observeViewModel() {
        viewModel.addCard()
        viewModel.resultLoadCards.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    it.data?.let {
                        drawCards(it)
                        val cardsAdapter = CardsAdapter{ card ->
                            findNavController().navigate(R.id.cardDeleteDialog,
                                bundleOf(Pair("card_id",card.card_id),Pair("card_hash",card.card_hash)))
                        }
                        binding.cardsRv.adapter = cardsAdapter
                        cardsAdapter.submitList(it.body()?.data)
                    }
                }
                Status.ERROR -> {
                    handleException(it.exception)
                }
            }
        })
        viewModel.resultAddCard.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    it.data?.let {
                        url = it.data.url
                        SessionManager.setUrlAddCard(it.data.url)
                        //val bundle = bundleOf("url" to it.data.url)
                        //findNavController().navigate(R.id.addCardFragment, bundle)
                    }
                }
                Status.ERROR -> {
                    handleException(it.exception)
                }
            }
        })
    }

    private fun drawCards(data: Response<CardsResponse>) {
        binding.run {
            if (data.code() == 204) {
                emptyLayout.visibility = View.VISIBLE
                cardsLayout.visibility = View.GONE
            } else if (data.code() == 200) {
                emptyLayout.visibility = View.GONE
                cardsLayout.visibility = View.VISIBLE
            }
        }
    }

}