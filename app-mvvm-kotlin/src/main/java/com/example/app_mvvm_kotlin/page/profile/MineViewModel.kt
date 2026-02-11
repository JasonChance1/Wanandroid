package com.example.app_mvvm_kotlin.page.profile

import androidx.lifecycle.viewModelScope
import com.example.app_mvvm_kotlin.page.auth.AuthRepository
import com.example.common.ui.BaseViewModel
import com.example.model.ApiResult
import kotlinx.coroutines.launch

/**
 * @author wandervogel
 * @date 2026-02-11  星期三
 * @description
 */
class MineViewModel : BaseViewModel() {
    private val autoRepository = AuthRepository()

    fun logout(onSuccess: (() -> Unit) = {}) {
        viewModelScope.launch {
            when (val r = autoRepository.logout()) {
                is ApiResult.Success -> {
                    onSuccess.invoke()
                }

                is ApiResult.Error -> _events.emit(r.message)
            }
        }
    }

}