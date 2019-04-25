package com.alidevs.instaapp.utils

import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewConfiguration

/**
 * Doesn't care about which view is clicked, so should only be used for a single View.
 *
 * Modified by Filippo Beraldo on 02/02/17:
 * - Added the ability to pass the View v when the click event occurs.
 */
abstract class OnDoubleClickListener : View.OnClickListener {
    private val doubleClickTimeout: Int
    private val handler: Handler

    private var firstClickTime: Long = 0

    init {
        doubleClickTimeout = ViewConfiguration.getDoubleTapTimeout()
        firstClickTime = 0L
        handler = Handler(Looper.getMainLooper())
    }

    override fun onClick(v: View) {
        val now = System.currentTimeMillis()

        if (now - firstClickTime < doubleClickTimeout) {
            handler.removeCallbacksAndMessages(null)
            firstClickTime = 0L
            onDoubleClick(v)
        } else {
            firstClickTime = now
            handler.postDelayed({
                onSingleClick(v)
                firstClickTime = 0L
            }, doubleClickTimeout.toLong())
        }
    }

    abstract fun onDoubleClick(v: View)

    abstract fun onSingleClick(v: View)

    fun reset() {
        handler.removeCallbacksAndMessages(null)
    }
}