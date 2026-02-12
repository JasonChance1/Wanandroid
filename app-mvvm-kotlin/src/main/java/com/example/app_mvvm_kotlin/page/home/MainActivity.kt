package com.example.app_mvvm_kotlin.page.home

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.app_mvvm_kotlin.R
import com.example.app_mvvm_kotlin.databinding.ActivityMainBinding
import com.example.common.ui.activity.BaseVbActivity

/**
 * navigation切换fragment使用的replace，切换之后fragment会重建
 */
class MainActivity : BaseVbActivity<ActivityMainBinding>() {
    private val hosts = mutableMapOf<Int, NavHostFragment>()
    private lateinit var currentNavController: NavController

    private val tabGraphs = mapOf(
        R.id.home_graph to R.navigation.nav_home,
        R.id.project_graph to R.navigation.nav_project,
        R.id.qaa_graph to R.navigation.nav_qaa,
        R.id.mine_graph to R.navigation.nav_mine
    )

    private val KEY_SELECTED_TAB = "selected_tab"

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        tabGraphs.forEach { (menuId, graphRes) ->
            val tag = "tab_$menuId"
            val existing = supportFragmentManager.findFragmentByTag(tag) as? NavHostFragment
            val host = existing ?: NavHostFragment.create(graphRes).also { nh ->
                supportFragmentManager.beginTransaction()
                    .add(R.id.nav_host_container, nh, tag)
                    .hide(nh)
                    .commitNow()
            }
            hosts[menuId] = host
        }

        val restoredTab = savedInstanceState?.getInt(KEY_SELECTED_TAB)
        val initialTab = restoredTab ?: R.id.home_graph
        binding.bottomNav.selectedItemId = initialTab
        selectTab(initialTab)

        binding.bottomNav.setOnItemSelectedListener { item ->
            selectTab(item.itemId)
            true
        }

        binding.bottomNav.setOnItemReselectedListener { item ->
            popToTabRoot(item.itemId)
        }

        onBackPressedDispatcher.addCallback(this,object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val popped = currentNavController.popBackStack()
                if (!popped) {
                    val currentTab = binding.bottomNav.selectedItemId
                    if (currentTab != R.id.home_graph) {
                        binding.bottomNav.selectedItemId = R.id.home_graph
                        selectTab(R.id.home_graph)
                    } else {
                        finish()
                    }
                }
            }
        })
    }

    private fun selectTab(menuId: Int) {
        val target = hosts[menuId] ?: return

        supportFragmentManager.beginTransaction().apply {
            hosts.values.forEach { hide(it) }
            show(target)
            // 让系统返回键/FragmentResult等优先落在当前 host
            setPrimaryNavigationFragment(target)
        }.commit()

        currentNavController = target.navController
    }

    private fun popToTabRoot(menuId: Int) {
        val host = hosts[menuId] ?: return
        val nav = host.navController
        nav.popBackStack(nav.graph.startDestinationId, false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_SELECTED_TAB, binding.bottomNav.selectedItemId)
        super.onSaveInstanceState(outState)
    }

    override fun autoLoading() = false
}