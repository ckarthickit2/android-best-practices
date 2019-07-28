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

class MealCatalogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  internal val sectionRecycler: RecyclerView = itemView.findViewById(R.id.section_recycler)
  internal val sectionLabel: AppCompatTextView = itemView.findViewById(R.id.section_name)
}

class MealCatalogAdapter(private val areas: Array<Area>, items: Map<Area, Array<Meal>>) :
  RecyclerView.Adapter<MealCatalogViewHolder>() {
  private var items: Map<Area, Array<Meal>> = items
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealCatalogViewHolder {
    val itemView = LayoutInflater.from(parent.context).inflate(R.layout.horizontal_scroll_section, parent, false)
    return MealCatalogViewHolder(itemView)
  }

  override fun getItemCount(): Int {
    return items.size
  }

  override fun onBindViewHolder(holder: MealCatalogViewHolder, position: Int) {
    val area = areas[position]
    holder.sectionLabel.text = area.name
    val mealItems: Array<Meal>? = items[area]
    mealItems?.takeIf { items.isNotEmpty() }?.let {
      holder.sectionRecycler.run {
        val mLayoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
        layoutManager = mLayoutManager
        isNestedScrollingEnabled = false
        setHasFixedSize(true)
        adapter = MealItemsSectionAdapter(mealItems)
      }
    }
  }
}
