package com.ckarthickit.mealdb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ckarthickit.mealdb.model.CatalogServiceModel
import com.ckarthickit.mealdb.service.model.Area
import com.ckarthickit.mealdb.service.model.Meal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MealCatalogViewModel : androidx.lifecycle.ViewModel() {

  private val backgroundWorkScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
  private val mealService: CatalogServiceModel = CatalogServiceModel.create()
  private val _areasLiveData: MutableLiveData<Array<Area>> = MutableLiveData()
  private val _areaToRecipeMapLiveData: MutableLiveData<Map<Area, Array<Meal>>> = MutableLiveData()

  val areasLiveData: LiveData<Array<Area>>
    get() = _areasLiveData
  val areaToRecipeMapLiveData: LiveData<Map<Area, Array<Meal>>>
    get() = _areaToRecipeMapLiveData

  init {
    triggerCatalogFetch()
  }

  fun triggerCatalogFetch() {
    backgroundWorkScope.launch(Dispatchers.IO) {
      val areas = mealService.getAvailableMealAreas()
      withContext(Dispatchers.Main) {
        _areasLiveData.value = areas
        // this@MealCatalogViewModel.areas = areas
      }
      val mealListStream = produce {
        for (area in areas) {
          send(area to mealService.mealsByArea(area))
        }
      }
      withContext(Dispatchers.Main) {
        mealListStream.consumeEach {
          val (area, mealList) = it
          _areaToRecipeMapLiveData.value = mutableMapOf(area to mealList).apply {
            _areaToRecipeMapLiveData.value?.let { map ->
              this.putAll(map)
            }
          }
        }
      }
    }
  }

  override fun onCleared() {
    // cancel all
    backgroundWorkScope.cancel()
  }
}
