package com.example.common.net

import com.example.common.net.interceptor.AddCookieInterceptor
import com.example.common.net.interceptor.SaveCookieInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import okhttp3.internal.platform.Platform

object CoreRetrofit {
    private const val BASE_URL = "https://www.wanandroid.com/"

    private val okHttpClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        OkHttpClient().newBuilder().apply {
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(10, TimeUnit.SECONDS)
            addInterceptor(SaveCookieInterceptor())
            addInterceptor(AddCookieInterceptor())
            addInterceptor(LoggingInterceptor.Builder().setLevel(Level.BASIC).log(Platform.WARN).request("request").response("response").build())
        }.build()

    }


    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(service: Class<T>): T = retrofit.create(service)
}
