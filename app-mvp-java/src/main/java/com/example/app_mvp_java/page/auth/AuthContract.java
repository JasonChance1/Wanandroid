package com.example.app_mvp_java.page.auth;

import com.example.app_mvp_java.base.IBasePresenter;
import com.example.app_mvp_java.base.IBaseView;
import com.example.model.Login;

/**
 * @author wandervogel
 * @date 2025-09-22
 * @description 注册登录契约类
 */
public interface AuthContract {
    interface View extends IBaseView {
        void onAuthSuccess(Login loginData);
        void onAuthFailed(String errorMsg);
    }

    interface Presenter extends IBasePresenter<View> {
        void login(String username, String password);
        void register(String username, String password, String repassword);
    }
}
