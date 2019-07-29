package com.ckarthickit.mealdb.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewCellMaringDecoration(private val spaceHeight: Int) :
  RecyclerView.ItemDecoration() {
  override fun getItemOffsets(
    outRect: Rect,
    view: View,
    parent: RecyclerView,
    state: RecyclerView.State
  ) {
    with(outRect) {
      top = spaceHeight
      if (parent.getChildAdapterPosition(view) == 0) {
        left = spaceHeight
      }
      right = spaceHeight
      bottom = spaceHeight
    }
  }
}
