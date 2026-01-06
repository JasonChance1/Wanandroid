package com.example.app_mvvm_kotlin.activities.auth

import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.app_mvvm_kotlin.MainActivity
import com.example.app_mvvm_kotlin.databinding.ActivityLoginBinding
import com.example.app_mvvm_kotlin.viewmodel.AuthViewModel
import com.example.common.entities.state.AuthEvent
import com.example.common.extensions.value
import com.example.common.ui.activity.BaseVbActivity
import kotlinx.coroutines.launch

/**
 * @author wandervogel
 * @date 2025-09-23  星期二
 * @description 登录
 */
class LoginActivity : BaseVbActivity<ActivityLoginBinding>() {
    private val vm: AuthViewModel by viewModels()
    override fun autoLoading() = false

    override fun initView() {
        super.initView()
        binding.etUsername.doAfterTextChanged { vm.onUsernameChanged(it?.toString().orEmpty()) }
        binding.etPassword.doAfterTextChanged { vm.onPasswordChanged(it?.toString().orEmpty()) }
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.value
            val password = binding.etPassword.value
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.uiState.collect { state ->
                    binding.btnLogin.isEnabled = !state.loading

                }
            }
        }
    }

    private fun observeEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.eventFlow.collect { e ->
                    when (e) {
                        is AuthEvent.Toast -> toast(e.msg)
                        AuthEvent.LoginSuccess -> {
                            startActivity(MainActivity::class.java)
                        }

                        AuthEvent.RegisterSuccess -> {

                        }
                    }
                }
            }
        }
    }
}