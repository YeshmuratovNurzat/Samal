package kz.fime.samal.api

import kz.fime.samal.data.SessionManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import kotlin.jvm.Throws

class AuthInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
            .header("Application", "application/json")
            .header("Content-Type", "application/json")
        if (SessionManager.token.isNotEmpty()) requestBuilder.addHeader(
            "Authorization",
            "Bearer ${SessionManager.token}"
        )

        val response = chain.proceed(requestBuilder.build())
        return response
    }
}