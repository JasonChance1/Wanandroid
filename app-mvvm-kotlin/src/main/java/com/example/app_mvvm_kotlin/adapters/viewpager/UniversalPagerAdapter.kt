package com.example.app_mvvm_kotlin.adapters.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class UniversalPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private var pages: List<PagerPage> = emptyList()

    fun submitPages(newPages: List<PagerPage>) {
        pages = newPages
        notifyDataSetChanged()
    }

    override fun getItemCount() = pages.size

    override fun createFragment(position: Int): Fragment = pages[position].create()

    override fun getItemId(position: Int): Long = pages[position].pageId.toLong()

    override fun containsItem(itemId: Long): Boolean = pages.any { it.pageId.toLong() == itemId }

    fun getTitle(position: Int): CharSequence = pages[position].title
}
