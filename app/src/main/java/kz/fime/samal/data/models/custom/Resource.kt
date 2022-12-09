package kz.fime.samal.data.models.custom

data class Resource<out T>(val status: Status, val data: T?, val message: String?, val exception: Exception? = null) {

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String?, data: T?, exception: Exception?): Resource<T> {
            return Resource(Status.ERROR, data, msg, exception)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }

}
