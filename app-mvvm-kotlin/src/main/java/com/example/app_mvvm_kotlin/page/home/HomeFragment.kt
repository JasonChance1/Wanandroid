package com.example.app_mvvm_kotlin.page.home

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_mvvm_kotlin.page.article.DetailArticleActivity
import com.example.app_mvvm_kotlin.adapters.ArticlePagingAdapter
import com.example.app_mvvm_kotlin.page.collect.CollectViewModel
import com.example.app_mvvm_kotlin.databinding.FragmentHomeBinding
import com.example.app_mvvm_kotlin.page.search.SearchActivity
import com.example.common.constant.IntentConstant
import com.example.common.extensions.addEqualSpacing
import com.example.common.ui.fragment.StateObserveFragment
import com.google.android.material.search.SearchView
import com.youth.banner.indicator.CircleIndicator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author wandervogel
 * @date 2026-01-14  星期三
 * @description
 */
class HomeFragment : StateObserveFragment<FragmentHomeBinding>() {
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var collectViewModel: CollectViewModel
    private val mAdapter = ArticlePagingAdapter()
    override fun initView() {
        super.initView()
        startLoading()
        observeState()
        initRecyclerView()
        binding.llSearch.setOnClickListener {
            startActivity(SearchActivity::class.java)
        }
    }

    override fun loadData() {
        viewModel.getBanner()
        collectViewModel = ViewModelProvider(requireActivity())[CollectViewModel::class]
    }

    private fun initRecyclerView() {
        mAdapter.onCollectClick = { isCollect, id ->
            if (isCollect) {
                collectViewModel.collect(id)
            } else {
                collectViewModel.cancelCollect(id)
            }
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
            }).setIndicator(CircleIndicator(requireContext()))
        }
    }

    override fun onLoadSuccess() {
        binding.refreshLayout.finishRefresh()
    }

    override fun getBaseViewModel() = viewModel

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

    override fun retry() {
        super.retry()
        mAdapter.refresh()
    }
}