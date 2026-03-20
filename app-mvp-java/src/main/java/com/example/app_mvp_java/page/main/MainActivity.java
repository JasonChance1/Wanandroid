package com.example.app_mvp_java.page.main;

import android.widget.TextView;
import android.widget.Toast;

import com.example.app_mvp_java.R;
import com.example.app_mvp_java.base.BaseActivity;

/**
 * @author wandervogel
 * @date 2025-09-22
 * @description 首页实现
 */
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    private TextView tvWelcome;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    protected void initView() {
        tvWelcome = findViewById(R.id.tv_welcome);
    }

    @Override
    protected void initData() {
        mPresenter.loadData();
    }

    @Override
    public void showWelcomeMessage(String message) {
        if (tvWelcome != null) {
            tvWelcome.setText(message);
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
