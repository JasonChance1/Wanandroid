package com.example.app_mvvm_kotlin.page.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.app_mvvm_kotlin.databinding.ItemBannerBinding
import com.example.model.BannerBean
import com.youth.banner.adapter.BannerAdapter

/**
 * @author wandervogel
 * @date 2026-01-16  星期五
 * @description
 */
class HomeBannerAdapter(datas: List<BannerBean>) :
    BannerAdapter<BannerBean, HomeBannerAdapter.ViewHolder>(datas) {
    class ViewHolder(val binding: ItemBannerBinding) : RecyclerView.ViewHolder(binding.root)

    var onItemClick: ((url: String) -> Unit) = {}
    override fun onCreateHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindView(holder: ViewHolder, data: BannerBean, position: Int, size: Int) {
        holder.binding.apply {
            Glide.with(ivCover).load(data.imagePath)
                .placeholder(com.example.common.R.drawable.no_data)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(30))).into(ivCover)

            root.setOnClickListener { onItemClick.invoke(data.url) }
        }
    }
}