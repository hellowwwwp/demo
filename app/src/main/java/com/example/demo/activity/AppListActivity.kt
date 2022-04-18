package com.example.demo.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.example.demo.adapter.app.AppListItemAdapter
import com.example.demo.adapter.app.AppRecommendListAdapter
import com.example.demo.adapter.app.AppRecommendTitleAdapter
import com.example.demo.databinding.ActivityAppListBinding
import com.example.demo.utils.LoadMoreAdapter
import com.example.demo.utils.submitListAwaitCommitted
import com.example.demo.viewmodel.AppListViewModel
import com.example.demo.viewstate.MainViewState
import kotlinx.coroutines.launch

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
class AppListActivity : BaseActivity() {

    private val viewBinding: ActivityAppListBinding by lazy {
        ActivityAppListBinding.inflate(layoutInflater)
    }

    private val appListViewModel: AppListViewModel by viewModels()

    private val appRecommendTitleAdapter: AppRecommendTitleAdapter by lazy {
        AppRecommendTitleAdapter()
    }

    private val appRecommendListAdapter: AppRecommendListAdapter by lazy {
        AppRecommendListAdapter()
    }

    private val appListItemAdapter: AppListItemAdapter by lazy {
        AppListItemAdapter()
    }

    private val loadMoreAdapter: LoadMoreAdapter by lazy {
        LoadMoreAdapter(3).apply {
            onLoadMore = {
                appListViewModel.loadMore()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        initView()
        initData()
    }

    private fun initView() {
        with(viewBinding) {
            refreshLayout.setOnRefreshListener {
                appListViewModel.initData(true)
            }
            val concatAdapter = ConcatAdapter()
            concatAdapter.addAdapter(appRecommendTitleAdapter)
            concatAdapter.addAdapter(appRecommendListAdapter)
            concatAdapter.addAdapter(appListItemAdapter)
            concatAdapter.addAdapter(loadMoreAdapter)
            recyclerView.layoutManager = LinearLayoutManager(this@AppListActivity)
            recyclerView.itemAnimator = null
            recyclerView.adapter = concatAdapter

            searchLayout.root.setOnClickListener {
                toSearchActivity()
            }
        }
    }

    private fun toSearchActivity() {
        val intent = Intent(this, SearchLocalAppActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    private fun initData() {
        appListViewModel.mainViewStateLiveData.observe(this, ::onMainViewStateChanged)

        appListViewModel.initData(false)
    }

    private fun onMainViewStateChanged(mainViewState: MainViewState) {
        lifecycleScope.launch {
            mainViewState.refreshEvent?.getContentIfNotHandled()?.let {
                setRefreshState(it)
            }
            appRecommendTitleAdapter.titleLabel = mainViewState.appRecommendTitleState
            appRecommendListAdapter.submitList(mainViewState.appRecommendListState)
            appListItemAdapter.submitListAwaitCommitted(mainViewState.appListState)
            mainViewState.finishLoadMoreEvent?.getContentIfNotHandled()?.let {
                finishLoadMore(mainViewState.hasMoreState)
            }
            mainViewState.errorToastEvent?.getContentIfNotHandled()?.let {
                ToastUtils.showShort(it)
            }
        }
    }

    private fun setRefreshState(isRefreshing: Boolean) {
        viewBinding.refreshLayout.isRefreshing = isRefreshing
    }

    private fun finishLoadMore(hasMore: Boolean) {
        loadMoreAdapter.finishLoadMore(hasMore)
    }

}