package com.example.app_mvvm_kotlin.page.search

import androidx.lifecycle.viewModelScope
import com.example.common.ui.BaseViewModel
import com.example.model.HotKey
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * @author wandervogel
 * @date 2026-02-12  星期四
 * @description
 */
class SearchViewModel() : BaseViewModel() {
    private val repository = SearchRepository()


    val hotKeys: StateFlow<List<HotKey>> =
        repository.observeHotKeys()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    init {
        viewModelScope.launch {
            repository.refreshIfNeed(force = false)
        }
    }
}