package com.example.app_mvvm_kotlin.page.qaa

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_mvvm_kotlin.adapters.ArticlePagingAdapter
import com.example.app_mvvm_kotlin.page.collect.CollectViewModel
import com.example.app_mvvm_kotlin.databinding.FragmentQaaBinding
import com.example.app_mvvm_kotlin.page.article.DetailArticleActivity
import com.example.common.constant.IntentConstant
import com.example.common.extensions.addEqualSpacing
import com.example.common.ui.fragment.StateObserveFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author wandervogel
 * @date 2026-01-14  星期三
 * @description
 */
class QaaFragment : StateObserveFragment<FragmentQaaBinding>() {
    private lateinit var viewModel: QaaViewModel
    private lateinit var collectViewModel: CollectViewModel
    private lateinit var pageAdapter: ArticlePagingAdapter

    override fun initData() {
        super.initData()
        viewModel = ViewModelProvider(requireActivity())[QaaViewModel::class.java]
        collectViewModel = ViewModelProvider(requireActivity())[CollectViewModel::class.java]
    }

    override fun getBaseViewModel() = viewModel

    override fun initView() {
        super.initView()
        pageAdapter = ArticlePagingAdapter().apply {
            onItemClick = { toDetail(it.link) }
            onCollectClick = { isCollect, id ->
                if (isCollect) {
                    collectViewModel.collect(id)
                } else {
                    collectViewModel.cancelCollect(id)
                }
            }
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.addEqualSpacing()
        binding.recyclerView.adapter = pageAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dataFlow.collectLatest { dataList ->
                    pageAdapter.submitData(dataList)
                }
            }
        }
    }

    private fun toDetail(url: String) {
        val bundle = Bundle().apply {
            putString(IntentConstant.KEY_1, url)
        }
        startActivity(DetailArticleActivity::class.java, bundle)
    }

    override fun retry() {
        super.retry()
        pageAdapter.refresh()
    }
}