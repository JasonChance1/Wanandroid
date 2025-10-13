package com.example.common.net.interceptor

import com.example.common.util.DataStoreUtil
import okhttp3.Interceptor
import okhttp3.Response

private const val COOKIE_NAME = "Cookie"
class AddCookieInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        val domain = request.url.host
        if (domain.isNotEmpty()) {
            val spDomain: String = DataStoreUtil.getSync(domain, "")
            val cookie: String = spDomain.ifEmpty { "" }
            if (cookie.isNotEmpty()) {
                builder.addHeader(COOKIE_NAME, cookie)
            }
        }
        return chain.proceed(builder.build())
    }
}