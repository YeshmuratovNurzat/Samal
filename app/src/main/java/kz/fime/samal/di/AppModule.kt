package kz.fime.samal.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kz.fime.samal.api.ApiBuilder
import kz.fime.samal.api.SamalApi
import kz.fime.samal.data.repositories.*
import kz.fime.samal.utils.SHARED_PREFERENCES_NAME
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideServiceApi(): SamalApi {
        return ApiBuilder().buildService(SamalApi::class.java)
    }

    @Singleton
    @Provides
    fun provideAuthRepository(service: SamalApi): AuthRepository {
        return AuthRepository(service, GsonBuilder().create())
    }

    @Singleton
    @Provides
    fun provideMainRepository(service: SamalApi): MainRepository {
        return MainRepository(service)
    }

    @Singleton
    @Provides
    fun provideCartRepository(service: SamalApi): CartRepository {
        return CartRepository(service)
    }

    @Singleton
    @Provides
    fun provideProfileRepository(service: SamalApi) : ProfileRepository {
        return ProfileRepository(service, GsonBuilder().create())
    }

    @Singleton
    @Provides
    fun provideCatalogRepository(service: SamalApi): CatalogRepository {
        return CatalogRepository(service)
    }

    @Singleton
    @Provides
    fun provideFcmRepository(service: SamalApi) : FcmRepository {
        return FcmRepository(service)
    }
}