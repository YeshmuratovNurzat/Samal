package kz.fime.samal.ui.cart

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kz.fime.samal.R
import kz.fime.samal.data.SessionManager
import kz.fime.samal.data.base.State
import kz.fime.samal.databinding.DialogCartOrderBinding
import kz.fime.samal.ui.base.observeEvent
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.ui.cart.adapters.*
import kz.fime.samal.ui.cart.order.OrderInfoDialog
import kz.fime.samal.ui.profile.address.AddressesAdapter
import kz.fime.samal.ui.profile.address.AddressesViewModel
import kz.fime.samal.utils.MessageUtils
import kz.fime.samal.utils.binding.BindingBottomSheetFragment
import kz.fime.samal.utils.extensions.InnerItem
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull
import kz.fime.samal.utils.extensions.loadMapScreenshot
import timber.log.Timber

class OrderCartDialog: BindingBottomSheetFragment<DialogCartOrderBinding>(DialogCartOrderBinding::inflate) {

    private val viewModel: CartViewModel by activityViewModels()
    private val addressViewModel: AddressesViewModel by activityViewModels()

    private var showPaymentMethod = false
    private var showDeliveryMethod = false
    private var useBonus = false
    private var deliverySlug = ""
    private var paymentSlug = ""
    private var addressSlug = ""
    private var installmentId = ""
    private var pickUpPointSlug = ""
    private var installmentPercent = 1
    private var baskets = listOf<String>()
    private var priceProduct = 0
    private var quota = 0
    private var price = 0
    private var priceTotal = 0
    private lateinit var list : MutableList<CartItem>
    private lateinit var listAddress : MutableList<Item>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {

            val cartOptionsAdapter = CartOptionAdapter()
            rvItems.adapter = cartOptionsAdapter

            val cardAdapter = CardAdapter {
                tvPaymentMethod.text = "Картой\n•••• ${it.getOrNull("card_hash","")?.substring(12,16)}"
                iv.setImageResource(R.drawable.ic_master_card)
                rvInstallment.visibility = View.GONE
                paymentSlug = it.getOrNull("slug","card")!!
                calculatePrice(cartOptionsAdapter)
            }
            rvCard.adapter = cardAdapter

            val installmentAdapter = InstallmentAdapter {
                tvPaymentMethod.text = it.getOrNull("short_description", "")
                iv.setImageResource(R.drawable.ic_home_credit_bank)
                paymentSlug = it.getOrNull("slug", "installment")!!
                installmentId = it.getOrNull("uuid","")!!
                it.getOrNull("percent","")?.toIntOrNull()?.let {
                    installmentPercent = it
                    calculatePrice(cartOptionsAdapter, it)
                }
            }
            rvInstallment.adapter = installmentAdapter

            val paymentOptionsAdapter = PaymentOptionAdapter {
                when(it.getOrNull("payment_id",1.0)){
                    1.0 -> {
                        iv.setImageResource(R.drawable.ic_cash)
                        rvInstallment.visibility = View.GONE
                    }
                    2.0 -> {
                        iv.setImageResource(R.drawable.ic_baseline_add_24)
                        rvInstallment.visibility = View.GONE
                        //findNavController().navigate(R.id.addCardFragment, bundleOf("url" to SessionManager.getUrlAddCard()))
                        findNavController().navigate(R.id.action_global_cardsFragment)
                    }
                    5.0 -> {
                        iv.setImageResource(R.drawable.ic_installment)
                        rvInstallment.visibility = View.VISIBLE
                    }
                }
                tvPaymentMethod.text = it.getOrNull("name", "")
                paymentSlug = it.getOrNull("slug", "")!!
                calculatePrice(cartOptionsAdapter)
            }
            rvPaymentOptions.adapter = paymentOptionsAdapter

            val addressesAdapter = AddressesAdapter {
                tvDeliveryInfo.text = "Доставка курьером, ${it.getOrNull("name", "")}"
                cardView.visibility = View.GONE
                val address = it.getOrNull("address_slug", "")!!
                addressSlug = address
                viewModel.getDeliveryCost(address)
                pickUpPointSlug = ""
                if (viewModel.deliveryTypes.liveData.value is State.Success) {
                    val types = (viewModel.deliveryTypes.liveData.value as State.Success).result
                    types?.forEach {
                        if (it.getOrNull("delivery_id", 0)!! == 2) {
                            deliverySlug = it.getOrNull("slug", "")!!
                        }
                    }
                }
            }
            rvDeliveryOptions.adapter = addressesAdapter

            viewModel.deliveryCost.observeEvent(viewLifecycleOwner, {
                val price = it.result?.getOrNull("quota", 0)!!
                list = cartOptionsAdapter.getList() as MutableList
                quota = price
                for(i in 0 until list.size) {
                    if (list[i].count == -1) {
                        list.removeAt(i)
                    }
                }
                list.add(list.lastIndex + 1, CartItem(-1, "Доставка", price))
                cartOptionsAdapter.submitList(list)
                tvToPay.text = "К оплате ${priceProduct + price}₸"
            }, {
                val bundle = bundleOf(
                    OrderInfoDialog.KEY_TITLE to "Ошибка",
                    OrderInfoDialog.KEY_DESC to it.message,
                    OrderInfoDialog.KEY_BUTTON_TEXT to "Понятно, закрыть",
                    OrderInfoDialog.KEY_IMAGE to R.drawable.ic_cat_order_error)
                findNavController().navigate(R.id.action_global_order_info, bundle)
            })

            vgDeliveryPickUp.setOnClickListener {
                findNavController().navigate(R.id.action_global_pickUpLocationsDialog)
            }

            vgAddNewAddress.setOnClickListener {
                findNavController().navigate(R.id.action_global_add_address)
            }

            btnOk.setOnClickListener {
                if(baskets != null && paymentSlug != null && priceProduct != null){
                    dismiss()
                }
                viewModel.placeOrder(baskets, deliverySlug, pickUpPointSlug, paymentSlug, installmentId, addressSlug, priceProduct, quota)
            }

            viewModel.run {
                clientPoints.call()
                paymentCards.call()
                deliveryTypes.call()
                paymentTypes.call()
                clientAddresses.call()
                pickUpLocations.call()
                installment.call()

                clientPoints.liveData.observeState(viewLifecycleOwner, {
                    tvBonus.text = "${it.result.getOrNull("points", "")} Б"
                })
                paymentCards.liveData.observeState(viewLifecycleOwner, {
                    Timber.d("Cards: %s", it)
                    cardAdapter.submitList(it.result)
                })
                deliveryTypes.liveData.observeState(viewLifecycleOwner, {
                    Timber.d("Delivery: %s", it)
                })
                paymentTypes.liveData.observeState(viewLifecycleOwner, {
                    Timber.d("Payment: %s", it)
                    paymentOptionsAdapter.submitList(it.result)
                })
                clientAddresses.liveData.observeState(viewLifecycleOwner, {
                    Timber.d("Addresses: %s", it)
                    addressesAdapter.submitList(it.result)
                    addressesAdapter.notifyDataSetChanged()
                })
                pickUpLocations.liveData.observeState(viewLifecycleOwner, {
                    Timber.d("Pickups: %s", it)
                })
                installment.liveData.observeState(viewLifecycleOwner,{
                    Timber.d("Installment: %s", it)
                    installmentAdapter.submitList(it.result)
                })
                placeOrder.observeEvent(viewLifecycleOwner, {
                    MessageUtils.postMessage("Успешно")
                    dismiss()
                })

                addressViewModel.addresses.observeState(viewLifecycleOwner,{
                    addressesAdapter.submitList(it.result)
                    addressesAdapter.notifyDataSetChanged()
                }){}

                viewModel.cartItems.observeState(viewLifecycleOwner, {
                    var toPay = 0
                    val items = mutableListOf<CartItem>()
                    val list = mutableListOf<String>()
                    it.result?.forEach {
                        it.getOrNull<List<InnerItem>>("products")?.forEach {
                            list.add(it.getOrNull("basket_uuid", "")!!)
                            val item = CartItem(it.getOrNull("count", 0, safe = true)!!,
                                it.getOrNull("name", ""),
                                it.getOrNull("price", 0, safe = true)!!)
                            items.add(item)
                            toPay += item.price * item.count
                            price = toPay
                            priceTotal = item.count * item.price
                        }
                    }
                    baskets = list
                    priceProduct = toPay
                    Timber.d("Go: %s", items)
                    tvToPay.text = "К оплате ${toPay}₸"
                    tvItemCount.text = "${items.size} позиций"
                    cartOptionsAdapter.submitList(items)
                })

                viewModel.fetchPickUp?.observe(viewLifecycleOwner) {
                    if (it != null) {
                        tvDeliveryInfo.text = "Пункт самовывоза, ${it.getOrNull("address", "")}"
                        val location = it["location"]
                        val loc = location as List<*>
                        val latitude = loc[0] as Double
                        val longitude = loc[1] as Double
                        cardView.visibility = View.VISIBLE
                        imagePickUp.loadMapScreenshot(latitude, longitude)

                        list = cartOptionsAdapter.getList() as MutableList
                        for (i in 0 until list.size) {
                            if (list[i].title == "Доставка") {
                                list.removeAt(i)
                                break
                            }
                        }
                        cartOptionsAdapter.submitList(list)

                        addressSlug = ""
                        quota = 0
                        pickUpPointSlug = it.getOrNull("slug", "")!!
                        val pricePickUp = it.getOrNull("price", 0)!!
                        //priceProduct = price + pricePickUp
                        tvToPay.text = "К оплате ${priceProduct}₸"

                        if (viewModel.deliveryTypes.liveData.value is State.Success) {
                            val types =
                                (viewModel.deliveryTypes.liveData.value as State.Success).result
                            types?.forEach {
                                if (it.getOrNull("delivery_id", 0)!! == 1) {
                                    deliverySlug = it.getOrNull("slug", "")!!
                                }
                            }
                        }
                    }
                }
            }

            useBonuses(useBonus)
            vgBonus.setOnClickListener {
                useBonus = !useBonus
                useBonuses(useBonus)
            }

            showPaymentMethod(showPaymentMethod)
            vgPaymentMethod.setOnClickListener {
                showPaymentMethod = !showPaymentMethod
                showPaymentMethod(showPaymentMethod)
            }

            showDeliveryMethods(showDeliveryMethod)
            vgDeliveryMethod.setOnClickListener {
                showDeliveryMethod = !showDeliveryMethod
                showDeliveryMethods(showDeliveryMethod)
            }
        }
    }

    private fun calculatePrice(cartOptionsAdapter: CartOptionAdapter, percent: Int? = null) {
        list = cartOptionsAdapter.getList() as MutableList

        if(percent != null){

            priceProduct = price + (price * percent / 100)

            val installmentPrice = price * percent / 100

            for (i in 0 until list.size) {
                if (list[i].title == "Комиссия банка") {
                    list.removeAt(i)
                    break
                }
            }

            if(list.lastOrNull()?.title == "Доставка"){
                list.add(list.size - 1,CartItem(-2,"Комиссия банка", installmentPrice))
            }else{
                list.add(CartItem(-2,"Комиссия банка", installmentPrice))
            }
            binding.tvToPay.text = "К оплате ${priceProduct + quota}₸"
        }else{
            for (i in 0 until list.size) {
                if (list[i].title == "Комиссия банка") {
                    list.removeAt(i)
                    break
                }
            }
            binding.tvToPay.text = "К оплате ${price + quota}₸"
        }
        cartOptionsAdapter.submitList(list)
    }

    private fun showDeliveryMethods(show: Boolean) {
        binding.run {
//            TransitionManager.beginDelayedTransition(vgMainContainer, TransitionSet()
//                .addTransition(ChangeBounds())
//                .addTransition(Fade()))
            if (show){
                vgDeliveryOptions.visibility = View.VISIBLE
                ivDeliveryMethodDropdown.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.gray_8))
                ivDeliveryMethodDropdown.setImageResource(R.drawable.ic_arrow_up_24)
            } else {
                vgDeliveryOptions.visibility = View.GONE
                ivDeliveryMethodDropdown.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.gray_72))
                ivDeliveryMethodDropdown.setImageResource(R.drawable.ic_arrow_down_24)
            }
        }
    }

    private fun showPaymentMethod(show: Boolean) {
        binding.run {
//            TransitionManager.beginDelayedTransition(vgSv, TransitionSet()
//                .addTransition(ChangeBounds())
//                .addTransition(Fade()))
            if (show){
                rvPaymentOptions.visibility = View.VISIBLE
                rvCard.visibility = View.VISIBLE
                vgPaymentMethod.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.gray_93))
                ivPaymentMethodDropdown.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.gray_8))
                ivPaymentMethodDropdown.setImageResource(R.drawable.ic_arrow_up_24)
            } else {
                rvPaymentOptions.visibility = View.GONE
                rvCard.visibility = View.GONE
                vgPaymentMethod.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.gray_98))
                ivPaymentMethodDropdown.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.gray_72))
                ivPaymentMethodDropdown.setImageResource(R.drawable.ic_arrow_down_24)
            }
        }
    }

    private fun useBonuses(enabled: Boolean){
        binding.run {
            if (enabled) {
                vgBonus.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.gray_93))
                ivBonus.setImageResource(R.drawable.ic_bonus_enabled_24)
                tvBonusInfo.text = "Тратить"
            } else {
                vgBonus.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.gray_98))
                ivBonus.setImageResource(R.drawable.ic_bonus_disabled_24)
                tvBonusInfo.text = "Не тратить"
            }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.fetchPickUp?.value = null
    }

}