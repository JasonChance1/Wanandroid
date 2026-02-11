package com.example.app_mvvm_kotlin.page.project

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_mvvm_kotlin.adapters.ArticlePagingAdapter
import com.example.app_mvvm_kotlin.common.collect.CollectViewModel
import com.example.app_mvvm_kotlin.databinding.FragmentProjectContentBinding
import com.example.app_mvvm_kotlin.page.article.DetailArticleActivity
import com.example.common.constant.IntentConstant
import com.example.common.entities.state.UiState
import com.example.common.extensions.addEqualSpacing
import com.example.common.ui.fragment.BaseVbFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author wandervogel
 * @date 2026-02-10  星期二
 * @description
 */
class ProjectContentFragment : BaseVbFragment<FragmentProjectContentBinding>() {
    private var cid = 0
    private var titleName: String = ""
    private lateinit var viewModel:ProjectViewModel
    private lateinit var collectViewModel: CollectViewModel
    private lateinit var articleAdapter: ArticlePagingAdapter

    companion object {
        fun newInstance(cid: Int, titleName: String) = ProjectContentFragment().apply {
            arguments = Bundle().apply {
                putInt(IntentConstant.KEY_1, cid)
                putString(IntentConstant.KEY_2, titleName)
            }
        }
    }

    override fun initData() {
        collectViewModel = ViewModelProvider(requireActivity())[CollectViewModel::class]
        viewModel = ViewModelProvider(requireActivity())[ProjectViewModel::class]
        arguments?.let {
            cid = it.getInt(IntentConstant.KEY_1)
            titleName = it.getString(IntentConstant.KEY_2, "")
        }
    }

    override fun autoLoading() = true
    override fun initView() {
        super.initView()
        articleAdapter = ArticlePagingAdapter().apply {
            onItemClick = { toDetail(it.link) }
            onCollectClick = { isCollect, id ->
                if (isCollect) {
                    collectViewModel.collect(id)
                } else {
                    collectViewModel.cancelCollect(id)
                }
            }
        }
        binding.recyclerView.adapter = articleAdapter
        binding.recyclerView.addEqualSpacing()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.articlesFlow(cid).collectLatest { pagingData ->
                    articleAdapter.submitData(pagingData)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { msg ->
                    toast(msg)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        UiState.Loading,
                        UiState.Idle -> Unit

                        is UiState.Success -> {
                            loadingFinished()
                        }

                        is UiState.Error -> showError()
                    }
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
}