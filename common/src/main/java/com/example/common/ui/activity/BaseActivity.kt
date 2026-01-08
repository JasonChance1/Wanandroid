package com.example.common.ui.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.common.extensions.transparentStatusBar
import com.example.common.ui.state.DefaultStateImpl
import com.example.common.ui.state.IState

/**
 * @author wandervogel
 * @date 2025-09-22  星期一
 * @description
 */
abstract class BaseActivity : AppCompatActivity(), IState {
    private var stateImpl: IState? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT < 35) {
            transparentStatusBar()
        } else {
            enableEdgeToEdge()
        }
        setContentView(getLayoutId())

        stateImpl = getState()
        stateImpl?.let {
            val params = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            addContentView(it.getStateView(), params)
        }

        if (autoLoading()) {
            startLoading()
        } else {
            loadingFinished()
        }
        initView()
        initData()
    }

    protected open fun initView() {

    }

    protected open fun initData() {

    }

    protected open fun getState(): IState {
        return DefaultStateImpl(this)
    }

    override fun startLoading(msg: String) {
        stateImpl?.startLoading(msg)
    }

    override fun loadingFinished() {
        stateImpl?.loadingFinished()
    }

    override fun showBadNetwork(listener: View.OnClickListener) {
        stateImpl?.showBadNetwork(listener)
    }

    override fun showError(tip: String, errorAction: View.OnClickListener?) {
        stateImpl?.showError(tip, errorAction)
    }

    override fun showEmptyView(tip: String, emptyAction: View.OnClickListener?) {
        stateImpl?.showEmptyView(tip, emptyAction)
    }

    abstract fun getLayoutId(): Int

    protected fun autoLoading() = false

    override fun getStateView(): View? = stateImpl?.getStateView()
}