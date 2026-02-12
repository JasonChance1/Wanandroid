package com.example.common.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.BarUtils
import com.example.common.R
import com.example.common.extensions.transparentStatusBar
import com.example.common.ui.dialog.LoadingDialog
import com.example.common.ui.state.DefaultStateImpl
import com.example.common.ui.state.IState
import common.widiget.TitleBar
import java.lang.reflect.ParameterizedType

/**
 * @author wandervogel
 * @date 2025-09-23  星期二
 * @description
 */
abstract class BaseVbActivity<VB : ViewBinding> : AppCompatActivity(), IState {
    private var stateImpl: IState? = null
    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding
            ?: throw IllegalStateException("Binding should not be accessed after onDestroy()")
    private val loadingDialog by lazy {
        LoadingDialog(this)
    }
    private val tag = this::class.simpleName
    private val windowInsetsController by lazy {
        WindowCompat.getInsetsController(
            window,
            window.decorView
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        preprocessing()
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT < 35) {
            transparentStatusBar()
        } else {
            enableEdgeToEdge()
        }
        _binding = createBinding() ?: createBinding(layoutInflater)
        val frameLayout = FrameLayout(this).apply {
            setBackgroundColor(
                resources.getColor(
                    com.example.common_res.R.color.colorBackground,
                    theme
                )
            )
        }
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        ).apply {
            setMargins(0, BarUtils.getStatusBarHeight(), 0, 0)
        }
        binding.root.layoutParams = params
        frameLayout.addView(binding.root)
        setContentView(frameLayout)
        BarUtils.setStatusBarColor(
            window,
            resources.getColor(com.example.common_res.R.color.colorPrimary, theme)
        )
        log("onCreate")
        initState()
        if (autoLoading()) {
            startLoading()
        } else {
            loadingFinished()
        }
        initData()
        setImmersion()
        getTitleBar()?.let {
            it.setOnBackClickListener { onBackPressedDispatcher.onBackPressed() }
        }
        initView(savedInstanceState)
    }

    protected open fun preprocessing() {

    }

    private fun initState() {
        stateImpl = getState()
        stateImpl?.let {
            val params = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            addContentView(it.getStateView(), params)
        }
    }

    protected open fun initView(savedInstanceState: Bundle?) {

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

    protected open fun createBinding(): VB? = null

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
        overridePendingTransition(R.anim.slide_out, R.anim.slide_in)
        _binding = null
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

    private fun log(msg: String) {
        Log.e(tag, "----------$msg----------")
    }

    override fun getStateView(): View? = stateImpl?.getStateView()

    protected open fun toast(msg: String?, duration: Int = Toast.LENGTH_SHORT) {
        msg.takeIf { !it.isNullOrEmpty() }?.let {
            Toast.makeText(this, msg, duration).show()
        }
    }

    protected open fun showLoading(msg: String = "加载中...") {
        loadingDialog.show(msg)
    }

    protected open fun hideLoading() {
        loadingDialog.hide()
    }

    protected open fun isImmersion(): Boolean = false

    private fun setImmersion() {
        if (isImmersion()) {
            hideBar()
        }
    }

    open fun hideBar() {
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())// 隐藏状态栏
    }

    open fun showBar() {
        windowInsetsController.show(WindowInsetsCompat.Type.systemBars())// 隐藏状态栏
    }

    open fun getTitleBar(): TitleBar? = null
}