package com.example.common.ui.activity

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.example.common.entities.state.UiState
import com.example.common.ui.BaseViewModel
import kotlinx.coroutines.launch

/**
 * @author wandervogel
 * @date 2026-02-11  星期三
 * @description
 */
abstract class StateObserveActivity<VB : ViewBinding> : BaseVbActivity<VB>() {
    abstract fun getBaseViewModel(): BaseViewModel
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                getBaseViewModel().state.collect { state ->
                    when (state) {
                        UiState.Loading,
                        UiState.Idle -> Unit

                        is UiState.Success -> {
                            loadingFinished()
                        }

                        is UiState.Empty -> showEmptyView()

                        is UiState.Error -> showError()
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                getBaseViewModel().events.collect { msg ->
                    toast(msg)
                }
            }
        }
    }
}