package kz.fime.samal.data

import android.content.Context
import android.content.SharedPreferences
import kz.fime.samal.utils.SHARED_PREFERENCES_NAME

object SessionManager {

    private lateinit var sharedPreferences: SharedPreferences

    fun inject(context: Context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        UserManager.inject(context)
    }

    var token: String = ""
        get() {
            return if (field.isEmpty())
                sharedPreferences.getString("token", "") ?: ""
            else field
        }
        set(value) {
            field = value
            sharedPreferences.edit().putString("token", value).apply()
        }

    var isFirstLaunch: Boolean
        get() = sharedPreferences.getBoolean("is_first_launch", true)
        set(value) = sharedPreferences.edit().putBoolean("is_first_launch", value).apply()

    fun logOut(){
        token = ""
        sharedPreferences.edit().putString("token", "").apply()
        UserManager.logOut()
        UserManager.profile = null
    }

    fun getPhone() = sharedPreferences.getString("phone", "")

    fun setPhone() = sharedPreferences.edit().putString("phone", "").apply()

    fun getPassword() = sharedPreferences.getString("password", "")

    fun setPassword() = sharedPreferences.edit().putString("password", "").apply()

    fun setPushToken() = sharedPreferences.edit().putString("pushtoken", "").apply()

    fun getPushToken() = sharedPreferences.getString("pushtoken", "")!!

    fun getUrlAddCard() = sharedPreferences.getString("urlAddCard", "")

    fun setUrlAddCard(url: String) = sharedPreferences.edit().putString("urlAddCard", url).apply()

}