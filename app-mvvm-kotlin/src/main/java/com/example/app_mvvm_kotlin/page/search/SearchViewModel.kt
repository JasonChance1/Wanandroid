package com.example.app_mvvm_kotlin.page.search

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.app_mvvm_kotlin.paging.CommonPagingSource
import com.example.common.ui.BaseViewModel
import com.example.model.Article
import com.example.model.HotKey
import com.example.model.PageData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * @author wandervogel
 * @date 2026-02-12  星期四
 * @description
 */
class SearchViewModel() : BaseViewModel() {
    private val repository = SearchRepository()
    private val _keyword = MutableStateFlow<String?>(null)


    val hotKeys: StateFlow<List<HotKey>> =
        repository.observeHotKeys()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    init {
        viewModelScope.launch {
            repository.refreshIfNeed(force = false)
        }
    }

    val searchPagingFlow: Flow<PagingData<Article>> =
        _keyword
            .filterNotNull()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .distinctUntilChanged()
            .flatMapLatest { keyword ->
                Pager(PagingConfig(pageSize = 20)) {
                    CommonPagingSource(
                        state = _state,
                        fetch = { page -> repository.search(page, keyword) },
                        mapper = { it as? PageData<Article> }
                    )
                }.flow
            }
            .cachedIn(viewModelScope)

    fun submitKeyword(keyword: String) {
        _keyword.value = keyword
    }
}