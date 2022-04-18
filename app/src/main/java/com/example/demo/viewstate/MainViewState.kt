package com.example.demo.viewstate

import com.example.demo.model.app.AppItemEntity
import com.example.demo.model.app.AppRecommendListEntity
import com.example.demo.viewmodel.model.base.Event

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
data class MainViewState(
    val appRecommendTitleState: String? = null,
    val appRecommendListState: List<AppRecommendListEntity>? = null,
    val appListState: List<AppItemEntity>? = null,
    val refreshEvent: Event<Boolean>? = null,
    val hasMoreState: Boolean = false,
    val finishLoadMoreEvent: Event<Unit>? = null,
    val errorToastEvent: Event<String>? = null,
)