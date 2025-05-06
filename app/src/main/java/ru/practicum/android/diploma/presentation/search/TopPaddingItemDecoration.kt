package ru.practicum.android.diploma.presentation.search

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class TopPaddingItemDecoration(private val topPadding: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.getChildAdapterPosition(view) == 0) { // Только для первого элемента
            outRect.top = topPadding
        }
    }
}
