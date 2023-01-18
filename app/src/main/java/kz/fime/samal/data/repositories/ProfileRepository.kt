package kz.fime.samal.data.repositories

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kz.fime.samal.api.ApiResponse
import kz.fime.samal.api.SamalApi
import kz.fime.samal.data.base.call
import kz.fime.samal.data.models.*
import kz.fime.samal.data.models.custom.Resource
import kz.fime.samal.data.models.order_detail.ClientAddress
import kz.fime.samal.utils.extensions.Item
import okhttp3.MultipartBody
import retrofit2.Response

class ProfileRepository constructor(private val service: SamalApi, gson: Gson) :
    BaseRepository(gson) {

    @SuppressLint("CheckResult")
    fun getProfile3(): LiveData<Resource<ProfileResponse>> {
        val tempLiveData: MutableLiveData<Resource<ProfileResponse>> = MutableLiveData()
        service
            .getProfile3()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { tempLiveData.postValue(Resource.loading(null)) }
            .subscribe({
                tempLiveData.postValue(Resource.success(it))
            }, {
                tempLiveData.postValue(getError(it))
            })
        return tempLiveData
    }

    @SuppressLint("CheckResult")
    fun logoutUser() : LiveData<Resource<BaseResponse>> {
        val tempLiveData: MutableLiveData<Resource<BaseResponse>> = MutableLiveData()
        service
            .logoutUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { tempLiveData.postValue(Resource.loading(null)) }
            .subscribe({
                tempLiveData.postValue(Resource.success(it))
            }, {
                tempLiveData.postValue(getError(it))
            })
        return tempLiveData
    }

    @SuppressLint("CheckResult")
    fun changePhoneRequest(phone: String) : LiveData<Resource<BaseResponse>> {
        val data = HashMap<String, Any>()
        data["phone"] = phone
        val tempLiveData: MutableLiveData<Resource<BaseResponse>> = MutableLiveData()
        service
            .changePhoneRequest(data)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { tempLiveData.postValue(Resource.loading(null)) }
            .subscribe({
                tempLiveData.postValue(Resource.success(it))
            }, {
                tempLiveData.postValue(getError(it))
            })
        return tempLiveData
    }

    @SuppressLint("CheckResult")
    fun changePhone(phone: String, otp: String) : LiveData<Resource<BaseResponse>> {
        val data = HashMap<String, Any>()
        data["phone"] = phone
        data["code"] = otp
        val tempLiveData: MutableLiveData<Resource<BaseResponse>> = MutableLiveData()
        service
            .changePhone(data)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { tempLiveData.postValue(Resource.loading(null)) }
            .subscribe({
                tempLiveData.postValue(Resource.success(it))
            }, {
                tempLiveData.postValue(getError(it))
            })
        return tempLiveData
    }

    @SuppressLint("CheckResult")
    fun changeName(name: String) : LiveData<Resource<BaseResponse>> {
        val data = HashMap<String, Any>()
        data["name"] = name
        val tempLiveData: MutableLiveData<Resource<BaseResponse>> = MutableLiveData()
        service
            .changeName(data)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { tempLiveData.postValue(Resource.loading(null)) }
            .subscribe({
                tempLiveData.postValue(Resource.success(it))
            }, {
                tempLiveData.postValue(getError(it))
            })
        return tempLiveData
    }

    @SuppressLint("CheckResult")
    fun changeEmail(email: String) : LiveData<Resource<BaseResponse>> {
        val data = HashMap<String, Any>()
        data["email"] = email
        val tempLiveData: MutableLiveData<Resource<BaseResponse>> = MutableLiveData()
        service
            .changeEmail(data)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { tempLiveData.postValue(Resource.loading(null)) }
            .subscribe({
                tempLiveData.postValue(Resource.success(it))
            }, {
                tempLiveData.postValue(getError(it))
            })
        return tempLiveData
    }

    @SuppressLint("CheckResult")
    fun getCards() : LiveData<Resource<Response<CardsResponse>>> {
        val tempLiveData: MutableLiveData<Resource<Response<CardsResponse>>> = MutableLiveData()
        service
            .getCards()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { tempLiveData.postValue(Resource.loading(null)) }
            .subscribe({
                tempLiveData.postValue(Resource.success(it))
            }, {
                tempLiveData.postValue(getError(it))
            })
        return tempLiveData
    }

    @SuppressLint("CheckResult")
    fun getAddress() : LiveData<Resource<Response<AddressResponse>>> {
        val tempLiveData: MutableLiveData<Resource<Response<AddressResponse>>> = MutableLiveData()
        service
            .getAddress()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { tempLiveData.postValue(Resource.loading(null)) }
            .subscribe({
                tempLiveData.postValue(Resource.success(it))
            }, {
                tempLiveData.postValue(getError(it))
            })
        return tempLiveData
    }

    @SuppressLint("CheckResult")
    fun addCard() : LiveData<Resource<AddCardResponse>> {
        val tempLiveData: MutableLiveData<Resource<AddCardResponse>> = MutableLiveData()
        service
            .addCard()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { tempLiveData.postValue(Resource.loading(null)) }
            .subscribe({
                tempLiveData.postValue(Resource.success(it))
            }, {
                tempLiveData.postValue(getError(it))
            })
        return tempLiveData
    }

    @SuppressLint("CheckResult")
    fun deleteCard(cardId: String) : LiveData<Resource<CardsResponse>>{
        val tempLiveData: MutableLiveData<Resource<CardsResponse>> = MutableLiveData()
        service.deleteCard(cardId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { tempLiveData.postValue(Resource.loading(null)) }
            .subscribe({
                tempLiveData.postValue(Resource.success(it))
            },{
                tempLiveData.postValue(getError(it))
            })

        return tempLiveData
    }

    @SuppressLint("CheckResult")
    fun loadFaq() : LiveData<Resource<FaqResponse>> {
        val tempLiveData: MutableLiveData<Resource<FaqResponse>> = MutableLiveData()
        service
            .getFaq()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { tempLiveData.postValue(Resource.loading(null)) }
            .subscribe({
                tempLiveData.postValue(Resource.success(it))
            }, {
                tempLiveData.postValue(getError(it))
            })
        return tempLiveData
    }

    @SuppressLint("CheckResult")
    fun loadAbout() : LiveData<Resource<ApiResponse<AboutResponse>>> {
        val tempLiveData: MutableLiveData<Resource<ApiResponse<AboutResponse>>> = MutableLiveData()
        service
            .getAbout()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { tempLiveData.postValue(Resource.loading(null)) }
            .subscribe({
                tempLiveData.postValue(Resource.success(it))
            }, {
                tempLiveData.postValue(getError(it))
            })
        return tempLiveData
    }

    @SuppressLint("CheckResult")
    fun changeImage(photo: MultipartBody.Part?) : LiveData<Resource<BaseResponse>> {
        val tempLiveData: MutableLiveData<Resource<BaseResponse>> = MutableLiveData()
        service
            .changePhoto(photo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { tempLiveData.postValue(Resource.loading(null)) }
            .subscribe({
                tempLiveData.postValue(Resource.success(it))
            }, {
                tempLiveData.postValue(getError(it))
            })
        return tempLiveData
    }

    @SuppressLint("CheckResult")
    fun loadCategoryProducts(categorySlug: String, body: Item, page: String?) : LiveData<Resource<ApiResponse<List<Item>>>> {
        val tempLiveData: MutableLiveData<Resource<ApiResponse<List<Item>>>> = MutableLiveData()
        service
            .getCategoryProducts(categorySlug, body, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { tempLiveData.postValue(Resource.loading(null)) }
            .subscribe({
                tempLiveData.postValue(Resource.success(it))
            }, {
                tempLiveData.postValue(getError(it))
            })
        return tempLiveData
    }

    @SuppressLint("CheckResult")
    fun loadShops(page: String?) : LiveData<Resource<ApiResponse<List<Item>>>> {
        val tempLiveData: MutableLiveData<Resource<ApiResponse<List<Item>>>> = MutableLiveData()
        service
            .getShops(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { tempLiveData.postValue(Resource.loading(null)) }
            .subscribe({
                tempLiveData.postValue(Resource.success(it))
            }, {
                tempLiveData.postValue(getError(it))
            })
        return tempLiveData
    }

    @SuppressLint("CheckResult")
    fun loadSubcategory(categorySlug: String) : LiveData<Resource<CategoryModel>> {
        val tempLiveData: MutableLiveData<Resource<CategoryModel>> = MutableLiveData()
        service
            .subCategory(categorySlug)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { tempLiveData.postValue(Resource.loading(null)) }
            .subscribe({
                tempLiveData.postValue(Resource.success(it))
            }, {
                tempLiveData.postValue(getError(it))
            })
        return tempLiveData
    }

    @SuppressLint("CheckResult")
    fun loadSearchProduct(text: String) : LiveData<Resource<ApiResponse<List<Item>>>> {
        val tempLiveData: MutableLiveData<Resource<ApiResponse<List<Item>>>> = MutableLiveData()
        service
            .productSearch(SearchRequest(text))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { tempLiveData.postValue(Resource.loading(null)) }
            .subscribe({
                tempLiveData.postValue(Resource.success(it))
            }, {
                tempLiveData.postValue(getError(it))
            })
        return tempLiveData
    }

    @SuppressLint("CheckResult")
    fun loadShopSearch(text: String) : LiveData<Resource<ApiResponse<List<Item>>>> {
        val tempLiveData: MutableLiveData<Resource<ApiResponse<List<Item>>>> = MutableLiveData()
        service
            .shopsSearch(ShopSearchRequest(text))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { tempLiveData.postValue(Resource.loading(null)) }
            .subscribe({
                tempLiveData.postValue(Resource.success(it))
            }, {
                tempLiveData.postValue(getError(it))
            })
        return tempLiveData
    }

    @SuppressLint("CheckResult")
    fun loadCategoriesSearch(text: String) : LiveData<Resource<ApiResponse<List<Item>>>> {
        val tempLiveData: MutableLiveData<Resource<ApiResponse<List<Item>>>> = MutableLiveData()
        service
            .productSearch(SearchRequest(text))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { tempLiveData.postValue(Resource.loading(null)) }
            .subscribe({
                tempLiveData.postValue(Resource.success(it))
            }, {
                tempLiveData.postValue(getError(it))
            })
        return tempLiveData
    }
}