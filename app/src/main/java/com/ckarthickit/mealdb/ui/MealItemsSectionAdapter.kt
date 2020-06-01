package com.ckarthickit.mealdb.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.ckarthickit.mealdb.R
import com.ckarthickit.mealdb.service.model.Meal

class MealItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
  internal val imageView: ImageView = view.findViewById(R.id.card_image)
  internal val label: AppCompatTextView = view.findViewById(R.id.card_label)
  internal val subLabel: AppCompatTextView = view.findViewById(R.id.card_sub_label)
}

class MealItemsSectionAdapter : RecyclerView.Adapter<MealItemViewHolder>() {
  private var mealItems: List<Meal> = emptyList()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealItemViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view, parent, false)
    return MealItemViewHolder(view)
  }

  override fun getItemCount(): Int {
    return mealItems.size
  }

  fun updateItems(mealItems: List<Meal>) {
    this.mealItems = mealItems
    notifyDataSetChanged()
  }

  override fun onBindViewHolder(holder: MealItemViewHolder, position: Int) {
    val mealItem = mealItems[position]
    holder.label.text = mealItem.name
    holder.subLabel.text = mealItem.id.toString()
    val crossFadeFactory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
    Glide
      .with(holder.itemView.context)
      .load(mealItem.thumbnail)
      .diskCacheStrategy(DiskCacheStrategy.ALL)
      .placeholder(R.drawable.black_gradient_top_down)
      .error(R.drawable.black_gradient_top_down)
      .transition(DrawableTransitionOptions.with(crossFadeFactory))
//      .animate(R.anim.fade_in)
      .into(holder.imageView)
  }
}
