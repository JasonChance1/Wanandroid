package com.example.app_mvvm_kotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.app_mvvm_kotlin.databinding.ItemPtsBinding
import com.example.model.Points

class PtsPagingAdapter :
    PagingDataAdapter<Points, PtsPagingAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemPtsBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        getItem(position)?.let { item ->
            holder.binding.apply {
                tvCurPts.text = "${item.coinCount}"
                tvDate.text = item.displayDate
                tvUsername.text = item.userName
                tvReason.text = item.getDisplayReason()
            }
        }
    }

    class VH(val binding: ItemPtsBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Points>() {
            override fun areItemsTheSame(old: Points, new: Points) = old.id == new.id
            override fun areContentsTheSame(old: Points, new: Points) = old == new
        }
    }
}
