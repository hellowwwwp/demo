package com.example.demo.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.FrameLayout
import com.example.demo.R
import kotlin.math.abs

/**
 * @author: wangpan
 * @emial: p.wang@aftership.com
 * @date: 2020/9/28
 *
 * 当出现滑动冲突又不太方便重写控件的时候(比如: ViewPager2就不能被重写)
 * 使用这个类包裹内部View,并且设置需要滑动的方向,即可解决
 * 比如:NestedScrollView嵌套ViewPager2的时候,默认ViewPager2会很难左右滑动.
 * 可以在ViewPager2外面包一层FixedTouchLayout并设置orientation为HORIZONTAL
 */
@Suppress("unused")
class FixedTouchLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var downX: Float = 0f
    private var downY: Float = 0f
    private var isDragged = false

    private var orientation = Orientation.HORIZONTAL

    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.FixedTouchLayout)
        orientation = if (ta.getInt(R.styleable.FixedTouchLayout_ftl_orientation, 0) == 0) {
            Orientation.HORIZONTAL
        } else {
            Orientation.VERTICAL
        }
        ta.recycle()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.x
                downY = ev.y
                isDragged = false
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isDragged) {
                    val dx = abs(ev.x - downX)
                    val dy = abs(ev.y - downY)
                    isDragged = when (orientation) {
                        Orientation.HORIZONTAL -> {
                            dx > touchSlop && dx > dy
                        }
                        Orientation.VERTICAL -> {
                            dy > touchSlop && dy > dx
                        }
                    }
                }
                parent.requestDisallowInterceptTouchEvent(isDragged)
            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                isDragged = false
                parent.requestDisallowInterceptTouchEvent(false)
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    enum class Orientation {
        HORIZONTAL, VERTICAL
    }

}