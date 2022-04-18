package com.example.demo.adapter.app

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.demo.databinding.LayoutAppListItemBinding
import com.example.demo.model.app.AppItemEntity
import com.example.demo.utils.asAsyncDifferConfig

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
class AppListItemAdapter : ListAdapter<AppItemEntity, AppListItemAdapter.AppListItemViewHolder>(
    AppItemEntity.comparator.asAsyncDifferConfig()
) {

    private val roundIconOptions: RequestOptions by lazy {
        val roundingRadius = SizeUtils.dp2px(8f)
        RequestOptions().transform(RoundedCorners(roundingRadius))
    }

    private val circleIconOptions: RequestOptions by lazy {
        RequestOptions().transform(CircleCrop())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppListItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutAppListItemBinding.inflate(inflater, parent, false)
        return AppListItemViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AppListItemViewHolder, position: Int) {
        val appItemEntity = getItem(position)
        with(holder.viewBinding) {
            appRankTv.text = "${position + 1}"
            val requestOptions: RequestOptions =
                if (position % 2 == 0) {
                    circleIconOptions
                } else {
                    roundIconOptions
                }
            Glide.with(iconIv)
                .load(appItemEntity.icon)
                .apply(requestOptions)
                .into(iconIv)
            appNameTv.text = appItemEntity.appName
            appTypeTv.text = appItemEntity.appTypeName
            ratingBar.setRating(appItemEntity.rating)
            ratingCountTv.text = "(${appItemEntity.ratingCount})"
        }
    }

    class AppListItemViewHolder(
        val viewBinding: LayoutAppListItemBinding
    ) : RecyclerView.ViewHolder(viewBinding.root)

}