package com.example.demo.viewmodel

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.AppUtils
import com.example.demo.R
import com.example.demo.model.search.LocalAppEntity
import com.example.demo.utils.ResourceUtils
import com.example.demo.utils.requireValue
import com.example.demo.utils.setOrPostValue
import com.example.demo.viewstate.SearchViewState
import kotlinx.coroutines.*

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
class SearchViewModel : ViewModel() {

    private val _searchViewStateLiveData: MutableLiveData<SearchViewState> = MutableLiveData(SearchViewState())

    val searchViewStateLiveData: LiveData<SearchViewState> = _searchViewStateLiveData

    private var searchJob: Job? = null

    private fun cancelSearchJobIfNeed() {
        searchJob?.apply {
            cancel(null)
            searchJob = null
        }
    }

    fun search(keyword: String, delay: Long = 0L) {
        cancelSearchJobIfNeed()
        viewModelScope.launch {
            delay(delay)
            val oldData = _searchViewStateLiveData.requireValue()
            if (oldData.localApps.isNullOrEmpty()) {
                _searchViewStateLiveData.setOrPostValue {
                    it.copy(loading = true)
                }
            }
            val localApps = withContext(Dispatchers.IO) {
                getAllLocalApps(keyword)
            }
            val emptyText = if (localApps.isNullOrEmpty()) {
                ResourceUtils.getString(resId = R.string.search_empty_text)
            } else {
                null
            }
            _searchViewStateLiveData.setOrPostValue {
                it.copy(
                    loading = false,
                    localApps = localApps,
                    emptyText = emptyText
                )
            }
        }.also {
            searchJob = it
        }
    }

    @WorkerThread
    fun getAllLocalApps(keyword: String? = null): List<LocalAppEntity> {
        return AppUtils.getAppsInfo()
            .map {
                LocalAppEntity(it.packageName, it.name, it.icon)
            }.filter {
                if (keyword.isNullOrEmpty()) {
                    true
                } else {
                    it.appName.contains(keyword) || it.packageName.contains(keyword)
                }
            }
    }

}