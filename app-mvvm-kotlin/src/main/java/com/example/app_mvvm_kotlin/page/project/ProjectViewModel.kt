package com.example.app_mvvm_kotlin.page.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.app_mvvm_kotlin.base.BaseViewModel
import com.example.app_mvvm_kotlin.paging.ProjectPagingSource
import com.example.model.ApiResult
import com.example.model.ProjectClassify
import kotlinx.coroutines.launch

/**
 * @author wandervogel
 * @date 2026-02-10  星期二
 * @description
 */
class ProjectViewModel : BaseViewModel() {
    private val repository: ProjectRepository = ProjectRepository()

    private val _tree = MutableLiveData<List<ProjectClassify>>()
    val tree: LiveData<List<ProjectClassify>> = _tree

    fun getProjectTree() {
        viewModelScope.launch {
            when (val r = repository.getProjectTree()) {
                is ApiResult.Success -> _tree.value = r.data
                is ApiResult.Error -> {
                    _tree.value = emptyList()
                    _events.emit(r.message)
                }
            }
        }
    }

    fun articlesFlow(cid: Int) = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { ProjectPagingSource(repository, cid, _state) }
    ).flow.cachedIn(viewModelScope)

}