package com.example.common.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.common.R
import com.example.common.ui.activity.state.DefaultStateImpl
import com.example.common.ui.activity.state.IState
import java.lang.reflect.ParameterizedType

/**
 * @author wandervogel
 * @date 2025-09-23  星期二
 * @description
 */
abstract class BaseVbActivity<VB : ViewBinding> : AppCompatActivity(), IState {
    private var loadingView: View? = null
    private var errorView: View? = null
    private var emptyView: View? = null
    private var badNetworkView: View? = null
    private var stateImpl: IState? = null
    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding
            ?: throw IllegalStateException("Binding should not be accessed after onDestroy()")

    private val tag = this::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        preprocessing()
        super.onCreate(savedInstanceState)
        _binding = createBinding() ?: createBinding(layoutInflater)
        setContentView(binding.root)
        log("onCreate")
        initState()
        if (autoLoading()) {
            startLoading()
        } else {
            loadingFinished()
        }
        initView()
    }

    protected open fun preprocessing(){

    }

    private fun initState() {
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

    }

    protected open fun initView() {

    }

    protected open fun initData() {

    }

    protected open fun getState(): IState {
        return DefaultStateImpl(this)
    }

    @Suppress("UNCHECKED_CAST")
    private fun createBinding(inflater: LayoutInflater): VB {
        try {
            // 通过反射获取泛型类 VB 的实际类型
            val superClass = javaClass.genericSuperclass
            val type = (superClass as ParameterizedType).actualTypeArguments[0]
            val clazz = type as Class<VB>

            // 调用 inflate 方法
            val method = clazz.getMethod("inflate", LayoutInflater::class.java)
            return method.invoke(null, inflater) as VB
        } catch (e: Exception) {
            throw RuntimeException("Failed to create binding instance", e)
        }
    }

    protected fun createBinding(): VB? = null

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
        overridePendingTransition(R.anim.slide_out, R.anim.slide_in)
        _binding = null
    }

    override fun startLoading(msg:String) {
        stateImpl?.startLoading(msg)
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

    protected open fun autoLoading() = true

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

    private fun log(msg:String){
        Log.e(tag,"----------$msg----------")
    }

    override fun getStateView(): View? = stateImpl?.getStateView()
}