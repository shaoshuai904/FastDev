package com.maple.fastdev.utils

import android.os.SystemClock
import android.view.View

/**
 * View 避免频繁点击
 *
 * @author : shaoshuai27
 * @date ：2020/7/27
 */
inline fun View.setOnSingleClickListener(crossinline action: () -> Unit) {
    var lastClick = 0L
    setOnClickListener {
        val gap = System.currentTimeMillis() - lastClick
        if (gap > 1500) {
            lastClick = System.currentTimeMillis()
            action.invoke()
        }
    }
}

/**
 * 限定时间内 多次点击
 */
inline fun View.setOnMultiClickListener(
        hits: Int = 5, // 5次点击
        mDuration: Long = 3000, // 限定时间
        crossinline action: () -> Unit
) {
    val mHits = LongArray(hits) // 点击的时间数组
    setOnClickListener {
        System.arraycopy(mHits, 1, mHits, 0, mHits.size - 1)
        mHits[mHits.size - 1] = SystemClock.uptimeMillis() //System.currentTimeMillis()
        if (mHits[mHits.size - 1] - mHits[0] <= mDuration) {
            action.invoke()
        }
    }
}