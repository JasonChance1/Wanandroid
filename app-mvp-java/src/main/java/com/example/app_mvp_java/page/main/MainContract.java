package com.example.app_mvp_java.page.main;

import com.example.app_mvp_java.base.IBasePresenter;
import com.example.app_mvp_java.base.IBaseView;

/**
 * @author wandervogel
 * @date 2025-09-22
 * @description 首页契约类
 */
public interface MainContract {
    interface View extends IBaseView {
        void showWelcomeMessage(String message);
    }

    interface Presenter extends IBasePresenter<View> {
        void loadData();
    }
}
