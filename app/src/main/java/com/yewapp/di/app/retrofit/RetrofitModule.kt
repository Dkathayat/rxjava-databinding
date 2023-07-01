package com.yewapp.di.app.retrofit

import com.google.gson.Gson
import com.yewapp.BuildConfig
import com.yewapp.data.local.PreferencesHelper
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
abstract class RetrofitModule {

    companion object {

        @JvmStatic
        @Provides
        fun provideAuthInterceptor(preferencesHelper: PreferencesHelper): AuthInterceptor {
            return AuthInterceptor(preferencesHelper)
        }

        @JvmStatic
        @Provides
        fun loggingInterceptor(): HttpLoggingInterceptor {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            return interceptor
        }

//        @JvmStatic
//        @Provides
//        fun provideOkHttpClient(
//            httpLoggingInterceptor: HttpLoggingInterceptor, authInterceptor: AuthInterceptor
//        ): OkHttpClient {
//            return OkHttpClient.Builder()
//                .addInterceptor(authInterceptor)
//                .addInterceptor(httpLoggingInterceptor)
//                .build()
//        }

        @JvmStatic
        @Provides
        fun provideOkHttpClient(
            httpLoggingInterceptor: HttpLoggingInterceptor,
            authInterceptor: AuthInterceptor
        ): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .build()
        }


        @JvmStatic
        @Provides
        fun provideRetrofit(
            okHttpClient: OkHttpClient, moshi: Gson
        ): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(moshi))
                .build()
        }
    }
}