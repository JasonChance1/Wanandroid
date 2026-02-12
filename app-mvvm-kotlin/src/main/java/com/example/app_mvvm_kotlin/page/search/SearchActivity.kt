package com.example.app_mvvm_kotlin.page.search

import androidx.lifecycle.ViewModelProvider
import com.example.app_mvvm_kotlin.databinding.ActivitySearchBinding
import com.example.common.ui.BaseViewModel
import com.example.common.ui.activity.StateObserveActivity

/**
 * @author wandervogel
 * @date 2026-02-12  星期四
 * @description 搜索
 */
class SearchActivity :StateObserveActivity<ActivitySearchBinding>(){
    private lateinit var viewModel:SearchViewModel
    override fun getBaseViewModel() = viewModel

    override fun initData() {
        viewModel = ViewModelProvider(this)[SearchViewModel::class]
    }
}