package com.example.app_mvvm_kotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.app_mvvm_kotlin.databinding.ItemArticleHomeBinding
import com.example.common.extensions.safeGetStr
import com.example.common.extensions.toDateTimeString
import com.example.model.Article

class ArticlePagingAdapter :
    PagingDataAdapter<Article, ArticlePagingAdapter.VH>(DIFF) {

    var onCollectClick: ((isCollect: Boolean) -> Unit) = {}
    var onItemClick: ((item: Article) -> Unit) = {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemArticleHomeBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: VH, position: Int) {
        getItem(position)?.let { item ->
            holder.binding.apply {
                tvAuthor.text = item.author.safeGetStr
                tvTag.text = item.chapterName.safeGetStr
                ivCollect.isSelected = item.collect
                tvTitle.text = item.title.safeGetStr
                tvTime.text = item.publishTime.toDateTimeString("yyyy-MM-dd")

                ivCollect.setOnClickListener {
                    ivCollect.isSelected = !ivCollect.isSelected
                    onCollectClick.invoke(ivCollect.isSelected)
                }
                root.setOnClickListener {
                    onItemClick.invoke(item)
                }
            }
        }
    }

    class VH(val binding: ItemArticleHomeBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(old: Article, new: Article) = old.id == new.id
            override fun areContentsTheSame(old: Article, new: Article) = old == new
        }
    }
}
