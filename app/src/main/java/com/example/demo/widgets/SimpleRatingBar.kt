package com.example.demo.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import com.blankj.utilcode.util.SizeUtils
import com.example.demo.R
import kotlin.math.ceil

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2022/7/8
 */
class SimpleRatingBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var rating: Float = 0f

    private var starSize: Int = 0

    init {
        orientation = HORIZONTAL
        val ta = context.obtainStyledAttributes(attrs, R.styleable.SimpleRatingBar)
        rating = ta.getFloat(R.styleable.SimpleRatingBar_srb_rating, 0f)
        starSize = ta.getDimensionPixelSize(R.styleable.SimpleRatingBar_srb_star_size, SizeUtils.dp2px(4f))
        ta.recycle()
        updateRatingView()
    }

    fun setRating(rating: Float) {
        this.rating = when {
            rating < 0 -> 0f
            rating > 5 -> 5f
            else -> rating
        }
        updateRatingView()
    }

    fun setStarSize(size: Int) {
        this.starSize = size
        updateRatingView()
    }

    private fun updateRatingView() {
        val fullStarCount = rating.toInt()
        val halfStarCount = ceil(rating - fullStarCount).toInt()
        val normalStarCount = 5 - fullStarCount - halfStarCount
        removeAllViews()
        repeat(fullStarCount) {
            addView(createStarImageView(R.drawable.star_full))
        }
        repeat(halfStarCount) {
            addView(createStarImageView(R.drawable.star_half))
        }
        repeat(normalStarCount) {
            addView(createStarImageView(R.drawable.star_normal))
        }
    }

    private fun createStarImageView(resId: Int): ImageView {
        return ImageView(context).apply {
            setImageResource(resId)
            layoutParams = LayoutParams(starSize, starSize)
            scaleType = ImageView.ScaleType.FIT_CENTER
        }
    }

}