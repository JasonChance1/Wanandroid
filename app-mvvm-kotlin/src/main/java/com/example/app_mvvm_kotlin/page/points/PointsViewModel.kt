package com.example.app_mvvm_kotlin.page.points

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.app_mvvm_kotlin.paging.CommonPagingSource
import com.example.app_mvvm_kotlin.paging.PointsRankPagingSource
import com.example.common.ui.BaseViewModel
import com.example.model.ApiResult
import com.example.model.PageData
import com.example.model.Points
import com.example.model.PointsRank
import kotlinx.coroutines.launch

/**
 * @author wandervogel
 * @date 2026-02-11  星期三
 * @description
 */
class PointsViewModel : BaseViewModel() {
    private val repository = PointsRepository()
    private val _myRank = MutableLiveData<PointsRank>()
    val myRank: LiveData<PointsRank> = _myRank

    fun pointsFlow() =
        Pager(PagingConfig(pageSize = 20,initialLoadSize = 40)) {
            CommonPagingSource(
                state = _state,
                fetch = { page -> repository.getPointList(page) },
                mapper = {
                    it as? PageData<Points>
                }
            )
        }.flow.cachedIn(viewModelScope)

    fun rankList() =
        Pager(
            PagingConfig(
                pageSize = 20,
                initialLoadSize = 40,
                enablePlaceholders = false
            )
        ) {
            PointsRankPagingSource(repository, _state)
        }.flow.cachedIn(viewModelScope)

    fun getMyRank() {
        viewModelScope.launch {
            when (val r = repository.getMyRank()) {
                is ApiResult.Success -> _myRank.value = r.data
                is ApiResult.Error -> _events.emit(r.message)
            }
        }
    }
}