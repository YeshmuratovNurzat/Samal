package kz.fime.samal.ui.cart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kz.fime.samal.R
import kz.fime.samal.data.SessionManager
import kz.fime.samal.databinding.FragmentCartBinding
import kz.fime.samal.ui.AuthActivity
import kz.fime.samal.ui.base.observeEvent
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.ui.cart.adapters.CartAdapter
import kz.fime.samal.ui.cart.order.OrderInfoDialog
import kz.fime.samal.utils.MessageUtils
import kz.fime.samal.utils.binding.BindingFragment
import kz.fime.samal.utils.components.BottomBar
import kz.fime.samal.utils.extensions.InnerItem
import kz.fime.samal.utils.extensions.Item
import kz.fime.samal.utils.extensions.getOrNull
import kz.fime.samal.utils.extensions.setUpMenu
import timber.log.Timber

@AndroidEntryPoint
class CartFragment: BindingFragment<FragmentCartBinding>(FragmentCartBinding::inflate) {

    private val viewModel: CartViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        BottomBar.getBottomBar()?.showMain()
//        findNavController().navigate(R.id.action_global_installmentFragment, null)
        binding.run {
            if (SessionManager.token.isEmpty()) {
                vgErrorAuth.visibility = View.VISIBLE
                vgMain.visibility = View.INVISIBLE
                binding.btnAuth.setOnClickListener {
                    startActivity(Intent(requireActivity(), AuthActivity::class.java))
                    //requireActivity().finish()
                }
            } else {
                toolbar.setUpMenu(R.id.clear to { findNavController().navigate(R.id.action_global_clear_cart) })
                contentEmpty.btnGoToCatalog.setOnClickListener {
                    BottomBar.getBottomBar()?.selectItemIndex(1)
                }
                btnOrder.setOnClickListener { findNavController().navigate(R.id.action_global_order_cart) }

                viewModel.loadCart()
                srl.setOnRefreshListener { viewModel.loadCart() }

                val cartAdapter = CartAdapter(onShopClick = {
                    findNavController().navigate(
                        R.id.action_global_shop,
                        bundleOf(Pair("id", it.getOrNull("shop_uuid", "")))
                    )
                }, {}, { item, adapter ->
                    val count = item.getOrNull("count", 0)!! + 1
                    viewModel.updateCartItem(item.getOrNull("basket_uuid", "")!!, count)
                }, { item, adapter ->
                    val count = item.getOrNull("count", 0)!! - 1
                    viewModel.updateCartItem(item.getOrNull("basket_uuid", "")!!, count)
                })

                viewModel.updateCartItem.observeEvent(viewLifecycleOwner, {
                    MessageUtils.postMessage(it.result.getOrNull("message", ""))
                    viewModel.loadCart()
                })

                viewModel.cartItems.observeState(viewLifecycleOwner, {
                    if (it.result.isNullOrEmpty()) {
                        contentEmpty.root.visibility = View.VISIBLE
                        vgMain.visibility = View.GONE
                    } else {
                        contentEmpty.root.visibility = View.GONE
                        vgMain.visibility = View.VISIBLE

                        Timber.d("Got: %s", it)
                        cartAdapter.submitList(it.result)
                    }
                }, {}, {
                    srl.isRefreshing = it
                })
                rvItems.adapter = cartAdapter

                viewModel.clearCart.observeState(viewLifecycleOwner, {
                    contentEmpty.root.visibility = View.VISIBLE
                    vgMain.visibility = View.GONE
                }, {}, {})

                viewModel.placeOrder.observeEvent(viewLifecycleOwner, {
                    MessageUtils.postMessage("Успешно")
                    viewModel.loadCart()
                    if (it.result.getOrNull("url", "") != null) {
                        val bundle = bundleOf("url_installment" to it.result.getOrNull("url", ""))
                        findNavController().navigate(R.id.action_global_installmentFragment, bundle)
                    } else {
                        findNavController().navigate(R.id.action_global_order_info)
                    }
                }, {
                    val bundle = bundleOf(
                        OrderInfoDialog.KEY_TITLE to "Ошибка",
                        OrderInfoDialog.KEY_DESC to it.message,
                        OrderInfoDialog.KEY_BUTTON_TEXT to "Понятно, закрыть",
                        OrderInfoDialog.KEY_IMAGE to R.drawable.ic_cat_order_error
                    )
                    findNavController().navigate(R.id.action_global_order_info, bundle)
                })
            }
        }
    }
}