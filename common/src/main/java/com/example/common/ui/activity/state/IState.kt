package com.example.common.ui.activity.state

import android.view.View

interface IState {
    fun startLoading(msg:String = "加载中...")
    fun loadingFinished()
    fun showBadNetwork(listener: View.OnClickListener)
    fun showError(tip: String = "加载失败", errorAction: View.OnClickListener? = null)
    fun showEmptyView(tip: String = "暂无内容", emptyAction: View.OnClickListener? = null)
    fun getStateView():View?
}