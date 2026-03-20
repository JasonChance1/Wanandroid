package com.example.app_mvp_java.net;

import com.example.common.net.interceptor.AddCookieInterceptor;
import com.example.common.net.interceptor.SaveCookieInterceptor;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author wandervogel
 * @date 2025-09-22
 * @description RxJava 网络管理类
 */
public class RxNetManager {
    private static final String BASE_URL = "https://www.wanandroid.com/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (RxNetManager.class) {
                if (retrofit == null) {
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS)
                            .addInterceptor(new SaveCookieInterceptor())
                            .addInterceptor(new AddCookieInterceptor())
                            .addInterceptor(new LoggingInterceptor.Builder()
                                    .setLevel(Level.BASIC)
                                    .log(Platform.WARN)
                                    .request("request")
                                    .response("response")
                                    .build())
                            .build();

                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

    public static <T> T create(Class<T> service) {
        return getRetrofit().create(service);
    }
}
