package com.example.app_mvp_java.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.common.ui.state.DefaultStateImpl;
import com.example.common.ui.state.IState;

/**
 * @author wandervogel
 * @date 2025-09-22
 * @description Fragment基类，集成MVP与状态管理
 */
public abstract class BaseFragment<P extends IBasePresenter> extends Fragment implements IBaseView {

    protected P mPresenter;
    private IState mStateImpl;
    private FrameLayout mRootGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootGroup = new FrameLayout(requireContext());
        View contentView = inflater.inflate(getLayoutId(), container, false);
        mRootGroup.addView(contentView);
        return mRootGroup;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // 初始化状态管理
        mStateImpl = getState();
        if (mStateImpl != null && mStateImpl.getStateView() != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
            );
            mRootGroup.addView(mStateImpl.getStateView(), params);
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

        initView(view);
        initData();
    }

    protected abstract int getLayoutId();

    protected abstract P createPresenter();

    protected void initView(View view) {}

    protected void initData() {}

    protected IState getState() {
        return new DefaultStateImpl(requireContext());
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
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
    }
}
