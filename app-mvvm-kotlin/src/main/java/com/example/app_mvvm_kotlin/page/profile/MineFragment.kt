package com.example.app_mvvm_kotlin.page.profile

import com.example.app_mvvm_kotlin.databinding.FragmentMineBinding
import com.example.app_mvvm_kotlin.page.auth.LoginActivity
import com.example.app_mvvm_kotlin.page.collect.MyCollectActivity
import com.example.common.ui.fragment.BaseVbFragment

/**
 * @author wandervogel
 * @date 2026-01-14  星期三
 * @description
 */
class MineFragment : BaseVbFragment<FragmentMineBinding>() {
    override fun initView() {
        super.initView()
        binding.tvUsername.setOnClickListener {
            startActivity(LoginActivity::class.java)
        }

        binding.btnCollect.setOnClickListener {
            startActivity(MyCollectActivity::class.java)
        }
    }
}