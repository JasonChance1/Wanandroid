package com.example.common.ui.activity.state

import android.view.View
import com.example.common.setVisible

/**
 * @author wandervogel
 * @date 2025-09-22  星期一
 * @description 默认状态管理实现
 */
class DefaultStateImpl(
    var loadingView: View?,
    var emptyView: View?,
    var networkView: View?,
    var errorView: View?
) : IState {
    override fun startLoading() {
        loadingView?.setVisible(true)
        emptyView?.setVisible(false)
        networkView?.setVisible(false)
        errorView?.setVisible(false)
    }

    override fun loadingFinished() {
        loadingView?.setVisible(false)
        emptyView?.setVisible(false)
        networkView?.setVisible(false)
        errorView?.setVisible(false)
    }

    override fun showBadNetwork(listener: View.OnClickListener) {
        loadingView?.setVisible(false)
        emptyView?.setVisible(false)
        networkView?.setVisible(true)
        errorView?.setVisible(false)
    }

    override fun showError(tip: String, listener: View.OnClickListener?) {
        loadingView?.setVisible(false)
        emptyView?.setVisible(false)
        networkView?.setVisible(false)
        errorView?.setVisible(true)
    }

    override fun showEmptyView(tip: String, listener: View.OnClickListener?) {
        loadingView?.setVisible(false)
        emptyView?.setVisible(true)
        networkView?.setVisible(false)
        errorView?.setVisible(false)
    }
}