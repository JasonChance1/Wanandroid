package com.example.app_mvvm_kotlin.page.search

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_mvvm_kotlin.adapters.ArticlePagingAdapter
import com.example.app_mvvm_kotlin.adapters.SearchHistoryFlexAdapter
import com.example.app_mvvm_kotlin.databinding.ActivitySearchBinding
import com.example.app_mvvm_kotlin.extensions.onSearch
import com.example.common.extensions.addEqualSpacing
import com.example.common.extensions.setVisible
import com.example.common.ui.activity.StateObserveActivity
import com.example.model.HotKey
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author wandervogel
 * @date 2026-02-12  星期四
 * @description 搜索
 */
class SearchActivity : StateObserveActivity<ActivitySearchBinding>() {
    private lateinit var viewModel: SearchViewModel
    private lateinit var searchHistoryFlexAdapter: SearchHistoryFlexAdapter
    private lateinit var searchResultAdapter: ArticlePagingAdapter
    override fun getBaseViewModel() = viewModel

    override fun initData() {
        viewModel = ViewModelProvider(this)[SearchViewModel::class]
        searchHistoryFlexAdapter = SearchHistoryFlexAdapter(binding.flowHotKey).apply {
            onItemClick = { name ->
                binding.etSearch.setText(name)
                search()
            }
        }
        searchResultAdapter = ArticlePagingAdapter()
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        binding.etSearch.onSearch { search() }// 按回车键进行搜索
        binding.etSearch.addTextChangedListener {
            binding.llKeys.setVisible(it.isNullOrEmpty())
            if (it.isNullOrEmpty()) {
                binding.rvResult.setVisible(false)
            }
            binding.btnClear.setVisible(!it.isNullOrEmpty())
        }
        binding.btnClear.setOnClickListener { binding.etSearch.setText("") }

        binding.rvResult.adapter = searchResultAdapter
        binding.rvResult.layoutManager = LinearLayoutManager(this)
        binding.rvResult.addEqualSpacing()

        binding.ivBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hotKeys.collect { list ->
                    showHotKeys(list)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchPagingFlow.collectLatest { pagingData ->
                    binding.rvResult.setVisible(true)
                    searchResultAdapter.submitData(pagingData)
                }
            }
        }
    }

    private fun showHotKeys(list: List<HotKey>) {
        searchHistoryFlexAdapter.submitList(list.map { it.name })
    }

    private fun search() {
        val keyword = binding.etSearch.text.toString().trim()
        keyword.takeIf { it.isNotEmpty() }?.let {
            startLoading()
            viewModel.submitKeyword(keyword)
            binding.llKeys.setVisible(false)
        } ?: toast("请输入关键词进行搜索")
    }

    override fun autoLoading() = false

    override fun getStateParent() = binding.flResult
}