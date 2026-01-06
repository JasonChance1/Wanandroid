package com.example.common.ui.state

import android.content.Context
import android.view.View
import android.widget.TextView
import com.example.common.R
import com.example.common.extensions.fadeVisible

/**
 * @author wandervogel
 * @date 2025-09-22  星期一
 * @description 默认状态管理实现
 */
class DefaultStateImpl(
    context: Context
) : IState {
    private var errorRetryTip: TextView? = null
    private var emptyRetryTip: TextView? = null
    private var tvError: TextView? = null
    private var tvBadNet: TextView? = null
    private var tvLoading: TextView? = null
    private var tvEmpty: TextView? = null
    private var stateView: View = View.inflate(context, R.layout.layout_state, null)
    private var loadingView: View? = null
    private var errorView: View? = null
    private var emptyView: View? = null
    private var badNetView: View? = null

    init {
        loadingView = stateView.findViewById(R.id.loading)
        errorView = stateView.findViewById(R.id.loadErrorView)
        emptyView = stateView.findViewById(R.id.emptyView)
        badNetView = stateView.findViewById(R.id.badNetworkView)

        errorView?.apply {
            errorRetryTip = findViewById(R.id.retryTip)
            tvError = findViewById(R.id.loadErrorText)
        }
        emptyView?.apply {
            emptyRetryTip = findViewById(R.id.retryTip)
            tvEmpty = findViewById(R.id.noContentText)
        }
        badNetView?.apply {
            tvBadNet = findViewById(R.id.badNetText)
        }
    }

    override fun startLoading(msg: String) {
        stateView.fadeVisible(true)
        errorView?.fadeVisible(false)
        emptyView?.fadeVisible(false)
        badNetView?.fadeVisible(false)
        loadingView?.apply {
            tvLoading?.text = msg
            fadeVisible(true)
        }
    }

    override fun loadingFinished() {
        stateView.fadeVisible(false)
        loadingView?.fadeVisible(false)
        errorView?.fadeVisible(false)
        emptyView?.fadeVisible(false)
        badNetView?.fadeVisible(false)
    }

    override fun showError(tip: String, errorAction: View.OnClickListener?) {
        stateView.fadeVisible(true)
        loadingView?.fadeVisible(false)
        emptyView?.fadeVisible(false)
        badNetView?.fadeVisible(false)
        errorView?.apply {
            errorRetryTip?.fadeVisible(errorAction != null)
            errorAction?.let {
                setOnClickListener(it)
            }
            tvError?.text = tip
            fadeVisible(true)
        }
    }

    override fun showEmptyView(tip: String, emptyAction: View.OnClickListener?) {
        stateView.fadeVisible(true)
        loadingView?.fadeVisible(false)
        errorView?.fadeVisible(false)
        badNetView?.fadeVisible(false)
        emptyView?.apply {
            emptyAction?.let {
                setOnClickListener(it)
            }
            emptyRetryTip?.fadeVisible(emptyAction != null)
            tvEmpty?.text = tip
            fadeVisible(true)
        }
    }

    override fun showBadNetwork(listener: View.OnClickListener) {
        stateView.fadeVisible(true)
        loadingView?.fadeVisible(false)
        errorView?.fadeVisible(false)
        emptyView?.fadeVisible(false)
        badNetView?.apply {
            setOnClickListener(listener)
            fadeVisible(true)
        }
    }

    override fun getStateView(): View = stateView
}