package com.ckarthickit.mealdb.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ckarthickit.mealdb.R
import com.ckarthickit.mealdb.viewmodel.MealCatalogViewModel

class MealCatalogActivity : AppCompatActivity() {

  private lateinit var catalogViewModel: MealCatalogViewModel
  private lateinit var catalogRecycler: RecyclerView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_catalog)
    setSupportActionBar(findViewById(R.id.toolbar))
    initViews()
    setupViewModel()
  }

  private fun setupViewModel() {
    catalogViewModel = ViewModelProviders.of(this)[MealCatalogViewModel::class.java]

    catalogViewModel.areasLiveData.observe(this, Observer {
      setupAdapter()
    })
    catalogViewModel.areaToRecipeMapLiveData.observe(this, Observer {
      setupAdapter(true)
    })
  }

  private fun initViews() {
    catalogRecycler = findViewById(R.id.catalog_recycler)
  }

  private fun setupAdapter(areasToRecepieChanged: Boolean = false) {
    catalogRecycler.run {
      val mLayoutManager = LinearLayoutManager(this@MealCatalogActivity)
      layoutManager = mLayoutManager
      isNestedScrollingEnabled = false
      setHasFixedSize(true)
      catalogViewModel.areasLiveData.value?.let {
        if (areasToRecepieChanged) {
          val items = catalogViewModel.areaToRecipeMapLiveData.value ?: emptyMap()
          (this.adapter as? MealCatalogAdapter)?.updateItems(items)
        } else {
          swapAdapter(
            MealCatalogAdapter(
              it,
              catalogViewModel.areaToRecipeMapLiveData.value ?: emptyMap()
            ), false
          )
        }
      }
    }
  }
}
