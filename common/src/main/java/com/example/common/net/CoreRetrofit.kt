package com.example.common.net

import com.example.common.net.interceptor.AddCookieInterceptor
import com.example.common.net.interceptor.SaveCookieInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit


object CoreRetrofit {
    private const val BASE_URL = "https://www.wanandroid.com/"

    private val okHttpClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        OkHttpClient().newBuilder().apply {
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(10, TimeUnit.SECONDS)
            addInterceptor(SaveCookieInterceptor())
            addInterceptor(AddCookieInterceptor())
        }.build()

    }


    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(service: Class<T>): T = retrofit.create(service)
}
