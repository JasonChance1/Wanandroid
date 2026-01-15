package com.example.app_mvvm_kotlin

import android.app.Application
import com.example.common.util.DataStoreUtil
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.example.common_res.R

/**
 * @author wandervogel
 * @date 2025-09-23  星期二
 * @description application
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DataStoreUtil.init(this)
        initSmartRefresh()
    }

    private fun initSmartRefresh() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.primary_500)
            ClassicsHeader(this@App)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.primary_500)
            ClassicsFooter(this@App).setDrawableSize(20f)
        }
    }
}