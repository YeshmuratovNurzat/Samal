package kz.fime.samal.data.repositories

import kz.fime.samal.api.SamalApi
import kz.fime.samal.data.base.call

class FcmRepository (private val service: SamalApi) {

    suspend fun addPushToken(token: String) = call {
        service.addPushToken(
            hashMapOf(
                Pair("device_token", token),
                Pair("device_type", 2)
            )
        )
    }

    suspend fun editPushToken(token: String) = call {
        service.editPushToken(
            hashMapOf(
                Pair("device_token", token),
                Pair("device_type", 2)
            )
        )
    }

}