package kz.fime.samal.data.repositories

import com.google.gson.Gson
import kz.fime.samal.api.SamalApi
import kz.fime.samal.data.base.State
import kz.fime.samal.data.base.call
import timber.log.Timber

class AuthRepository constructor(private val service: SamalApi, gson: Gson) : BaseRepository(gson) {

    suspend fun checkAccount(phone: String): State<HashMap<String, Any>> {
        val data = HashMap<String, Any>()
        data["phone"] = phone
        return call {
            service.checkAccount(data)
        }
    }

    suspend fun getOTP(phone: String): State<HashMap<String, Any>> {
        val data = HashMap<String, Any>()
        data["phone"] = phone
        return call {
            service.getOTP(data)
        }
    }

    suspend fun checkOTP(phone: String, code: String): State<HashMap<String, Any>> {
        val data = HashMap<String, Any>()
        data["phone"] = phone
        data["code"] = code
        return call {
            service.checkOTP(data)
        }
    }

    suspend fun registerUser(
        phone: String,
        code: String,
        name: String,
        password: String
    ): State<HashMap<String, Any>> {
        val data = HashMap<String, Any>()
        data["phone"] = phone
        data["code"] = code
        data["name"] = name
        data["password"] = password
        return call {
            service.registerUser(data)
        }
    }

    suspend fun loginUser(phone: String, password: String): State<HashMap<String, Any>> {
        val data = HashMap<String, Any>()
        data["phone"] = phone
        data["password"] = password
        return call {
            service.loginUser(data)
        }
    }

    suspend fun resetPassword(
        phone: String,
        code: String,
        password: String
    ): State<HashMap<String, Any>> {
        val data = HashMap<String, Any>()
        data["phone"] = phone
        data["code"] = code
        data["password"] = password
        return call {
            service.resetPassword(data)
        }
    }

//    suspend fun checkAccount(phone: String) : Response<BaseResponse> {
//        val data = HashMap<String, Any>()
//        data["phone"] = phone
//        val call = service.checkAccount(data)
//        val result = try {
//            call.execute()
//        } catch (exception: Exception) {
//            return Response.Failure(exception)
//        }
//        val body = result.body() ?: let {
//            val dataError = RequestResponseError(RequestResponseErrorCode.EmptyBody)
//            return Response.Failure(dataError)
//        }
//        return Response.Success(body)
//    }

}