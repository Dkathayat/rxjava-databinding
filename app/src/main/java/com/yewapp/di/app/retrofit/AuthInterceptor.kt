package com.yewapp.di.app.retrofit

import com.yewapp.data.local.PreferenceKeys
import com.yewapp.data.local.PreferencesHelper
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthInterceptor(private val preferencesHelper: PreferencesHelper) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = preferencesHelper.getString(PreferenceKeys.API_TOKEN, "")
        val requestBuilder = chain.request().newBuilder()
        requestBuilder.addHeader("Authorization", "Bearer $token")
        requestBuilder.addHeader("Accept", "application/json")
        return chain.proceed(requestBuilder.build())
    }
}