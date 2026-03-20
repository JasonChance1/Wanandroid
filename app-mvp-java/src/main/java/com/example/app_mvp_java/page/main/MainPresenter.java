package com.example.app_mvp_java.page.main;

import com.example.app_mvp_java.base.BasePresenter;

/**
 * @author wandervogel
 * @date 2025-09-22
 * @description 首页Presenter实现
 */
public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

    @Override
    public void loadData() {
        if (!isViewAttached()) return;
        
        getView().startLoading();
        
        // 模拟网络请求
        new android.os.Handler().postDelayed(() -> {
            if (isViewAttached()) {
                getView().loadingFinished();
                getView().showWelcomeMessage("欢迎来到 Wanandroid MVP Java 版");
            }
        }, 2000);
    }
}
