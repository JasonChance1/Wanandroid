package com.example.common.ui.activity.state

import android.view.View

interface IState {
    fun startLoading()
    fun loadingFinished()
    fun showBadNetwork(listener: View.OnClickListener)
    fun showError(tip: String = "加载失败", listener: View.OnClickListener? = null)
    fun showEmptyView(tip: String = "暂无内容", listener: View.OnClickListener? = null)
}