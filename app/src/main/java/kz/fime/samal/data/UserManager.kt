package kz.fime.samal.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import kz.fime.samal.data.entities.Profile
import kz.fime.samal.utils.SHARED_PREFERENCES_NAME

object UserManager {

    private lateinit var sharedPreferences: SharedPreferences

    fun inject(context: Context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    private val gson = Gson()

    var profile: Profile? = null
        get() = field ?: sharedPreferences.getString("profile", null)?.let {
            gson.fromJson(it, Profile::class.java)
        }
        set(value) {
            field = value
            profile?.let {
                sharedPreferences.edit().putString("profile", gson.toJson(profile)).apply()
            }
        }


    fun logOut(){
        sharedPreferences.edit().putString("profile", null).apply()
    }
}