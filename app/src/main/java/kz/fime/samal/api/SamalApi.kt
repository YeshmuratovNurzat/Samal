package kz.fime.samal.api

import retrofit2.Response
import io.reactivex.Observable
import kz.fime.samal.data.entities.Profile
import kz.fime.samal.data.models.*
import kz.fime.samal.data.models.order_detail.ClientAddress
import kz.fime.samal.data.models.order_detail.OrderDetailResponse
import kz.fime.samal.utils.extensions.Item
import okhttp3.MultipartBody
import retrofit2.http.*

interface SamalApi {

    // Auth
    @POST("auth/client/check_account")
    suspend fun checkAccount(@Body data: HashMap<String, Any>): Response<ApiResponse<HashMap<String, Any>>>

    @POST("auth/client/get_code")
    suspend fun getOTP(@Body data: HashMap<String, Any>): Response<ApiResponse<HashMap<String, Any>>>

    @POST("auth/client/check_code")
    suspend fun checkOTP(@Body data: HashMap<String, Any>): Response<ApiResponse<HashMap<String, Any>>>

    @POST("auth/client/register")
    suspend fun registerUser(@Body data: HashMap<String, Any>): Response<ApiResponse<HashMap<String, Any>>>

    @POST("auth/client/login")
    suspend fun loginUser(@Body data: HashMap<String, Any>): Response<ApiResponse<HashMap<String, Any>>>

    @POST("auth/client/reset_password")
    suspend fun resetPassword(@Body data: HashMap<String, Any>): Response<ApiResponse<HashMap<String, Any>>>

    @POST("auth/client/refresh")
    suspend fun refreshToken(): Response<ApiResponse<HashMap<String, Any>>>

    @POST("auth/client/login")
    suspend fun signIn(@Body data: HashMap<String, Any>): Response<ApiResponse<HashMap<String, Any>>>

    @POST("auth/client/logout")
    suspend fun signOut(@Body data: HashMap<String, Any>): Response<ApiResponse<HashMap<String, Any>>>


    // Home

    @GET("client/category/all")
    suspend fun getCategories(): Response<ApiResponse<List<HashMap<String, Any>>>>

    @GET("client/favourite")
    suspend fun getFavorites(): Response<ApiResponse<List<Item>>>

    @PUT("client/favourite")
    suspend fun toggleFavorites(@Body body: Item): Response<ApiResponse<Nothing>>

    @DELETE("client/favourite")
    suspend fun clearFavorites(): Response<ApiResponse<Nothing>>

    @POST("client/category/{id}/products")
    fun getCategoryProducts(
        @Path("id") categorySlug: String,
        @Body body: Item,
        @Query("page") page: String?
    ): Observable<ApiResponse<List<Item>>>

    @POST("client/shops")
    fun getShops(
        @Query("page") page: String?
    ): Observable<ApiResponse<List<Item>>>

    @GET("client/shop/{id}")
    suspend fun getShop(@Path("id") shopId: String): Response<ApiResponse<Item>>

    @GET("client/shop/{id}/categories/all")
    suspend fun getShopCategories(@Path("id") shopId: String): Response<ApiResponse<List<Item>>>

    @GET("client/banner")
    suspend fun getBanners(): Response<ApiResponse<BannerData>>

    @GET("client/widget")
    suspend fun getWidgets(): Response<ApiResponse<List<Widget>>>

    @GET("client/widget/{id}")
    suspend fun getWidgetProducts(@Path("id") categorySlug: String): Response<ApiResponse<List<Product>>>

    @GET("client/shop/{shop_id}/product/{product_id}")
    suspend fun getProduct(
        @Path("shop_id") shopId: String,
        @Path("product_id") productId: String
    ): Response<ApiResponse<ProductDetailed>>

    @GET("client/shop/{shop_id}/product/{product_id}/comments")
    suspend fun getReviews(@Path("shop_id") shopId: String, @Path("product_id") productId: String): Response<ApiResponse<List<Review>>>


    // Catalog

    @GET("client/type/sort")
    suspend fun getTypeSort() : Response<ApiResponse<List<Item>>>

    // Cart

    @GET("client/basket")
    suspend fun getCartItems(): Response<ApiResponse<List<Item>>>

    @POST("client/basket")
    suspend fun addCartItem(@Body body: Item): Response<ApiResponse<Nothing>>

    @PUT("client/basket/{id}")
    suspend fun updateCartItem(
        @Path("id") basketId: String,
        @Body body: Item
    ): Response<ApiResponse<Item>>

    @POST("client/delivery/quota")
    suspend fun getDeliveryCost(@Body body: Item): Response<ApiResponse<Item>>

    @DELETE("client/basket")
    suspend fun clearCart(): Response<ApiResponse<Nothing>>

    @GET("client/profile/points")
    suspend fun getClientPoints(): Response<ApiResponse<Item>>

    @GET("client/card")
    suspend fun getPaymentCards(): Response<ApiResponse<List<Item>>>

    @GET("client/delivery/type")
    suspend fun getDeliveryTypes(): Response<ApiResponse<List<Item>>>

    @GET("client/payment/type")
    suspend fun getPaymentTypes(): Response<ApiResponse<List<Item>>>

    @GET("client/address")
    suspend fun getClientAddresses(): Response<ApiResponse<List<Item>>>

    @GET("client/city")
    suspend fun getClientCities(): Response<ApiResponse<List<Item>>>

    @GET("client/address/{id}")
    suspend fun getClientAddressDetails(@Path("id") addressId: String): Response<ApiResponse<Item>>

    @POST("client/address")
    suspend fun addClientAddress(@Body body: Item): Response<ApiResponse<Nothing>>

    @PUT("client/address/{id}")
    suspend fun updateClientAddress(
        @Path("id") addressId: String,
        @Body body: Item
    ): Response<ApiResponse<Nothing>>

    @DELETE("client/address/{id}")
    suspend fun deleteClientAddress(@Path("id") addressId: String): Response<ApiResponse<Nothing>>

    @GET("client/delivery/pick_up_point")
    suspend fun getPickUpLocations(): Response<ApiResponse<List<Item>>>


    @POST("client/order")
    suspend fun placeOrder(@Body body: Item): Response<ApiResponse<Item>>

    @GET("client/installment")
    suspend fun getInstallment(): Response<ApiResponse<List<Item>>>

    @GET("client/order")
    suspend fun getOrders(): Response<ApiResponse<List<Item>>>

    @GET("client/order/{id}")
    suspend fun getOrderDetails(@Path("id") orderId: String): Response<ApiResponse<OrderDetailResponse>>

    @GET("client/order/{id}")
    suspend fun cancelOrder(@Path("id") orderId: String): Response<ApiResponse<Nothing>>

    //Profile
    @GET("client/profile")
    fun getProfile3(): Observable<ProfileResponse>

    @POST("auth/client/logout")
    fun logoutUser(): Observable<BaseResponse>

    @PUT("client/profile/check_phone")
    fun changePhoneRequest(@Body data: HashMap<String, Any>): Observable<BaseResponse>

    @PUT("client/profile/phone")
    fun changePhone(@Body data: HashMap<String, Any>): Observable<BaseResponse>

    @PUT("client/profile/name")
    fun changeName(@Body data: HashMap<String, Any>): Observable<BaseResponse>

    @PUT("client/profile/email")
    fun changeEmail(@Body data: HashMap<String, Any>): Observable<BaseResponse>

    @GET("client/card")
    fun getCards(): Observable<Response<CardsResponse>>

    @GET("client/address")
    fun getAddress(): Observable<Response<AddressResponse>>

    @POST("client/card")
    fun addCard(): Observable<AddCardResponse>

    @DELETE("client/card/{{card_id}}")
    fun deleteCard(@Path("card_id") cardId : String) : Observable<CardsResponse>

    @GET("client/profile")
    suspend fun getProfile(): Response<ApiResponse<Profile>>

    @POST("client/push/token")
    suspend fun addPushToken(@Body data: HashMap<String, Any>) : Response<ApiResponse<BaseResponse>>

    @PUT("client/push/token")
    suspend fun editPushToken(@Body data: HashMap<String, Any>) : Response<ApiResponse<BaseResponse>>

    @GET("client/faq")
    fun getFaq() : Observable<FaqResponse>

    @GET("client/about")
    fun getAbout() : Observable<ApiResponse<AboutResponse>>

    @Multipart
    @POST("client/profile/photo")
    fun changePhoto(@Part photo: MultipartBody.Part?) : Observable<BaseResponse>

    @GET("client/category/{slug}")
    fun subCategory(@Path("slug") slug: String) : Observable<CategoryModel>

    @POST("client/search")
    fun productSearch(@Body searchRequest: SearchRequest): Observable<ApiResponse<List<Item>>>

    @POST("client/shops")
    fun shopsSearch(@Body searchRequest: ShopSearchRequest) : Observable<ApiResponse<List<Item>>>

    // Text Examples
    @GET("client/category/all")
    suspend fun postSomething(@Body data: HashMap<String, Any>): Response<ApiResponse<List<HashMap<String, Any>>>>
}