package kz.fime.samal.data.repositories

import kz.fime.samal.api.SamalApi
import kz.fime.samal.data.base.call
import kz.fime.samal.data.models.Product
import kz.fime.samal.utils.extensions.Item

class CatalogRepository (private val service: SamalApi) {

//    suspend fun getCategories(): State<List<HashMap<String, Any>>> {
//        return call { service.getCategories() }
//    }

    suspend fun getCategories() = call { service.getCategories() }

//    suspend fun getCategoryProducts(categorySlug: String, body: Item) = call { service.getCategoryProducts(categorySlug, body) }

    suspend fun getWidgetProducts(categorySlug: String) = call { service.getWidgetProducts(categorySlug) }

    suspend fun getShops() = call { service.getShops() }

    suspend fun postSomething() = call { service.postSomething(hashMapOf(
            Pair("key1", "val1"),
            Pair("key2", "val2")
        ))
    }

    suspend fun getBanners() = call { service.getBanners() }

    suspend fun getWidgets() = call { service.getWidgets() }

    suspend fun getProduct(product: Product) = call { service.getProduct(product.shop_slug!!, product.product_slug!!) }

    suspend fun getProductDetails(shopSlug: String, productSlug: String) = call { service.getProduct(shopSlug, productSlug) }

    suspend fun getShop(shopId: String) = call { service.getShop(shopId) }

    suspend fun getShopCategories(shopId: String) = call { service.getShopCategories(shopId) }

    suspend fun addCartItem(shopId: String, productSlug: String, productVariant: Int) = call {
        val body = Item()
        body["shop_uuid"] = shopId
        body["product_slug"] = productSlug
        body["product_variant_id"] = productVariant
        service.addCartItem(body)
    }

    suspend fun getReviews(shopSlug: String, productSlug: String) = call { service.getReviews(shopSlug, productSlug) }

    suspend fun getTypeSort() = call { service.getTypeSort() }
}