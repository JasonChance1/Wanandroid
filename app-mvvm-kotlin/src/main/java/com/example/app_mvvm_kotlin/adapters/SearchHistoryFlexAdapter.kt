package com.example.app_mvvm_kotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.app_mvvm_kotlin.databinding.ItemSearchHistoryBinding
import common.widiget.FlowFlexLayout

class SearchHistoryFlexAdapter(
    private val parent: FlowFlexLayout
) {
    var onItemClick: ((String) -> Unit)? = null

    private val data = mutableListOf<String>()

    fun submitList(list: List<String>) {
        data.clear()
        data.addAll(list)
        notifyChanged()
    }

    private fun notifyChanged() {
        parent.removeAllViews()

        val inflater = LayoutInflater.from(parent.context)
        data.forEach { keyword ->
            val binding = ItemSearchHistoryBinding.inflate(inflater, parent, false)

            binding.tvText.text = keyword

            binding.root.setOnClickListener {
                onItemClick?.invoke(keyword)
            }

            binding.tvText.maxWidth = (parent.resources.displayMetrics.widthPixels * 0.6f).toInt()

            val lp = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            binding.root.layoutParams = lp

            parent.addView(binding.root)
        }

        parent.requestLayout()
    }
}
