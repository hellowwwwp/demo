package com.example.demo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo.R
import com.example.demo.model.app.AppItemEntity
import com.example.demo.model.app.AppRecommendListEntity
import com.example.demo.utils.ResourceUtils
import com.example.demo.utils.requireValue
import com.example.demo.utils.setOrPostValue
import com.example.demo.viewmodel.mapper.AppDataMapper
import com.example.demo.viewmodel.model.base.Event
import com.example.demo.viewmodel.repo.AppLocalRepo
import com.example.demo.viewmodel.repo.AppRemoteRepo
import com.example.demo.viewstate.MainViewState
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
class AppListViewModel : ViewModel() {

    private val appRemoteRepo: AppRemoteRepo by lazy {
        AppRemoteRepo()
    }

    private val appLocalRepo: AppLocalRepo by lazy {
        AppLocalRepo()
    }

    private val _mainViewStateLiveData: MutableLiveData<MainViewState> = MutableLiveData(MainViewState())

    val mainViewStateLiveData: LiveData<MainViewState> = _mainViewStateLiveData

    private var loadIndex: Int = LIMIT_DEFAULT

    fun initData(isRefresh: Boolean) {
        viewModelScope.launch {
            if (!isRefresh) {
                _mainViewStateLiveData.setOrPostValue {
                    it.copy(refreshEvent = Event(true))
                }
                //先读取缓存数据
                initDataFromCacheIfNeed()
            }
            loadIndex = LIMIT_DEFAULT
            val appRecommendListDataResultDeferred = async {
                appRemoteRepo.getAppRecommendListData(loadIndex)
            }
            val appListDataResultDeferred = async {
                appRemoteRepo.getAppListData(loadIndex)
            }
            val appRecommendListDataResult = appRecommendListDataResultDeferred.await()
            val appListDataResult = appListDataResultDeferred.await()
            if (appRecommendListDataResult.isFailure || appListDataResult.isFailure) {
                _mainViewStateLiveData.setOrPostValue {
                    it.copy(
                        refreshEvent = Event(false),
                        errorToastEvent = Event("system error"),
                    )
                }
                return@launch
            }
            //推荐数据
            val appRecommendDataList = appRecommendListDataResult.getOrNull()?.items
            //先删除本地缓存数据
            appLocalRepo.deleteAllAppRecommendListItems()
            //插入缓存数据(只缓存第一页)
            appLocalRepo.insertAppRecommendListItems(appRecommendDataList)
            val appRecommendEntityList = AppDataMapper.appItemData2AppItemEntity(appRecommendDataList)
            val appRecommendTitle: String?
            val appRecommendListDataList: List<AppRecommendListEntity>?
            if (appRecommendEntityList.isNullOrEmpty()) {
                appRecommendTitle = null
                appRecommendListDataList = null
            } else {
                appRecommendTitle = ResourceUtils.getString(resId = R.string.app_recommend_title)
                appRecommendListDataList = listOf(AppRecommendListEntity(appRecommendEntityList))
            }
            //列表数据
            val appDataList = appListDataResult.getOrNull()?.items
            //查询评分信息
            appRemoteRepo.queryRatingDetail(appDataList)
            //先删除本地缓存数据
            appLocalRepo.deleteAllAppListItems()
            //插入缓存数据(只缓存第一页)
            appLocalRepo.insertAppListItems(appDataList)
            val appEntityList = AppDataMapper.appItemData2AppItemEntity(appDataList)
            val hasMore = appEntityList?.size == LIMIT_DEFAULT
            _mainViewStateLiveData.setOrPostValue {
                it.copy(
                    appRecommendTitleState = appRecommendTitle,
                    appRecommendListState = appRecommendListDataList,
                    appListState = appEntityList,
                    refreshEvent = Event(false),
                    hasMoreState = hasMore,
                    finishLoadMoreEvent = Event(Unit),
                )
            }
        }
    }

    private suspend fun initDataFromCacheIfNeed() {
        val cacheAppRecommendBeanList = appLocalRepo.getAllAppRecommendListItems()
        val cacheAppBeanList = appLocalRepo.getAllAppListItems()
        val cacheAppRecommendEntityList =
            AppDataMapper.appRecommendListItemBean2AppItemEntity(cacheAppRecommendBeanList)
        val cacheAppEntityList = AppDataMapper.appListItemBean2AppItemEntity(cacheAppBeanList)
        if (!cacheAppRecommendEntityList.isNullOrEmpty() && !cacheAppEntityList.isNullOrEmpty()) {
            //缓存有效就先显示缓存
            _mainViewStateLiveData.setOrPostValue {
                it.copy(
                    appRecommendTitleState = ResourceUtils.getString(resId = R.string.app_recommend_title),
                    appRecommendListState = listOf(AppRecommendListEntity(cacheAppRecommendEntityList)),
                    appListState = cacheAppEntityList,
                    refreshEvent = Event(false),
                    hasMoreState = false,
                    finishLoadMoreEvent = Event(Unit),
                )
            }
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            loadIndex += LIMIT_DEFAULT
            val appListDataResult = appRemoteRepo.getAppListData(loadIndex)
            if (appListDataResult.isFailure) {
                _mainViewStateLiveData.setOrPostValue {
                    it.copy(
                        hasMoreState = true,
                        finishLoadMoreEvent = Event(Unit),
                        errorToastEvent = Event("system error"),
                    )
                }
                return@launch
            }
            val appDataList = appListDataResult.getOrNull()?.items
            //查询评分信息
            appRemoteRepo.queryRatingDetail(appDataList)
            val appEntityList = AppDataMapper.appItemData2AppItemEntity(appDataList)
            if (appEntityList.isNullOrEmpty()) {
                _mainViewStateLiveData.setOrPostValue {
                    it.copy(
                        hasMoreState = false,
                        finishLoadMoreEvent = Event(Unit)
                    )
                }
                return@launch
            }
            val oldViewState = _mainViewStateLiveData.requireValue()
            val newAppListState = mutableListOf<AppItemEntity>()
            //先将之前的数据添加到新的集合
            oldViewState.appListState?.let {
                newAppListState.addAll(it)
            }
            //再将新的数据添加到新的集合
            newAppListState.addAll(appEntityList)
            val hasMore = appEntityList.size == LIMIT_DEFAULT && loadIndex < LIMIT_MAX
            _mainViewStateLiveData.setOrPostValue {
                it.copy(
                    appListState = newAppListState,
                    hasMoreState = hasMore,
                    finishLoadMoreEvent = Event(Unit),
                )
            }
        }
    }

    companion object {
        const val LIMIT_DEFAULT = 10
        const val LIMIT_MAX = 100
    }

}