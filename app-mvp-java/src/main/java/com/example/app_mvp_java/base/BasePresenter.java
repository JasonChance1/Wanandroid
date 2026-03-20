package com.example.app_mvp_java.base;

import java.lang.ref.WeakReference;

/**
 * @author wandervogel
 * @date 2025-09-22
 * @description Presenter基类
 */
public abstract class BasePresenter<V extends IBaseView> implements IBasePresenter<V> {

    private WeakReference<V> mViewRef;

    @Override
    public void attachView(V view) {
        mViewRef = new WeakReference<>(view);
    }

    @Override
    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    @Override
    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    public V getView() {
        return mViewRef != null ? mViewRef.get() : null;
    }
}
