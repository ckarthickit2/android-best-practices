package com.ckarthickit.mealdb.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ckarthickit.mealdb.R
import com.ckarthickit.mealdb.service.model.Area
import com.ckarthickit.mealdb.service.model.Meal
import kotlinx.android.synthetic.main.horizontal_scroll_section.view.*

class MealCatalogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  internal val sectionRecycler: RecyclerView = itemView.section_recycler
  internal val sectionLabel: AppCompatTextView = itemView.section_name

  init {
    sectionRecycler.run {
      val mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
      this.layoutManager = mLayoutManager
      this.isNestedScrollingEnabled = false
      this.setHasFixedSize(true)
      if (itemDecorationCount == 0) {
        val itemPadding = resources.getDimensionPixelSize(R.dimen.default_padding)
        addItemDecoration(RecyclerViewCellMaringDecoration(itemPadding))
      }
      adapter = MealItemsSectionAdapter()
    }
  }
}

class MealCatalogAdapter : RecyclerView.Adapter<MealCatalogViewHolder>() {

  private var areaToMeal: List<Pair<Area, List<Meal>>> = emptyList()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealCatalogViewHolder {
    val itemView = LayoutInflater.from(parent.context).inflate(R.layout.horizontal_scroll_section, parent, false)
    return MealCatalogViewHolder(itemView)
  }

  override fun getItemCount(): Int {
    return areaToMeal.size
  }

  fun updateItems(items: List<Pair<Area, List<Meal>>>) {
    this.areaToMeal = items
  }

  override fun onBindViewHolder(holder: MealCatalogViewHolder, position: Int) {
    val (area, mealItems) = areaToMeal[position]
    holder.sectionLabel.text = area.name
    if (mealItems.isNotEmpty()) {
      (holder.sectionRecycler.adapter as MealItemsSectionAdapter).updateItems(mealItems)
    }
  }
}
