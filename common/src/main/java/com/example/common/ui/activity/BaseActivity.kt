package com.example.common.ui.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.common.R
import com.example.common.transparentStatusBar
import com.example.common.ui.activity.state.DefaultStateImpl
import com.example.common.ui.activity.state.IState

/**
 * @author wandervogel
 * @date 2025-09-22  星期一
 * @description
 */
abstract class BaseActivity : AppCompatActivity(), IState {
    private var loadingView: View? = null
    private var badNetView: View? = null
    private var emptyView: View? = null
    private var errorView: View? = null
    private var stateImpl: IState? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT < 35) {
            transparentStatusBar()
        } else {
            enableEdgeToEdge()
        }
        setContentView(getLayoutId())
        val rootView = View.inflate(this, R.layout.layout_state, null)
        loadingView = rootView.findViewById(R.id.loading)
        errorView = rootView.findViewById(R.id.loadErrorView)
        emptyView = rootView.findViewById(R.id.emptyView)
        badNetView = rootView.findViewById(R.id.badNetworkView)

        stateImpl = getState()
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        addContentView(rootView, params)
        if (autoLoading()) {
            startLoading()
        }
        initView()
        initData()
    }

    protected open fun initView() {

    }

    protected open fun initData() {

    }

    protected open fun getState(): IState {
        return DefaultStateImpl(loadingView, emptyView, badNetView, errorView)
    }

    override fun startLoading() {
        stateImpl?.startLoading()
    }

    override fun loadingFinished() {
        stateImpl?.loadingFinished()
    }

    override fun showBadNetwork(listener: View.OnClickListener) {
        stateImpl?.showBadNetwork(listener)
    }

    override fun showError(tip: String, listener: View.OnClickListener?) {
        stateImpl?.showError(tip, listener)
    }

    override fun showEmptyView(tip: String, listener: View.OnClickListener?) {
        stateImpl?.showEmptyView(tip, listener)
    }

    abstract fun getLayoutId(): Int

    protected fun autoLoading() = true
}