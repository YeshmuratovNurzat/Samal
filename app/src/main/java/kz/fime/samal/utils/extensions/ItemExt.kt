package kz.fime.samal.utils.extensions

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap


const val ITEM_ARG = "item"
typealias Item = HashMap<String, Any>

typealias InnerItem = LinkedTreeMap<String, Any>

inline fun<reified T> HashMap<String, Any>?.getOrNull(key: String, default: T? = null, safe: Boolean = false): T? {
    if (this == null) return default
    if (contains(key)) {
        return if (get(key) is T && !safe) {
            get(key) as T
        } else {
            val gson = Gson()
            gson.fromJson(gson.toJsonTree(get(key)), T::class.java)
        }
    }
    return default
}

inline fun<reified T> LinkedTreeMap<String, Any>?.getOrNull(key: String, default: T? = null, safe: Boolean = false): T? {
    if (this == null) return default
    if (contains(key)) {
        return if (get(key) is T && !safe) {
            get(key) as T
        } else {
            val gson = Gson()
            gson.fromJson(gson.toJsonTree(get(key)), T::class.java)
        }
    }
    return default
}

fun<K, V> HashMap<K, V>.getOrOther(key: K, defValue: V) = get(key) ?: defValue