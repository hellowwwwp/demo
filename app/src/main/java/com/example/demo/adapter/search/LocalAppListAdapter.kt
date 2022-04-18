package com.example.demo.adapter.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.demo.databinding.LayoutLocalAppListItemBinding
import com.example.demo.model.search.LocalAppEntity
import com.example.demo.utils.asAsyncDifferConfig

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
class LocalAppListAdapter : ListAdapter<LocalAppEntity, LocalAppListAdapter.LocalAppListViewHolder>(
    LocalAppEntity.comparator.asAsyncDifferConfig()
) {

    private val roundIconOptions: RequestOptions by lazy {
        val roundingRadius = SizeUtils.dp2px(12f)
        RequestOptions().transform(RoundedCorners(roundingRadius))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalAppListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutLocalAppListItemBinding.inflate(inflater, parent, false)
        return LocalAppListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocalAppListViewHolder, position: Int) {
        val localAppEntity = getItem(position)
        with(holder.viewBinding) {
            Glide.with(iconIv)
                .load(localAppEntity.icon)
                .apply(roundIconOptions)
                .into(iconIv)
            appNameTv.text = localAppEntity.appName
        }
    }

    class LocalAppListViewHolder(
        val viewBinding: LayoutLocalAppListItemBinding
    ) : RecyclerView.ViewHolder(viewBinding.root)
}