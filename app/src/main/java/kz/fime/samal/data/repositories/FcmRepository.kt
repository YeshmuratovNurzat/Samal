package kz.fime.samal.data.repositories

import android.os.Build
import kz.fime.samal.BuildConfig
import kz.fime.samal.api.SamalApi
import kz.fime.samal.data.base.call

class FcmRepository (private val service: SamalApi) {

    suspend fun addPushToken(token: String) = call {
        service.addPushToken(
            hashMapOf(
                Pair("device_token", token),
                Pair("device_type", 3),
                Pair("user_agent", Build.DEVICE + ", " + Build.BOARD + ", "+ Build.MANUFACTURER)
            )
        )
    }

    suspend fun editPushToken(token: String) = call {
        service.editPushToken(
            hashMapOf(
                Pair("device_token", token),
                Pair("device_type", 3),
                Pair("user_agent", Build.DEVICE + ", " + Build.BOARD + ", "+ Build.MANUFACTURER)
            )
        )
    }

}