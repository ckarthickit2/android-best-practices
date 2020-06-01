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
    catalogViewModel.areaToRecipeMapLiveData.observe(this, Observer {
      updateAdapter()
    })
  }

  private fun initViews() {
    catalogRecycler = findViewById(R.id.catalog_recycler)
    val mLayoutManager = LinearLayoutManager(this@MealCatalogActivity)
    catalogRecycler.layoutManager = mLayoutManager
    catalogRecycler.isNestedScrollingEnabled = false
    catalogRecycler.setHasFixedSize(true)
    catalogRecycler.adapter = MealCatalogAdapter()
  }

  private fun updateAdapter() {
    (catalogRecycler.adapter as MealCatalogAdapter)
      .updateItems(catalogViewModel.areaToRecipeMapLiveData.value ?: emptyList())
  }
}
