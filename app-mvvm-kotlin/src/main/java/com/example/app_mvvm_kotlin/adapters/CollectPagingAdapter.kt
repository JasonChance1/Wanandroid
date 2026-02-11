package com.example.app_mvvm_kotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.app_mvvm_kotlin.databinding.ItemArticleHomeBinding
import com.example.common.extensions.safeGetStr
import com.example.common.extensions.toDateTimeString
import com.example.model.Collect

class CollectPagingAdapter :
    PagingDataAdapter<Collect, CollectPagingAdapter.VH>(DIFF) {

    var onItemRemove: ((id: Int) -> Unit) = {_ -> }
    var onItemClick: ((item: Collect) -> Unit) = {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemArticleHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        getItem(position)?.let { item ->
            holder.binding.apply {
                tvAuthor.text = item.author.safeGetStr
                tvTag.text = item.chapterName.safeGetStr
                ivCollect.isSelected = true
                tvTitle.text = item.title.safeGetStr
                tvTime.text = item.publishTime.toDateTimeString("yyyy-MM-dd")

                ivCollect.setOnClickListener {
                    onItemRemove.invoke(item.id)
                }
                root.setOnClickListener {
                    onItemClick.invoke(item)
                }
            }
        }
    }

    class VH(val binding: ItemArticleHomeBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Collect>() {
            override fun areItemsTheSame(old: Collect, new: Collect) = old.id == new.id
            override fun areContentsTheSame(old: Collect, new: Collect) = old == new
        }
    }
}
