package com.example.common.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.common.ui.dialog.LoadingDialog
import com.example.common.ui.state.DefaultStateImpl
import com.example.common.ui.state.IState
import java.lang.reflect.ParameterizedType

/**
 * @author wandervogel
 * @date 2026-01-14  星期三
 * @description
 */
abstract class BaseVbFragment<VB : ViewBinding> : Fragment(), IState {
    private var stateImpl: IState? = null
    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding ?: throw IllegalStateException("Binding should not be accessed after onDestroyView()")

    private val loadingDialog by lazy {
        LoadingDialog(requireContext())
    }
    private val tag = this::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preprocessing()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        log("onCreateView")
        _binding = createBinding() ?: createBinding(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initState()
        if (autoLoading()) {
            startLoading()
        } else {
            loadingFinished()
        }
        initView()
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        log("onDestroyView")
        _binding = null
    }

    protected open fun preprocessing() {
        // 预处理逻辑，例如权限检查等
    }

    private fun initState() {
        stateImpl = getState()
        stateImpl?.let { state ->
            val params = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            (binding.root as? ViewGroup)?.addView(state.getStateView(), params)
        }
    }

    protected open fun initView() {
        // 初始化视图
    }

    protected open fun initData() {
        // 初始化数据
    }

    protected open fun getState(): IState {
        return DefaultStateImpl(requireContext())
    }

    @Suppress("UNCHECKED_CAST")
    private fun createBinding(inflater: LayoutInflater): VB {
        try {
            // 通过反射获取泛型类 VB 的实际类型
            val superClass = javaClass.genericSuperclass
            val type = (superClass as ParameterizedType).actualTypeArguments[0]
            val clazz = type as Class<VB>

            // 检查 inflate 方法的签名
            val methods = clazz.methods
            val inflateMethod = methods.find { method ->
                method.name == "inflate" && method.parameterCount == 3 &&
                        method.parameterTypes[0] == LayoutInflater::class.java &&
                        method.parameterTypes[1] == ViewGroup::class.java &&
                        method.parameterTypes[2] == Boolean::class.java
            }

            return if (inflateMethod != null) {
                // 使用 inflate(LayoutInflater, ViewGroup?, Boolean) 方法
                inflateMethod.invoke(null, inflater, null, false) as VB
            } else {
                // 尝试使用 inflate(LayoutInflater) 方法
                val method = clazz.getMethod("inflate", LayoutInflater::class.java)
                method.invoke(null, inflater) as VB
            }
        } catch (e: Exception) {
            throw RuntimeException("Failed to create binding instance", e)
        }
    }

    protected open fun createBinding(): VB? = null

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

    override fun getStateView(): View? = stateImpl?.getStateView()

    protected open fun autoLoading() = false

    protected open fun toast(msg: String?, duration: Int = Toast.LENGTH_SHORT) {
        msg.takeIf { !it.isNullOrEmpty() }?.let {
            Toast.makeText(requireContext(), msg, duration).show()
        }
    }

    protected open fun showLoadingDialog(msg: String = "加载中...") {
        loadingDialog.show(msg)
    }

    protected open fun hideLoading() {
        loadingDialog.hide()
    }

    protected open fun startActivity(cls: Class<*>) {
        val intent = Intent(requireContext(), cls)
        startActivity(intent)
        // Fragment 中的转场动画通常在 Activity 中设置
    }

    protected open fun startActivity(cls: Class<*>, bundle: Bundle) {
        val intent = Intent(requireContext(), cls)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun log(msg: String) {
        Log.e(tag, "----------$msg----------")
    }
}