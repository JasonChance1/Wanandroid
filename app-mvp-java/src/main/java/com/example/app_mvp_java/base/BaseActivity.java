package com.example.app_mvp_java.base;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.common.extensions.ExtensionsKt;
import com.example.common.ui.state.DefaultStateImpl;
import com.example.common.ui.state.IState;

/**
 * @author wandervogel
 * @date 2025-09-22
 * @description Activity基类，集成MVP与状态管理
 */
public abstract class BaseActivity<P extends IBasePresenter> extends AppCompatActivity implements IBaseView {

    protected P mPresenter;
    private IState mStateImpl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 沉浸式状态栏处理 (参考mvvm-kotlin)
        if (Build.VERSION.SDK_INT < 35) {
            ExtensionsKt.transparentStatusBar(this);
        } else {
            // EdgeToEdge in Java
            // EdgeToEdge.enable(this); // 需要依赖 androidx.activity:activity
        }

        setContentView(getLayoutId());

        // 初始化状态管理
        mStateImpl = getState();
        if (mStateImpl != null && mStateImpl.getStateView() != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
            );
            addContentView(mStateImpl.getStateView(), params);
        }

        // 创建Presenter
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }

        if (autoLoading()) {
            startLoading();
        } else {
            loadingFinished();
        }

        initView();
        initData();
    }

    protected abstract int getLayoutId();

    protected abstract P createPresenter();

    protected void initView() {}

    protected void initData() {}

    protected IState getState() {
        return new DefaultStateImpl(this);
    }

    protected boolean autoLoading() {
        return false;
    }

    @Override
    public void startLoading() {
        startLoading("加载中...");
    }

    @Override
    public void startLoading(String msg) {
        if (mStateImpl != null) {
            mStateImpl.startLoading(msg);
        }
    }

    @Override
    public void loadingFinished() {
        if (mStateImpl != null) {
            mStateImpl.loadingFinished();
        }
    }

    @Override
    public void showBadNetwork(View.OnClickListener listener) {
        if (mStateImpl != null) {
            mStateImpl.showBadNetwork(listener);
        }
    }

    @Override
    public void showError(String tip, View.OnClickListener errorAction) {
        if (mStateImpl != null) {
            mStateImpl.showError(tip, errorAction);
        }
    }

    @Override
    public void showEmptyView(String tip, View.OnClickListener emptyAction) {
        if (mStateImpl != null) {
            mStateImpl.showEmptyView(tip, emptyAction);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
    }
}
