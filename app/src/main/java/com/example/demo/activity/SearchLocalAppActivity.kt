package com.example.demo.activity

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.KeyboardUtils
import com.example.demo.adapter.search.LocalAppListAdapter
import com.example.demo.databinding.ActivitySearchLocalAppBinding
import com.example.demo.utils.SimpleTextWatcher
import com.example.demo.viewmodel.SearchViewModel
import com.example.demo.viewstate.SearchViewState

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
class SearchLocalAppActivity : BaseActivity() {

    private val viewBinding: ActivitySearchLocalAppBinding by lazy {
        ActivitySearchLocalAppBinding.inflate(layoutInflater)
    }

    private val searchInputEt: EditText
        get() = viewBinding.searchLayout.searchInputEt

    private val searchClearView: View
        get() = viewBinding.searchLayout.searchInputClearFl

    private val searchViewMode: SearchViewModel by viewModels()

    private val localAppListAdapter: LocalAppListAdapter by lazy {
        LocalAppListAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        initView()
        initData()
    }

    private fun initView() {
        with(viewBinding.recyclerView) {
            layoutManager = LinearLayoutManager(this@SearchLocalAppActivity)
            itemAnimator = null
            adapter = localAppListAdapter
            //滑动列表时自动关闭软键盘
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING && searchInputEt.hasFocus()) {
                        KeyboardUtils.hideSoftInput(searchInputEt)
                    }
                }
            })
        }

        searchInputEt.post {
            KeyboardUtils.showSoftInput(searchInputEt)
        }
        searchClearView.setOnClickListener {
            searchInputEt.setText("")
            searchInputEt.setSelection(0)
        }
        searchInputEt.addTextChangedListener(object : SimpleTextWatcher() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                searchClearView.isVisible = s.isNotEmpty()
                searchViewMode.search(s.toString(), 500L)
            }
        })
    }

    private fun initData() {
        searchViewMode.searchViewStateLiveData.observe(this, ::onSearchViewStateChanged)
        searchViewMode.search("")
    }

    private fun onSearchViewStateChanged(viewState: SearchViewState) {
        if (viewState.loading) {
            showLoadingVisible()
        } else {
            if (viewState.localApps.isNullOrEmpty()) {
                showEmptyTextVisible(viewState.emptyText)
            } else {
                showContentLayout()
                localAppListAdapter.submitList(viewState.localApps)
            }
        }
    }

    private fun showContentLayout() {
        with(viewBinding.stateLayout) {
            root.isVisible = false
            loadingPb.isVisible = false
            emptyTv.isVisible = false
        }
    }

    private fun showLoadingVisible() {
        with(viewBinding.stateLayout) {
            root.isVisible = true
            loadingPb.isVisible = true
            emptyTv.isVisible = false
        }
    }

    private fun showEmptyTextVisible(emptyText: String?) {
        with(viewBinding.stateLayout) {
            root.isVisible = true
            loadingPb.isVisible = false
            emptyTv.isVisible = true
            emptyTv.text = emptyText ?: ""
        }
    }

    override fun onPause() {
        super.onPause()
        KeyboardUtils.hideSoftInput(searchInputEt)
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(0, 0)
    }

}