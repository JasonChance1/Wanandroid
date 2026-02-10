package com.example.app_mvvm_kotlin.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @author wandervogel
 * @date 2026-02-10  星期二
 * @description
 */
class FragmentAdapter(mFragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(
        mFragmentManager, lifecycle
    ) {
    private val mFragment: MutableList<Fragment> = ArrayList()
    private lateinit var mTitles: List<String>

    fun resetFragments(fragments: List<Fragment>?) {
        fragments?.apply {
            mFragment.clear()
            mFragment.addAll(this)
        }
    }

    fun title(position: Int): String {
        return mTitles[position]
    }

    fun resetTitles(titles: List<String>) {
        mTitles = titles
    }

    override fun getItemCount(): Int {
        return mFragment.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragment[position]
    }
}