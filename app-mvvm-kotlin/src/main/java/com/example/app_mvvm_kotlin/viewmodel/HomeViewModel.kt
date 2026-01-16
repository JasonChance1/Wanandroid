package com.example.app_mvvm_kotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.app_mvvm_kotlin.entiry.page.ArticlesPagingSource
import com.example.app_mvvm_kotlin.repositories.HomeRepository
import com.example.model.ApiResult
import com.example.model.BannerBean
import kotlinx.coroutines.launch

/**
 * @author wandervogel
 * @date 2026-01-15  星期四
 * @description
 */
open class HomeViewModel() : BaseViewModel() {
    private val repository: HomeRepository = HomeRepository()

    private val _bannerList = MutableLiveData<List<BannerBean>>()
    val bannerList: LiveData<List<BannerBean>> = _bannerList

    fun getBanner() {
        viewModelScope.launch {
            when (val r = repository.getBanner()) {
                is ApiResult.Success -> _bannerList.value = r.data ?: emptyList()
                is ApiResult.Error -> {
                    _bannerList.value = emptyList()
                    _events.emit(r.message)
                }
            }
        }
    }

    val articlesPagingFlow = Pager(
        config = PagingConfig(
            pageSize = 20,
            initialLoadSize = 40,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { ArticlesPagingSource(repository, _state) }
    ).flow.cachedIn(viewModelScope)
}
