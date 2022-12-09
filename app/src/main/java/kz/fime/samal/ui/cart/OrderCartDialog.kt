package kz.fime.samal.ui.cart

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kz.fime.samal.R
import kz.fime.samal.data.base.State
import kz.fime.samal.databinding.DialogCartOrderBinding
import kz.fime.samal.ui.base.observeEvent
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.ui.cart.adapters.CartItem
import kz.fime.samal.ui.cart.adapters.CartOptionAdapter
import kz.fime.samal.ui.cart.adapters.PaymentOptionAdapter
import kz.fime.samal.ui.profile.address.AddressesAdapter
import kz.fime.samal.utils.MessageUtils
import kz.fime.samal.utils.binding.BindingBottomSheetFragment
import kz.fime.samal.utils.extensions.InnerItem
import kz.fime.samal.utils.extensions.getOrNull
import timber.log.Timber

class OrderCartDialog: BindingBottomSheetFragment<DialogCartOrderBinding>(DialogCartOrderBinding::inflate) {

    private val viewModel: CartViewModel by activityViewModels()

    private var showPaymentMethod = false
    private var showDeliveryMethod = false
    private var useBonus = false

    private var deliverySlug = ""
    private var paymentSlug = ""
    private var addressSlug = ""
    private var baskets = listOf<String>()
    private var priceProduct = 0
    private var quota = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            val cartOptionsAdapter = CartOptionAdapter()
            rvItems.adapter = cartOptionsAdapter

            val paymentOptionsAdapter = PaymentOptionAdapter {
                tvPaymentMethod.text = it.getOrNull("name", "")
                paymentSlug = it.getOrNull("slug", "")!!
            }
            rvPaymentOptions.adapter = paymentOptionsAdapter

            val addressesAdapter = AddressesAdapter {
                tvDeliveryInfo.text = "Доставка курьером, ${it.getOrNull("name", "")}"
                val a = it.getOrNull("address_slug", "")!!
                addressSlug = a
                viewModel.getDeliveryCost(a)
                if (viewModel.deliveryTypes.liveData.value is State.Success) {
                    val types = (viewModel.deliveryTypes.liveData.value as State.Success).result
                    types?.forEach {
                        if (it.getOrNull("delivery_id", 0)!! == 2) {
                            deliverySlug = it.getOrNull("slug", "")!!
                    }
                    }
                }
            }
            viewModel.deliveryCost.observeEvent(viewLifecycleOwner, {
                val price = it.result?.getOrNull("quota", 0)!!
                quota = price
                val list = cartOptionsAdapter.getList() as MutableList
                if (list.firstOrNull()?.count == -1) {
                    list.removeAt(0)
                }
                list.add(0, CartItem(-1, "Доставка", price))
                cartOptionsAdapter.submitList(list)
                tvToPay.text = "К оплате ${priceProduct + price}₸"
            })
            rvDeliveryOptions.adapter = addressesAdapter

            vgDeliveryPickUp.setOnClickListener {

            }
            vgAddNewAddress.setOnClickListener {
                findNavController().navigate(R.id.action_global_add_address)
            }

            btnOk.setOnClickListener {
                if (baskets.isEmpty() || deliverySlug.isEmpty() || paymentSlug.isEmpty() || addressSlug.isEmpty()) {
                    MessageUtils.postMessage("Заполните все поля")
                    return@setOnClickListener
                }
                viewModel.placeOrder(baskets, deliverySlug, paymentSlug, addressSlug, priceProduct, quota)
                dismiss()
            }
            viewModel.placeOrder.observeEvent(viewLifecycleOwner, {
                MessageUtils.postMessage("Успешно")
            })

            viewModel.run {
                clientPoints.call()
                paymentCards.call()
                deliveryTypes.call()
                paymentTypes.call()
                clientAddresses.call()
                pickUpLocations.call()

                clientPoints.liveData.observeState(viewLifecycleOwner, {
                    tvBonus.text = "${it.result.getOrNull("points", "")} Б"
                })
                paymentCards.liveData.observeState(viewLifecycleOwner, {
                    Timber.d("Cards: %s", it)
                })
                deliveryTypes.liveData.observeState(viewLifecycleOwner, {
                    Timber.d("Delivery: %s", it)
                })
                paymentTypes.liveData.observeState(viewLifecycleOwner, {
                    paymentOptionsAdapter.submitList(it.result)
                })
                clientAddresses.liveData.observeState(viewLifecycleOwner, {
                    Timber.d("Addresses: %s", it)
                    addressesAdapter.submitList(it.result)
                })
                pickUpLocations.liveData.observeState(viewLifecycleOwner, {
                    Timber.d("Pickups: %s", it)
                })


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
                            toPay += item.price
                        }
                    }
                    baskets = list
                    priceProduct = toPay
                    Timber.d("Go: %s", items)
                    tvToPay.text = "К оплате ${toPay}₸"
                    tvItemCount.text = "${items.size} позиций"
                    cartOptionsAdapter.submitList(items)
                })

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

//            btnOk.setOnClickListener {
//                dismiss()
//            }
        }
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
                vgPaymentMethod.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.gray_93))
                ivPaymentMethodDropdown.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.gray_8))
                ivPaymentMethodDropdown.setImageResource(R.drawable.ic_arrow_up_24)
            } else {
                rvPaymentOptions.visibility = View.GONE
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
}