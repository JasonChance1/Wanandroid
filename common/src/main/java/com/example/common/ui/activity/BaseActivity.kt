package com.example.common.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.common.R
import com.example.common.extensions.transparentStatusBar
import com.example.common.ui.state.DefaultStateImpl
import com.example.common.ui.state.IState

/**
 * @author wandervogel
 * @date 2025-09-22  星期一
 * @description
 */
abstract class BaseActivity : AppCompatActivity(), IState {
    private var loadingView: View? = null
    private var badNetworkView: View? = null
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
        val stateView = View.inflate(this, R.layout.layout_state, null)
        loadingView = stateView.findViewById(R.id.loading)
        errorView = stateView.findViewById(R.id.loadErrorView)
        emptyView = stateView.findViewById(R.id.emptyView)
        badNetworkView = stateView.findViewById(R.id.badNetworkView)

        stateImpl = getState()
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        addContentView(stateView, params)
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

    protected fun autoLoading() = true

    override fun onDestroy() {
        super.onDestroy()
        overridePendingTransition(R.anim.slide_out, R.anim.slide_in)
    }

    protected open fun startActivity(cls: Class<*>) {
        val intent = Intent(this, cls)
        startActivity(intent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(
                OVERRIDE_TRANSITION_OPEN,
                R.anim.slide_in,
                R.anim.slide_out,
                Color.TRANSPARENT
            )
        } else {
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }

    override fun getStateView(): View? = stateImpl?.getStateView()
}