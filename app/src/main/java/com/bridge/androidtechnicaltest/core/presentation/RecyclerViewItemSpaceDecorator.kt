package com.bridge.androidtechnicaltest.core.presentation

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewItemSpaceDecorator(private val verticalSpacing: Int? = null, private val horizontalSpacing: Int? = null) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemPos = parent.getChildAdapterPosition(view)
        if (itemPos == 0)
            return

        verticalSpacing?.let { outRect.top = verticalSpacing }

        horizontalSpacing?.let { outRect.left = horizontalSpacing }
    }
}
