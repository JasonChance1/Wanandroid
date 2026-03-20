package com.example.app_mvp_java.page.auth;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_mvp_java.R;
import com.example.app_mvp_java.base.BaseActivity;
import com.example.app_mvp_java.page.main.MainActivity;
import com.example.model.Login;

/**
 * @author wandervogel
 * @date 2025-09-22
 * @description 登录注册页面实现
 */
public class AuthActivity extends BaseActivity<AuthPresenter> implements AuthContract.View {

    private TextView tvTitle;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etRepassword;
    private Button btnSubmit;
    private TextView tvSwitch;

    private boolean isLoginMode = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_auth;
    }

    @Override
    protected AuthPresenter createPresenter() {
        return new AuthPresenter();
    }

    @Override
    protected void initView() {
        tvTitle = findViewById(R.id.tv_title);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etRepassword = findViewById(R.id.et_repassword);
        btnSubmit = findViewById(R.id.btn_submit);
        tvSwitch = findViewById(R.id.tv_switch);

        btnSubmit.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isLoginMode) {
                mPresenter.login(username, password);
            } else {
                String repassword = etRepassword.getText().toString().trim();
                if (TextUtils.isEmpty(repassword)) {
                    Toast.makeText(this, "请再次输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(repassword)) {
                    Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                mPresenter.register(username, password, repassword);
            }
        });

        tvSwitch.setOnClickListener(v -> {
            isLoginMode = !isLoginMode;
            switchMode();
        });
    }

    private void switchMode() {
        if (isLoginMode) {
            tvTitle.setText("登录");
            btnSubmit.setText("登录");
            tvSwitch.setText("还没账号？立即注册");
            etRepassword.setVisibility(View.GONE);
        } else {
            tvTitle.setText("注册");
            btnSubmit.setText("注册");
            tvSwitch.setText("已有账号？立即登录");
            etRepassword.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAuthSuccess(Login loginData) {
        Toast.makeText(this, (isLoginMode ? "登录" : "注册") + "成功: " + loginData.getNickname(), Toast.LENGTH_SHORT).show();
        // 跳转到首页
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onAuthFailed(String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
    }
}
