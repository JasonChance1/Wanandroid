package com.example.app_mvp_java.base;

/**
 * @author wandervogel
 * @date 2025-09-22
 * @description 基础Presenter接口
 */
public interface IBasePresenter<V extends IBaseView> {
    /**
     * 绑定View
     */
    void attachView(V view);

    /**
     * 解绑View，防止内存泄漏
     */
    void detachView();

    /**
     * 判断View是否已绑定
     */
    boolean isViewAttached();
}
