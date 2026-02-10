package com.example.app_mvvm_kotlin.adapters.viewpager

import androidx.fragment.app.Fragment

interface PagerPage {
    val pageId: Int          // 稳定且唯一，用来正确恢复/复用
    val title: String   // Tab 标题（不需要 TabLayout 时也可不用）
    fun create(): Fragment    // 创建 Fragment（每次都 new，不缓存实例）
}
