package com.example.app_mvvm_kotlin.page.collect

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_mvvm_kotlin.adapters.ArticlePagingAdapter
import com.example.app_mvvm_kotlin.adapters.CollectPagingAdapter
import com.example.app_mvvm_kotlin.databinding.ActivityCollectBinding
import com.example.app_mvvm_kotlin.page.article.DetailArticleActivity
import com.example.common.constant.IntentConstant
import com.example.common.entities.state.UiState
import com.example.common.extensions.addEqualSpacing
import com.example.common.ui.activity.BaseVbActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author wandervogel
 * @date 2026-02-11  星期三
 * @description 我的收藏
 */
class MyCollectActivity : BaseVbActivity<ActivityCollectBinding>() {
    private val viewModel by viewModels<CollectViewModel>()
    private val mAdapter = CollectPagingAdapter()
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        loadingFinished()
        observeState()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mAdapter.onItemRemove = { id ->
            viewModel.cancelCollect(id)
        }
        mAdapter.onItemClick = {
            toDetail(it.link)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.addEqualSpacing()
    }

    private fun toDetail(url: String) {
        val intent = Intent(this, DetailArticleActivity::class.java)
        intent.putExtra(IntentConstant.KEY_1, url)
        startActivity(intent)
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.collectList.collectLatest { pagingData ->
                    mAdapter.submitData(pagingData)
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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { msg ->
                    if (msg == CollectViewModel.CANCEL_SUCCESS) {
//                        viewModel.updatePagingSource()
                    } else {
                        toast(msg)
                    }
                }
            }
        }
    }

}