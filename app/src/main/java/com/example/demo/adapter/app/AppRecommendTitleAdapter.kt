package com.example.demo.adapter.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.databinding.LayoutAppRecommentTitleBinding

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
class AppRecommendTitleAdapter : RecyclerView.Adapter<AppRecommendTitleAdapter.AppRecommendTitleViewHolder>() {

    private var appRecommendTitleState: Boolean = false

    var titleLabel: String? = null
        set(value) {
            field = value
            when {
                value.isNullOrEmpty() && appRecommendTitleState -> {
                    appRecommendTitleState = false
                    notifyItemRemoved(0)
                }
                !value.isNullOrEmpty() && !appRecommendTitleState -> {
                    appRecommendTitleState = true
                    notifyItemInserted(0)
                }
                !value.isNullOrEmpty() && appRecommendTitleState -> {
                    notifyItemChanged(0)
                }
            }
        }

    override fun getItemCount(): Int {
        return if (appRecommendTitleState) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppRecommendTitleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutAppRecommentTitleBinding.inflate(inflater, parent, false)
        return AppRecommendTitleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppRecommendTitleViewHolder, position: Int) {
        with(holder.viewBinding) {
            labelTv.text = titleLabel
        }
    }

    class AppRecommendTitleViewHolder(
        val viewBinding: LayoutAppRecommentTitleBinding
    ) : RecyclerView.ViewHolder(viewBinding.root)

}