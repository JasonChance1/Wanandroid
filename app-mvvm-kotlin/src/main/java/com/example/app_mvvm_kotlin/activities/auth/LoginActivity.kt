package com.example.app_mvvm_kotlin.activities.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.app_mvvm_kotlin.activities.home.MainActivity
import com.example.app_mvvm_kotlin.databinding.ActivityLoginBinding
import com.example.app_mvvm_kotlin.viewmodel.LoginViewModel
import com.example.common.constant.DSConstant
import com.example.common.entities.event.LoginEvent
import com.example.common.ui.activity.BaseVbActivity
import com.example.common.util.DataStoreUtil
import kotlinx.coroutines.launch

/**
 * @author wandervogel
 * @date 2025-09-23  星期二
 * @description 登录
 */
class LoginActivity : BaseVbActivity<ActivityLoginBinding>() {
    private val vm: LoginViewModel by viewModels()
    override fun createBinding() = ActivityLoginBinding.inflate(layoutInflater)
    private val registerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val username = result.data?.getStringExtra(RegisterActivity.EXTRA_USERNAME).orEmpty()
            vm.applyRegisteredUsername(username)
        }

    override fun autoLoading() = false

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        with(binding) {
            etUsername.doAfterTextChanged { vm.onUsernameChanged(it?.toString().orEmpty()) }
            etPassword.doAfterTextChanged { vm.onPasswordChanged(it?.toString().orEmpty()) }

            btnLogin.setOnClickListener { vm.login() }
            tvRegister.setOnClickListener { vm.clickRegister() }
        }
        observeState()
        observeEvent()
        loadingFinished()
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.uiState.collect { s ->

                    binding.btnLogin.isEnabled = !s.loading
                    if (s.loading) {
                        showLoading()
                    } else {
                        hideLoading()
                    }

                    if (binding.etUsername.text?.toString() != s.username) {
                        binding.etUsername.setText(s.username)
                        binding.etUsername.setSelection(s.username.length)
                    }
                }
            }
        }
    }

    private fun observeEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.eventFlow.collect { e ->
                    when (e) {
                        is LoginEvent.Toast -> toast(e.msg)
                        is LoginEvent.LoginSuccess -> {
                            e.login
                            DataStoreUtil.putSync(DSConstant.USERNAME, binding.etUsername.text.toString())
                            startActivity(MainActivity::class.java)
                        }

                        LoginEvent.GoRegister -> {
                            registerLauncher.launch(
                                Intent(
                                    this@LoginActivity,
                                    RegisterActivity::class.java
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}