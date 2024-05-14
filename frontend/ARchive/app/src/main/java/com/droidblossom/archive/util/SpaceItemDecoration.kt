package com.droidblossom.archive.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(
    private val spaceLeft: Int = 0,
    private val spaceRight: Int = 0,
    private val spaceTop: Int = 0,
    private val spaceBottom: Int = 0
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = spaceLeft
        outRect.right = spaceRight
        outRect.top = spaceTop
        outRect.bottom = spaceBottom
    }
}