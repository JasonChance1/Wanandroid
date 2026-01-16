package com.example.app_mvvm_kotlin.fragments.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_mvvm_kotlin.adapters.ArticlePagingAdapter
import com.example.app_mvvm_kotlin.databinding.FragmentHomeBinding
import com.example.app_mvvm_kotlin.viewmodel.HomeViewModel
import com.example.common.entities.state.UiState
import com.example.common.extensions.addEqualSpacing
import com.example.common.ui.fragment.BaseVbFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author wandervogel
 * @date 2026-01-14  星期三
 * @description
 */
class HomeFragment : BaseVbFragment<FragmentHomeBinding>() {
    private val viewModel by viewModels<HomeViewModel>()
    private val mAdapter = ArticlePagingAdapter()
    override fun initView() {
        super.initView()

        viewModel.getBanner()
        observeState()
        initRecyclerView()
    }

    private fun initRecyclerView() {
//        mAdapter.withLoadStateFooter()// 添加footer
        mAdapter.onCollectClick = { isCollect ->
            // todo 收藏/取消收藏
        }
        mAdapter.onItemClick = {

        }
        binding.rvArticle.layoutManager = LinearLayoutManager(requireContext())
        binding.rvArticle.adapter = mAdapter
        binding.rvArticle.addEqualSpacing()
        binding.refreshLayout.setOnRefreshListener { mAdapter.refresh() }
    }

    override fun autoLoading() = true

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.articlesPagingFlow.collectLatest { pagingData ->
                    loadingFinished()
                    binding.refreshLayout.finishRefresh()
                    mAdapter.submitData(pagingData)
                }
                viewModel.state.collect { state ->
                    when (state) {
                        UiState.Idle -> Unit
                        UiState.Loading -> {
                            startLoading()
                        }

                        is UiState.Success -> {
                            state.data?.let {
                                // todo 更新banner
                            }
                            loadingFinished()
                        }

                        is UiState.Error -> {
                            showError(state.message)
                        }
                    }
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
    }
}