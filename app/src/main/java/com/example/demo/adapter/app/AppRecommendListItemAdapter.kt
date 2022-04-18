package com.example.demo.adapter.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.demo.databinding.LayoutAppRecommendListItemBinding
import com.example.demo.model.app.AppItemEntity
import com.example.demo.utils.asAsyncDifferConfig

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
class AppRecommendListItemAdapter :
    ListAdapter<AppItemEntity, AppRecommendListItemAdapter.AppRecommendListItemViewHolder>(
        AppItemEntity.comparator.asAsyncDifferConfig()
    ) {

    private val roundIconOptions: RequestOptions by lazy {
        val roundingRadius = SizeUtils.dp2px(16f)
        RequestOptions().transform(RoundedCorners(roundingRadius))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppRecommendListItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutAppRecommendListItemBinding.inflate(inflater, parent, false)
        return AppRecommendListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppRecommendListItemViewHolder, position: Int) {
        val appItemEntity = getItem(position)
        with(holder.viewBinding) {
            Glide.with(iconIv)
                .load(appItemEntity.icon)
                .apply(roundIconOptions)
                .into(iconIv)
            appNameTv.text = appItemEntity.appName
            appTypeTv.text = appItemEntity.appTypeName
        }
    }

    class AppRecommendListItemViewHolder(
        val viewBinding: LayoutAppRecommendListItemBinding
    ) : RecyclerView.ViewHolder(viewBinding.root)

}