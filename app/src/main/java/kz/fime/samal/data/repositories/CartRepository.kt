package kz.fime.samal.data.repositories

import kz.fime.samal.api.SamalApi
import kz.fime.samal.data.base.call

class CartRepository (private val service: SamalApi) {

    suspend fun getCartItems() = call { service.getCartItems() }

    suspend fun clearCart() = call { service.clearCart() }

    suspend fun getClientPoints() = call { service.getClientPoints() }

    suspend fun getPaymentCards() = call { service.getPaymentCards() }

    suspend fun getDeliveryTypes() = call { service.getDeliveryTypes() }

    suspend fun getPaymentTypes() = call { service.getPaymentTypes() }

    suspend fun getClientAddresses() = call { service.getClientAddresses() }

    suspend fun getPickUpLocations() = call { service.getPickUpLocations() }

    suspend fun getDeliveryCost(addressName: String) = call { service.getDeliveryCost(hashMapOf(Pair("address_slug", addressName))) }

    suspend fun updateCartItems(baskedId: String, count: Int) = call { service.updateCartItem(baskedId, hashMapOf(Pair("count", count))) }

}