package com.example.app_mvvm_kotlin.page.search

import com.example.common.ui.BaseViewModel
import com.example.model.ApiResult
import com.example.model.HotKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * @author wandervogel
 * @date 2026-02-12  星期四
 * @description
 */
class SearchViewModel :BaseViewModel() {
    private val repository = SearchRepository()

    fun hotKeysFlow(): Flow<List<HotKey>> = flow {
        val rsp = when(val r = repository.hotKeys()){
            is ApiResult.Success->r.data.orEmpty() // 成功显示，不成功不处理
            else-> emptyList()
        }
        emit(rsp)
    }.flowOn(Dispatchers.IO)

}