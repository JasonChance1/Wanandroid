package com.example.app_mvvm_kotlin.fragments.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_mvvm_kotlin.activities.DetailArticleActivity
import com.example.app_mvvm_kotlin.adapters.ArticlePagingAdapter
import com.example.app_mvvm_kotlin.adapters.HomeBannerAdapter
import com.example.app_mvvm_kotlin.databinding.FragmentHomeBinding
import com.example.app_mvvm_kotlin.viewmodel.HomeViewModel
import com.example.common.constant.IntentConstant
import com.example.common.entities.state.UiState
import com.example.common.extensions.addEqualSpacing
import com.example.common.ui.fragment.BaseVbFragment
import com.youth.banner.indicator.CircleIndicator
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
        startLoading()
        observeState()
        initRecyclerView()
    }

    override fun loadData() {
        viewModel.getBanner()
    }

    private fun initRecyclerView() {
//        mAdapter.withLoadStateFooter()// 添加footer
        mAdapter.onCollectClick = { isCollect ->
            // todo 收藏/取消收藏
        }
        mAdapter.onItemClick = {
            toDetail(it.link)
        }
        binding.rvArticle.layoutManager = LinearLayoutManager(requireContext())
        binding.rvArticle.adapter = mAdapter
        binding.rvArticle.addEqualSpacing()
        binding.refreshLayout.setOnRefreshListener { mAdapter.refresh() }
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.articlesPagingFlow.collectLatest { pagingData ->
                    mAdapter.submitData(pagingData)
                }
            }
        }

        viewModel.bannerList.observe(viewLifecycleOwner) {
            binding.banner.setAdapter(HomeBannerAdapter(it).apply {
                onItemClick = {
                    toDetail(it)
                }
            })
                .setIndicator(CircleIndicator(requireContext()))
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        UiState.Loading,
                        UiState.Idle -> Unit

                        is UiState.Success -> {
                            binding.refreshLayout.finishRefresh()
                            loadingFinished()
                        }

                        is UiState.Error -> showError()
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

    override fun doOnOnCreateView() {
        binding.banner.addBannerLifecycleObserver(this)
    }

    override fun onStart() {
        super.onStart()
        binding.banner.start()
    }

    override fun onStop() {
        super.onStop()
        binding.banner.stop()
    }

    override fun doOnOnDestroy() {
        binding.banner.onDestroy(this)
    }

    private fun toDetail(url: String) {
        val bundle = Bundle().apply {
            putString(IntentConstant.KEY_1, url)
        }
        startActivity(DetailArticleActivity::class.java, bundle)
    }

    override fun showEmptyView(tip: String, emptyAction: View.OnClickListener?) {
        super.showEmptyView(tip) {
            mAdapter.retry()
        }
    }
}