package com.alidevs.instaapp.utils

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView

class SnapHelperOneByOne : LinearSnapHelper() {

    override fun findTargetSnapPosition(
        layoutManager: RecyclerView.LayoutManager?,
        velocityX: Int,
        velocityY: Int
    ): Int {
        if (!(layoutManager is RecyclerView.SmoothScroller.ScrollVectorProvider)) {
            return RecyclerView.NO_POSITION
        }

        val currentView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION;

        val myLayoutManager: LinearLayoutManager = layoutManager as LinearLayoutManager

        val position1 = myLayoutManager.findFirstVisibleItemPosition()
        val position2 = myLayoutManager.findLastVisibleItemPosition()

        var currentPosition = layoutManager.getPosition(currentView)

        if (velocityX > 50) {
            currentPosition = position2
        } else if (velocityX < 50) {
            currentPosition = position1
        }

        if (currentPosition == RecyclerView.NO_POSITION) {
            return RecyclerView.NO_POSITION
        }

        return currentPosition
    }
}