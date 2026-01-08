package com.example.app_mvvm_kotlin.activities.auth

import android.content.Intent
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.app_mvvm_kotlin.databinding.ActivityRegisterBinding
import com.example.app_mvvm_kotlin.viewmodel.RegisterViewModel
import com.example.common.entities.event.RegisterEvent
import com.example.common.ui.activity.BaseVbActivity
import kotlinx.coroutines.launch

/**
 * @author wandervogel
 * @date 2026-01-06  星期二
 * @description
 */
class RegisterActivity : BaseVbActivity<ActivityRegisterBinding>() {

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }

    private val vm: RegisterViewModel by viewModels()

    override fun initView() {
        super.initView()
        with(binding) {
            etUsername.doAfterTextChanged { vm.onUsernameChanged(it?.toString().orEmpty()) }
            etPassword.doAfterTextChanged { vm.onPasswordChanged(it?.toString().orEmpty()) }
            etConfirmPassword.doAfterTextChanged {
                vm.onRepasswordChanged(
                    it?.toString().orEmpty()
                )
            }

            btnRegister.setOnClickListener { vm.register() }
        }
        observeState()
        observeEvent()
    }


    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.uiState.collect { s ->
                    if (s.loading) {
                        showLoading()
                    } else {
                        hideLoading()
                    }
                    binding.btnRegister.isEnabled = !s.loading
                }
            }
        }
    }

    private fun observeEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.eventFlow.collect { e ->
                    when (e) {
                        is RegisterEvent.Toast -> toast(e.msg)
                        is RegisterEvent.RegisterSuccess -> {

                            setResult(RESULT_OK, Intent().putExtra(EXTRA_USERNAME, e.username))
                            finish()
                        }
                    }
                }
            }
        }
    }
}
