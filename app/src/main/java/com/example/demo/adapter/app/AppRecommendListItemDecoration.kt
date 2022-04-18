package com.example.demo.adapter.app

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
class AppRecommendListItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val adapter = parent.adapter as? AppRecommendListItemAdapter
        if (adapter == null) {
            outRect.set(0, 0, 0, 0)
            return
        }
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) {
            outRect.set(0, 0, 0, 0)
            return
        }
        val dp8 = SizeUtils.dp2px(8f)
        val dp16 = SizeUtils.dp2px(16f)
        when (position) {
            0 -> {
                //第一个
                outRect.set(dp16, 0, dp8, 0)
            }
            adapter.itemCount - 1 -> {
                //最后一个
                outRect.set(dp8, 0, dp16, 0)
            }
            else -> {
                //中间的
                outRect.set(dp8, 0, dp8, 0)
            }
        }
    }

}