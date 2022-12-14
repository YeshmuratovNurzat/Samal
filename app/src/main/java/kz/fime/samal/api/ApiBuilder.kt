package kz.fime.samal.api

import android.app.Application
import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.hilt.android.qualifiers.ApplicationContext
import kz.fime.samal.utils.API
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import kotlin.jvm.Throws

class ApiBuilder(@ApplicationContext val context: Context? = null) {

    private val loggingInterceptor: HttpLoggingInterceptor
        get() {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            return httpLoggingInterceptor
        }

    fun <T> buildService(type: Class<T>, withoutAuthInterceptor: Boolean = false): T {
        val authInterceptor = AuthInterceptor()
//        val okHttp = OkHttpClient.Builder()
//            .addInterceptor(authInterceptor)
//            .addInterceptor(loggingInterceptor)
        val okHttp = getUnsafeOkHttpClient()
                .addInterceptor(authInterceptor)
        if (!withoutAuthInterceptor) {
            okHttp.addInterceptor(loggingInterceptor)
        }
        val builder = Retrofit.Builder()
            .baseUrl(API)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttp.build())
        val retrofit = builder.build()
        return retrofit.create(type)
    }

    private fun getUnsafeOkHttpClient(): OkHttpClient.Builder =
            try {
                // Create a trust manager that does not validate certificate chains
                val trustAllCerts: Array<TrustManager> = arrayOf(
                        object : X509TrustManager {
                            @Throws(CertificateException::class)
                            override fun checkClientTrusted(chain: Array<X509Certificate?>?,
                                                            authType: String?) = Unit

                            @Throws(CertificateException::class)
                            override fun checkServerTrusted(chain: Array<X509Certificate?>?,
                                                            authType: String?) = Unit


                            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                        }
                )
                // Install the all-trusting trust manager
                val sslContext: SSLContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, SecureRandom())
                // Create an ssl socket factory with our all-trusting manager
                val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
                val builder = OkHttpClient.Builder()
                context?.let {
                    builder.addInterceptor(ChuckerInterceptor(it))
                }
                builder.sslSocketFactory(sslSocketFactory,
                        trustAllCerts[0] as X509TrustManager)
                builder.hostnameVerifier { _, _ -> true }
                builder
            } catch (e: Exception) {
                throw RuntimeException(e)
            }

}