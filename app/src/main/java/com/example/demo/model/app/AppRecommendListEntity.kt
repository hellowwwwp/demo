package com.example.demo.model.app

import androidx.recyclerview.widget.DiffUtil

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
data class AppRecommendListEntity(val items: List<AppItemEntity>? = null) {

    companion object {
        val comparator = object : DiffUtil.ItemCallback<AppRecommendListEntity>() {
            override fun areItemsTheSame(oldItem: AppRecommendListEntity, newItem: AppRecommendListEntity): Boolean {
                return oldItem.items == newItem.items
            }

            override fun areContentsTheSame(oldItem: AppRecommendListEntity, newItem: AppRecommendListEntity): Boolean {
                return oldItem.items == newItem.items
            }
        }
    }

}