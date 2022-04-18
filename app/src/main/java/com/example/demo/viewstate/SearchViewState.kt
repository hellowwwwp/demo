package com.example.demo.viewstate

import com.example.demo.model.search.LocalAppEntity

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
data class SearchViewState(
    val loading: Boolean = false,
    val localApps: List<LocalAppEntity>? = null,
    val emptyText: String? = null
)