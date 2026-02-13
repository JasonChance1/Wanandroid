package com.example.app_mvvm_kotlin

import android.app.Application
import com.example.common.util.DataStoreUtil
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.example.common_res.R
import com.example.model.db.AppDatabase

/**
 * @author wandervogel
 * @date 2025-09-23  星期二
 * @description application
 */
class App : Application() {
    lateinit var db: AppDatabase
        private set
    companion object{
        lateinit var instance: App
        init {
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                layout.setPrimaryColorsId(R.color.primary_500)
                ClassicsHeader(context)
            }
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
                layout.setPrimaryColorsId(R.color.primary_500)
                ClassicsFooter(context).setDrawableSize(20f)
            }
        }
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        DataStoreUtil.init(this)

        db = AppDatabase.getInstance(this)
    }

}