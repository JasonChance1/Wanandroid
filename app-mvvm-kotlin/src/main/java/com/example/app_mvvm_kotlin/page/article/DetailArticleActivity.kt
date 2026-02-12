package com.example.app_mvvm_kotlin.page.article

import android.os.Bundle
import com.example.app_mvvm_kotlin.databinding.ActivityDetailArticleBinding
import com.example.app_mvvm_kotlin.extensions.toast
import com.example.common.constant.IntentConstant
import com.example.common.ui.activity.BaseVbActivity

/**
 * @author wandervogel
 * @date 2026-01-16  星期五
 * @description
 */
class DetailArticleActivity : BaseVbActivity<ActivityDetailArticleBinding>() {
    private lateinit var detailUrl: String
    override fun initData() {
        intent?.getStringExtra(IntentConstant.KEY_1)?.takeIf { it.isNotEmpty() }?.let {
            detailUrl = it
        } ?: run {
            "无法查看该文章".toast()
            finish()
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        binding.webView.loadUrl(detailUrl)

    }
    override fun getTitleBar() = binding.titleBar
}