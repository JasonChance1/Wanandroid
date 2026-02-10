package com.example.app_mvvm_kotlin.page.project

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.app_mvvm_kotlin.adapters.FragmentAdapter
import com.example.app_mvvm_kotlin.adapters.FragmentsAdapter
import com.example.app_mvvm_kotlin.adapters.viewpager.PagerPage
import com.example.app_mvvm_kotlin.adapters.viewpager.UniversalPagerAdapter
import com.example.app_mvvm_kotlin.databinding.FragmentMineBinding
import com.example.app_mvvm_kotlin.databinding.FragmentProjectBinding
import com.example.common.ui.fragment.BaseVbFragment
import com.google.android.material.tabs.TabLayoutMediator

/**
 * @author wandervogel
 * @date 2026-01-14  星期三
 * @description
 */
class ProjectFragment : BaseVbFragment<FragmentProjectBinding>() {
    private val viewModel by viewModels<ProjectViewModel>()
    private lateinit var pagerAdapter: UniversalPagerAdapter

    override fun loadData() {
        viewModel.getProjectTree()
    }

    override fun initView() {
        observeState()
    }

    private fun observeState() {
        viewModel.tree.observe(viewLifecycleOwner) { list ->
            if (!::pagerAdapter.isInitialized) {
                pagerAdapter = UniversalPagerAdapter(this)
                pagerAdapter.submitPages(buildList {
                    addAll(list.map { item ->
                        object : PagerPage {
                            override val pageId = item.id
                            override val title = item.name
                            override fun create() =
                                ProjectContentFragment.newInstance(item.id, item.name)
                        }
                    })
                })
                binding.viewPager.adapter = pagerAdapter
                val titles = list.map { it.name }
                binding.viewPager.offscreenPageLimit = 2
                TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
                    tab.text = titles.get(pos)
                }.attach()
            }
        }
    }
}