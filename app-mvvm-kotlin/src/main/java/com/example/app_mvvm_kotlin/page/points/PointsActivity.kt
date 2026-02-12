package com.example.app_mvvm_kotlin.page.points

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_mvvm_kotlin.adapters.PtsPagingAdapter
import com.example.app_mvvm_kotlin.databinding.ActivityPointsBinding
import com.example.common.extensions.addEqualSpacing
import com.example.common.ui.activity.StateObserveActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author wandervogel
 * @date 2026-02-11  星期三
 * @description 积分明细
 */
class PointsActivity : StateObserveActivity<ActivityPointsBinding>() {
    private lateinit var mAdapter: PtsPagingAdapter
    private lateinit var viewModel: PointsViewModel
    override fun getBaseViewModel() = viewModel

    override fun initData() {
        viewModel = ViewModelProvider(this)[PointsViewModel::class.java]
        mAdapter = PtsPagingAdapter()
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.addEqualSpacing()

        binding.titleBar.setOnIcon1ClickListener {
            startActivity(PointsRankActivity::class.java)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pointsFlow().collectLatest {
                    mAdapter.submitData(it)
                }
            }
        }
    }

    override fun getTitleBar() = binding.titleBar
}