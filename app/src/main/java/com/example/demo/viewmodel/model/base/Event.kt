package com.example.demo.viewmodel.model.base

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 *
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
open class Event<T>(private var content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content

    fun replaceContent(newContent: T): T {
        content = newContent
        return newContent
    }
}