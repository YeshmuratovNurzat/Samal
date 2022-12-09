package kz.fime.samal.data.base

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.fime.samal.api.ApiQuery
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class PagingDataSource<T> constructor(
        private val loadData: suspend (page: Int) -> Response<ApiQuery<T>>?
) {

    private var items = mutableListOf<T>()
    private val _liveItems = MutableLiveData<List<T>>()
    private val _liveState = MutableLiveData<State<List<T>>>()
    val liveItems = _liveItems as LiveData<List<T>>
    val liveState: LiveData<State<List<T>>> = _liveState

    @Volatile
    private var page = 0
    private var hasNext = true
    private var isLoading = false


    @Synchronized
    fun loadData(isInitial: Boolean){
        if (isLoading) return
        if (isInitial) {
            isLoading = true
            _liveState.postValue(State.Loading)
            hasNext = true
            items = mutableListOf()
            page = 1
            load(page)
        } else if (hasNext) {
            isLoading = true
            _liveState.postValue(State.Loading)
            page++
            load(page)
        }
    }

    private fun load(page: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            Timber.d("Loading: %s", page)
            try {
                postData(loadData.invoke(page))
            } catch (httpException: HttpException) {
                withContext(Dispatchers.Main) {
//                    ToastUtils.showResponseError(httpException.response()?.errorBody())
                }
                _liveState.postValue(State.UnknownError)
            } catch (connectionException: IOException) {
                withContext(Dispatchers.Main) {
//                    ToastUtils.showConnectionError()
                }
                _liveState.postValue(State.NetworkError)
            } catch (unknownException: Exception) {
                unknownException.printStackTrace()
                _liveState.postValue(State.UnknownError)
            }
        }
    }

    private fun postData(response: Response<ApiQuery<T>>?){
        if (response?.body() != null) {
            hasNext = !TextUtils.isEmpty(response.body()?.next)
            insertItems(response.body()?.results)
            _liveState.postValue(State.Success(response.code(), "", response.body()?.results))
        } else {
            _liveState.postValue(State.NetworkError)
        }
        isLoading = false
    }

    fun insertItem(item: T?) {
        item?.let {
            items.add(it)
            _liveItems.postValue(items)
        }
    }

    fun notifyChange(){
        _liveItems.postValue(items)
    }

    fun insertItems(list: List<T>?){
        list?.let {
            items.addAll(it)
            _liveItems.postValue(items)
        }
    }

    fun getItems() = (items as List<T>)

}