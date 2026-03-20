package com.example.app_mvp_java.net;

import com.example.model.BaseResponse;
import com.example.model.Login;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author wandervogel
 * @date 2025-09-22
 * @description RxJava 版 ApiService
 */
public interface ApiService {
    @POST("user/login")
    Observable<BaseResponse<Login>> login(
            @Query("username") String username,
            @Query("password") String password
    );

    @POST("user/register")
    Observable<BaseResponse<Login>> register(
            @Query("username") String username,
            @Query("password") String password,
            @Query("repassword") String repassword
    );
}
