package com.example.app_mvvm_kotlin.activities


import android.widget.Toast
import com.example.app_mvvm_kotlin.R
import com.example.common.ui.activity.BaseActivity

class MainActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_main

    override fun initView() {
        super.initView()
        Toast.makeText(this, "测试", Toast.LENGTH_SHORT).show()
    }
}