package com.example.demo.adapter.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.databinding.LayoutAppRecommendBinding
import com.example.demo.model.app.AppRecommendListEntity
import com.example.demo.utils.asAsyncDifferConfig

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
class AppRecommendListAdapter : ListAdapter<AppRecommendListEntity, AppRecommendListAdapter.AppRecommendListViewHolder>(
    AppRecommendListEntity.comparator.asAsyncDifferConfig()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppRecommendListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutAppRecommendBinding.inflate(inflater, parent, false)
        return AppRecommendListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppRecommendListViewHolder, position: Int) {
        val context = holder.itemView.context
        val appRecommendListData = getItem(position)
        with(holder.viewBinding) {
            if (recyclerView.layoutManager == null) {
                recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                recyclerView.itemAnimator = null
                recyclerView.addItemDecoration(AppRecommendListItemDecoration())
            }
            val adapter: AppRecommendListItemAdapter
            if (recyclerView.adapter == null) {
                adapter = AppRecommendListItemAdapter()
                recyclerView.adapter = adapter
            } else {
                adapter = recyclerView.adapter as AppRecommendListItemAdapter
            }
            adapter.submitList(appRecommendListData.items)
        }
    }

    class AppRecommendListViewHolder(
        val viewBinding: LayoutAppRecommendBinding
    ) : RecyclerView.ViewHolder(viewBinding.root)

}