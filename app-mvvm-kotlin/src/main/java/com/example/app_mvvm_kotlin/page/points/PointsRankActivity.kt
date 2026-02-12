package com.example.app_mvvm_kotlin.page.points

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_mvvm_kotlin.adapters.PtsRankAdapter
import com.example.app_mvvm_kotlin.databinding.ActivityPointsRankBinding
import com.example.common.extensions.addEqualSpacing
import com.example.common.ui.activity.StateObserveActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author wandervogel
 * @date 2026-02-12  星期四
 * @description
 */
class PointsRankActivity : StateObserveActivity<ActivityPointsRankBinding>() {
    private lateinit var viewModel: PointsViewModel
    private lateinit var mAdapter: PtsRankAdapter
    override fun getBaseViewModel() = viewModel

    override fun initData() {
        super.initData()
        viewModel = ViewModelProvider(this)[PointsViewModel::class]
        mAdapter = PtsRankAdapter()
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        viewModel.getMyRank()
        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.addEqualSpacing(2)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.rankList().collectLatest { mAdapter.submitData(it) }
            }
        }

        viewModel.myRank.observe(this) {
            binding.tvRank.text = "${it.rank}"
            binding.tvLevel.text = "${it.level}"
            binding.tvName.text = it.username
            binding.tvPts.text = "${it.coinCount}"
        }
    }

    override fun getTitleBar() = binding.titleBar
}