package com.example.demo.utils

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.databinding.LayoutLoadMoreItemBinding

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2021/6/23
 */
@Suppress("unused")
class LoadMoreAdapter(
    /**
     * 预加载的位置, 默认 -1 不预加载
     */
    preloadDistance: Int = -1
) : RecyclerView.Adapter<LoadMoreAdapter.LoadMoreViewHolder>() {

    private val loadMoreWrapper = PreloadWrapper(preloadDistance).apply {
        onArrivedPreloadPosition = {
            //预加载
            doLoadMore()
        }

        onRecyclerViewDetachedFromWindow = {
            //避免内存泄露
            removePendingRunnable()
        }
    }

    private val handler = Handler(Looper.getMainLooper())

    private var notifyFinishLoadMore: Runnable? = null

    private var isLoading: Boolean = false

    private object LoadMoreEntity

    private val dataList = ArrayList<LoadMoreEntity>(1)

    /**
     * loadMore 回调
     */
    var onLoadMore: (() -> Unit)? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        loadMoreWrapper.attachToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadMoreViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = LayoutLoadMoreItemBinding.inflate(inflater, parent, false)
        return LoadMoreViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: LoadMoreViewHolder, position: Int) {
        doLoadMore()
    }

    override fun onViewAttachedToWindow(holder: LoadMoreViewHolder) {
        doLoadMore()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setLoadMoreEnabled(isEnabled: Boolean) {
        finishLoadMore(isEnabled, 0)
    }

    fun hasMore(): Boolean {
        return dataList.isNotEmpty()
    }

    @JvmOverloads
    fun finishLoadMore(hasMore: Boolean, delayTime: Long = 100) {
        removePendingRunnable()
        //如果没有更多了就立即关闭 loadMore
        if (!hasMore && dataList.isNotEmpty()) {
            dataList.removeAt(0)
            notifyItemRemoved(0)
        }
        notifyFinishLoadMore = Runnable {
            //延迟更新 loading 状态为 false, 避免短时间内多次 loadMore
            isLoading = false
            //延迟通知列表开启 loadMore, 避免短时间内多次 loadMore
            if (hasMore && dataList.isEmpty()) {
                dataList.add(LoadMoreEntity)
                notifyItemInserted(0)
            }
        }.also {
            handler.postDelayed(it, delayTime)
        }
    }

    private fun removePendingRunnable() {
        notifyFinishLoadMore?.let {
            handler.removeCallbacks(it)
            notifyFinishLoadMore = null
        }
    }

    private fun doLoadMore() {
        if (dataList.isNotEmpty() && !isLoading) {
            //更新 loading 状态为 true
            isLoading = true
            onLoadMore?.invoke()
        }
    }

    class LoadMoreViewHolder(
        val viewBinding: LayoutLoadMoreItemBinding
    ) : RecyclerView.ViewHolder(viewBinding.root)

    /**
     * 主要负责预加载逻辑
     */
    private class PreloadWrapper(
        private val preloadDistance: Int = -1
    ) : View.OnAttachStateChangeListener {

        private var recyclerView: RecyclerView? = null

        var onRecyclerViewDetachedFromWindow: ((RecyclerView) -> Unit)? = null

        var onArrivedPreloadPosition: (() -> Unit)? = null

        private var isScrolling: Boolean = false

        private val onScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                isScrolling = newState == RecyclerView.SCROLL_STATE_DRAGGING
                        || newState == RecyclerView.SCROLL_STATE_SETTLING
            }
        }

        private val onChildAttachStateChangeListener =
            object : RecyclerView.OnChildAttachStateChangeListener {
                override fun onChildViewAttachedToWindow(view: View) {
                    if (isScrolling && isArrivedPreloadPosition(view)) {
                        onArrivedPreloadPosition?.invoke()
                    }
                }

                override fun onChildViewDetachedFromWindow(view: View) {
                    // no op
                }
            }

        fun attachToRecyclerView(recyclerView: RecyclerView) {
            this.recyclerView = recyclerView
            recyclerView.addOnAttachStateChangeListener(this)
            recyclerView.addOnScrollListener(onScrollListener)
            recyclerView.addOnChildAttachStateChangeListener(onChildAttachStateChangeListener)
        }

        /**
         * 判断是否滑到可以预加载的位置
         */
        private fun isArrivedPreloadPosition(view: View): Boolean {
            val recyclerView = this.recyclerView ?: return false
            val itemCount = recyclerView.adapter?.itemCount ?: return false
            val viewHolder = recyclerView.findContainingViewHolder(view)
            if (viewHolder == null || viewHolder is LoadMoreViewHolder) {
                return false
            }
            val position = viewHolder.absoluteAdapterPosition
            return itemCount > 0 && itemCount - position <= preloadDistance
        }

        override fun onViewDetachedFromWindow(v: View) {
            recyclerView?.apply {
                removeOnScrollListener(onScrollListener)
                removeOnChildAttachStateChangeListener(onChildAttachStateChangeListener)
                onRecyclerViewDetachedFromWindow?.invoke(this)
            }
        }

        override fun onViewAttachedToWindow(v: View) {
            // no op
        }
    }
}

