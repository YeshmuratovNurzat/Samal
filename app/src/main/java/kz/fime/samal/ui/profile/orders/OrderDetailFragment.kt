package kz.fime.samal.ui.profile.orders

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.navigateUp
import dagger.hilt.android.AndroidEntryPoint
import kz.fime.samal.R
import kz.fime.samal.data.base.State
import kz.fime.samal.data.models.order_detail.OrderDetailResponse
import kz.fime.samal.data.models.order_detail.Status
import kz.fime.samal.databinding.FragmentOrderDetailBinding
import kz.fime.samal.ui.base.observeEvent
import kz.fime.samal.ui.base.observeState
import kz.fime.samal.utils.binding.BindingFragment

@AndroidEntryPoint
class OrderDetailFragment: BindingFragment<FragmentOrderDetailBinding>(FragmentOrderDetailBinding::inflate) {

    private val viewModel: OrdersViewModel by viewModels()

    private val args: OrderDetailFragmentArgs by navArgs()

    private var isHide: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            //ibClose.setOnClickListener { findNavController().navigateUp() }
        }
        loadOrderDetails()
        observeViewModel()
        initView()
    }

    private fun loadOrderDetails() {
        val uuid = args.id
        viewModel.loadDetails(uuid)
    }

    private fun observeViewModel() {
        viewModel.orderDetailed.observeState(viewLifecycleOwner, {
            it.result?.let {
                drawOrderDetail(it)
            }
        }, {

        }, {

        })
    }

    private fun drawOrderDetail(orderDetail: OrderDetailResponse) {
        binding.run {
            statusTv.text = orderDetail.order.status.name

            if(orderDetail.order.status.name == "Отменен"){
                btnBack.visibility = View.VISIBLE
                btnClose.visibility = View.GONE

                btnBack.setOnClickListener {
                    findNavController().navigateUp()
                }
            }else{
                btnBack.visibility = View.GONE
                btnClose.visibility = View.VISIBLE
            }

            paymentTypeInfo.text = orderDetail.order.payment.name
            paymentAmount.text = orderDetail.order.total_cost + "₸"
            deliveryTypeTv.text = orderDetail.order.delivery.name
            productsCount.text = "Товаров " + orderDetail.order_products.size

            if (orderDetail.order.delivery.delivery_id == 2) {
                deliverAddress.text = orderDetail.client_address.street + ", дом" + orderDetail.client_address.house_number + ", кв " + orderDetail.client_address.apartment
            }else{
                deliverAddress.text = orderDetail.pick_up_point.address
            }

            val orderDetailAdapter = OrderDetailAdapter()
            productRv.adapter = orderDetailAdapter
            val productList = mutableListOf<ProductItem>()
            if (orderDetail.order.delivery.delivery_id == 2) {
                productList.add(ProductItem("delivery", orderDetail.order.delivery_sum.toInt(), 1))
            }
            for (product in orderDetail.order_products) {
                productList.add(ProductItem(product.name, product.cost.toInt(), product.count.toInt()))
            }
            orderDetailAdapter.submitList(productList)

            showHideProducts.setOnClickListener {
                if (isHide) {
                    productRv.visibility = View.GONE
                    showHideProducts.text = "Показать"
                    showHideProducts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down_24, 0)
                    isHide = false
                } else {
                    productRv.visibility = View.VISIBLE
                    showHideProducts.text = "Скрыть"
                    showHideProducts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up_24, 0)
                    isHide = true
                }
            }

            btnClose.setOnClickListener {
                findNavController().navigate(R.id.action_global_deleteOrderDialog, bundleOf(Pair("id", orderDetail.order.order_uuid)))
            }
        }
    }

    private fun initView() {
        val orderNumber = args.orderNumber
        binding.run {
//            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
//            toolbar.title = "Заказ №$orderNumber"
            orderTitle.text = "Заказ №$orderNumber"
        }
    }
}