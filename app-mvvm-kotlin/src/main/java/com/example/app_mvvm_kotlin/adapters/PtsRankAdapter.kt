package com.example.app_mvvm_kotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.app_mvvm_kotlin.R
import com.example.app_mvvm_kotlin.databinding.ItemPtsBinding
import com.example.app_mvvm_kotlin.databinding.ItemRankBinding
import com.example.common.extensions.setVisible
import com.example.model.Points
import com.example.model.PointsRank

class PtsRankAdapter :
    PagingDataAdapter<PointsRank, PtsRankAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemRankBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        getItem(position)?.let { item ->
            holder.binding.apply {
                tvRank.text = "${item.rank}"
                tvLevel.text = "${item.level}"
                tvNickname.text = item.username
                tvPts.text = "${item.coinCount}"
                ivIcon.setVisible(position < 3)
                tvRank.setVisible(position >= 3)
                ivIcon.setImageResource(
                    when (position) {
                        0 -> com.example.common_res.R.drawable.ic_first
                        1 -> com.example.common_res.R.drawable.ic_second
                        2 -> com.example.common_res.R.drawable.ic_third
                        else -> 0
                    }
                )
            }
        }
    }

    class VH(val binding: ItemRankBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<PointsRank>() {
            override fun areItemsTheSame(old: PointsRank, new: PointsRank) =
                old.userId == new.userId

            override fun areContentsTheSame(old: PointsRank, new: PointsRank) = old == new
        }
    }
}
