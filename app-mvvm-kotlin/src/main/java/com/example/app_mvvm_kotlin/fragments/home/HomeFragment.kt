package com.example.app_mvvm_kotlin.fragments.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.app_mvvm_kotlin.databinding.FragmentHomeBinding
import com.example.app_mvvm_kotlin.viewmodel.HomeViewModel
import com.example.common.entities.state.UiState
import com.example.common.ui.fragment.BaseVbFragment
import kotlinx.coroutines.launch

/**
 * @author wandervogel
 * @date 2026-01-14  星期三
 * @description
 */
class HomeFragment : BaseVbFragment<FragmentHomeBinding>() {
    private val viewModel by viewModels<HomeViewModel>()
    override fun initView() {
        super.initView()

        viewModel.getBanner()
        viewModel.getArticles(0)
        observeState()
    }

    private fun initRecyclerView(){

    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        UiState.Idle -> Unit
                        UiState.Loading -> {
                            startLoading()
                        }

                        is UiState.Success -> {
                            state.data?.let {

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