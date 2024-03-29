package kz.fime.samal.data.repositories

import kz.fime.samal.api.SamalApi
import kz.fime.samal.data.UserManager
import kz.fime.samal.data.base.State
import kz.fime.samal.data.base.call
import kz.fime.samal.data.entities.Profile
import kz.fime.samal.utils.extensions.Item

class MainRepository (private val service: SamalApi) {

    suspend fun getProfile(fromServer: Boolean): State<Profile> {
        return if (fromServer || UserManager.profile ==null) {
             call(doOnSuccess = {
                UserManager.profile = it
            }) { service.getProfile() }
        } else {
            State.Success(result = UserManager.profile)
        }
    }

    suspend fun getFavorites() = call { service.getFavorites() }

    suspend fun toggleFavorites(shopId: String, productSlug: String) = call { service.toggleFavorites(
        hashMapOf(Pair("shop_uuid", shopId), Pair("product_slug", productSlug))
    ) }

    suspend fun clearFavorites() = call { service.clearFavorites() }

    suspend fun getOrders() = call { service.getOrders() }

    suspend fun placeOrder(baskets: List<String>, deliverySlug: String, pick_up_point_slug: String, paymentSlug: String, installment_uuid: String, addressSlug: String, priceProduct: Int, quota: Int) = call {
        val body = hashMapOf(
            "baskets" to baskets,
            "price_product" to priceProduct,
            "payment_slug" to paymentSlug,
            "installment_uuid" to installment_uuid,
//            "cart" to null,
            "delivery_slug" to deliverySlug,
            "address_slug" to addressSlug,
            "pick_up_point_slug" to pick_up_point_slug,
            "quota" to quota,
            "cost_total" to (quota + priceProduct)
        )
        service.placeOrder(body)
    }

    suspend fun getInstallment() = call {service.getInstallment()}

    suspend fun getOrderDetails(orderId: String) = call { service.getOrderDetails(orderId) }

    suspend fun cancelOrder(orderId: String) = call { service.cancelOrder(orderId) }

    suspend fun getClientAddresses() = call { service.getClientAddresses() }

    suspend fun getClientCities() = call { service.getClientCities() }

    suspend fun getClientAddressDetails(addressId: String) = call { service.getClientAddressDetails(addressId) }

    suspend fun addClientAddress(cityId: Int, name: String, street: String, houseNumber: String, apartment: String, isDefault: Boolean, latitude: String, longitude: String) = call {
        val body = Item()
        body["city_id"] = cityId
        body["name"] = name
        body["street"] = street
        body["house_number"] = houseNumber
        body["apartment"] = apartment
        body["default"] = isDefault
        body["latitude"] = latitude
        body["longitude"] = longitude
        service.addClientAddress(body)
    }

    suspend fun updateClientAddress(addressId: String, cityId: Int, name: String, street: String, houseNumber: String, apartment: String, isDefault: Boolean, latitude: String, longitude: String ) = call {
        val body = Item()
        body["city_id"] = cityId
        body["name"] = name
        body["street"] = street
        body["house_number"] = houseNumber
        body["apartment"] = apartment
        body["default"] = isDefault
        body["latitude"] = latitude
        body["longitude"] = longitude
        service.updateClientAddress(addressId, body)
    }

    suspend fun deleteClientAddress(addressId: String) = call { service.deleteClientAddress(addressId) }

//    suspend fun notifications() = call {service.notifications()}

    suspend fun notificationsReadAll() = call {service.notificationsReadAll()}

    suspend fun notificationRead(notificationId: String) = call {service.notificationRead(notificationId)}

}