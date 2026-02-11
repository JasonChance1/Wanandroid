package com.example.common.ui.fragment

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.common.entities.state.UiState
import kotlinx.coroutines.launch
import androidx.lifecycle.repeatOnLifecycle
import com.example.common.ui.BaseViewModel

/**
 * @author wandervogel
 * @date 2026-02-11  星期三
 * @description
 */
abstract class StateObserveFragment<VB : ViewBinding> : BaseVbFragment<VB>() {
    abstract fun getBaseViewModel(): BaseViewModel
    open fun retry() {
        startLoading()
    }

    override fun initView() {
        super.initView()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                getBaseViewModel().state.collect { state ->
                    when (state) {
                        UiState.Loading,
                        UiState.Idle -> Unit

                        is UiState.Success -> {
                            onLoadSuccess()
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

    override fun showError(tip: String, errorAction: View.OnClickListener?) {
        super.showError(tip) { retry() }
    }

    override fun showBadNetwork(listener: View.OnClickListener) {
        super.showBadNetwork { retry() }
    }

    override fun showEmptyView(tip: String, emptyAction: View.OnClickListener?) {
        super.showEmptyView(tip) { retry() }
    }

    open fun onLoadSuccess() {}
}