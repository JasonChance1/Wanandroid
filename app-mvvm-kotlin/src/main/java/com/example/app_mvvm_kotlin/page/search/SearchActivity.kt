package com.example.app_mvvm_kotlin.page.search

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.app_mvvm_kotlin.adapters.SearchHistoryFlexAdapter
import com.example.app_mvvm_kotlin.databinding.ActivitySearchBinding
import com.example.common.ui.activity.StateObserveActivity
import com.example.model.HotKey
import kotlinx.coroutines.launch

/**
 * @author wandervogel
 * @date 2026-02-12  星期四
 * @description 搜索
 */
class SearchActivity : StateObserveActivity<ActivitySearchBinding>() {
    private lateinit var viewModel: SearchViewModel
    private lateinit var searchHistoryFlexAdapter: SearchHistoryFlexAdapter
    override fun getBaseViewModel() = viewModel

    override fun initData() {
        viewModel = ViewModelProvider(this)[SearchViewModel::class]
        searchHistoryFlexAdapter = SearchHistoryFlexAdapter(binding.flowFlexLayout).apply {
            onItemClick = { name ->
                search(name)
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hotKeys.collect { list ->
                    showHotKeys(list)
                }
            }
        }
    }

    private fun showHotKeys(list: List<HotKey>) {
        searchHistoryFlexAdapter.submitList(list.map { it.name })
    }

    private fun search(keyword: String) {

    }

    override fun autoLoading() = false
}