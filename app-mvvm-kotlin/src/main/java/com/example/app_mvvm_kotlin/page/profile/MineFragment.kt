package com.example.app_mvvm_kotlin.page.profile

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.app_mvvm_kotlin.databinding.FragmentMineBinding
import com.example.app_mvvm_kotlin.page.auth.LoginActivity
import com.example.app_mvvm_kotlin.page.collect.MyCollectActivity
import com.example.common.constant.DSConstant
import com.example.common.extensions.safeGetStr
import com.example.common.ui.fragment.StateObserveFragment
import com.example.common.util.DataStoreUtil
import com.example.model.Login
import kotlinx.coroutines.launch

/**
 * @author wandervogel
 * @date 2026-01-14  星期三
 * @description
 */
class MineFragment : StateObserveFragment<FragmentMineBinding>() {
    private lateinit var viewModel: MineViewModel

    override fun initData() {
        viewModel = ViewModelProvider(requireActivity())[MineViewModel::class]
    }

    override fun getBaseViewModel() = viewModel

    override fun initView() {
        super.initView()
        binding.tvUsername.setOnClickListener {
            startActivity(LoginActivity::class.java)
        }

        binding.btnCollect.setOnClickListener {
            startActivity(MyCollectActivity::class.java)
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout {
                lifecycleScope.launch { DataStoreUtil.clear() }
                startActivity(LoginActivity::class.java)
            }
        }

        lifecycleScope.launch {
            DataStoreUtil.getObjectOnce<Login>(DSConstant.USER, null)?.let {
                binding.tvUsername.text = it.publicName
                binding.tvUid.text = it.id.safeGetStr
            }
        }
    }
}