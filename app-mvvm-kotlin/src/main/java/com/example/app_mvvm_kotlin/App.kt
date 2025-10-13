package com.example.app_mvvm_kotlin

import android.app.Application
import com.example.common.util.DataStoreUtil

/**
 * @author wandervogel
 * @date 2025-09-23  星期二
 * @description application
 */
class App:Application() {
    override fun onCreate() {
        super.onCreate()
        DataStoreUtil.init(this)
    }
}