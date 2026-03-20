package com.example.app_mvp_java.base;

import android.view.View;

/**
 * @author wandervogel
 * @date 2025-09-22
 * @description 基础View接口，定义了通用的UI状态切换方法
 */
public interface IBaseView {
    void startLoading();
    
    void startLoading(String msg);

    void loadingFinished();

    void showBadNetwork(View.OnClickListener listener);

    void showError(String tip, View.OnClickListener errorAction);

    void showEmptyView(String tip, View.OnClickListener emptyAction);
}
