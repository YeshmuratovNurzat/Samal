package kz.fime.samal.ui.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import kz.fime.samal.data.base.State
import kz.fime.samal.utils.Event
import kz.fime.samal.utils.MessageUtils

@JvmName("observeState")
fun <T> LiveData<State<T>>.observeState(
    lifecycleOwner: LifecycleOwner,
    onSuccess: (State.Success<T>) -> Unit = {},
    onError: (State.ServerError) -> Unit = {},
    onLoading: (isLoading: Boolean) -> Unit = {}){
    observe(lifecycleOwner) {
        when (it) {
            is State.Success -> {
                onLoading.invoke(false)
                onSuccess.invoke(it)
            }
            is State.ServerError -> {
                onLoading.invoke(false)
                onError.invoke(it)
                showServerErrorMessage(it.message)
            }
            State.NetworkError -> {
                onLoading.invoke(false)
                showNetworkErrorMessage()
            }
            State.UnknownError -> {
                onLoading.invoke(false)
                showUnknownErrorMessage()
            }
            State.Loading ->  onLoading.invoke(true)
        }
    }
}

@JvmName("observeEvent")
fun<T> LiveData<Event<State<T>>>.observeEvent(
    lifecycleOwner: LifecycleOwner,
    onSuccess: (State.Success<T>) -> Unit = {},
    onError: (State.ServerError) -> Unit = {},
    onLoading: (isLoading: Boolean) -> Unit = {}
) {
    observe(lifecycleOwner) {
        if (it.peekContent() == State.Loading) {
            onLoading.invoke(true)
        } else {
            when (val res = it.getContentIfNotHandled()) {
                is State.Success -> {
                    onLoading.invoke(false)
                    onSuccess.invoke(res)
                }
                is State.ServerError -> {
                    onLoading.invoke(false)
                    onError.invoke(res)
                    showServerErrorMessage(res.message)
                }
                State.NetworkError -> {
                    onLoading.invoke(false)
                    showNetworkErrorMessage()
                }
                State.UnknownError -> {
                    onLoading.invoke(false)
                    showUnknownErrorMessage()
                }
            }
        }
    }
}

fun<T> Fragment.observe(
        liveData: LiveData<State<T>>,
        onSuccess: (State.Success<T>) -> Unit = {},
        onError: (State.ServerError) -> Unit = {},
        onLoading: (isLoading: Boolean) -> Unit = {}
) {
    liveData.observe(viewLifecycleOwner) {
        when (it) {
            is State.Success -> {
                onLoading.invoke(false)
                onSuccess.invoke(it)
            }
            is State.ServerError -> {
                onLoading.invoke(false)
                onError.invoke(it)
                showServerErrorMessage(it.message)
            }
            State.NetworkError -> {
                onLoading.invoke(false)
                showNetworkErrorMessage()
            }
            State.UnknownError -> {
                onLoading.invoke(false)
                showUnknownErrorMessage()
            }
            State.Loading ->  onLoading.invoke(true)
        }
    }
}

fun<T> Fragment.event(
        liveData: LiveData<Event<State<T>>>,
        onSuccess: (State.Success<T>) -> Unit = {},
        onError: (State.ServerError) -> Unit = {},
        onLoading: (isLoading: Boolean) -> Unit = {}
) {
    liveData.observe(viewLifecycleOwner) {
        if (it.peekContent() == State.Loading) {
            onLoading.invoke(true)
        } else {
            when (val res = it.getContentIfNotHandled()) {
                is State.Success -> {
                    onLoading.invoke(false)
                    onSuccess.invoke(res)
                }
                is State.ServerError -> {
                    onLoading.invoke(false)
                    onError.invoke(res)
                    showServerErrorMessage(res.message)
                }
                State.NetworkError -> {
                    onLoading.invoke(false)
                    showNetworkErrorMessage()
                }
                State.UnknownError -> {
                    onLoading.invoke(false)
                    showUnknownErrorMessage()
                }
            }
        }
    }
}

fun showServerErrorMessage(message: String?){
    MessageUtils.postMessage(message)
}

fun showNetworkErrorMessage() {
    MessageUtils.postMessage("Не удалось подключится к сети!")
}

fun showUnknownErrorMessage() {
    MessageUtils.postMessage("Неизвестная ошибка!")
}