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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MealCatalogViewModel : androidx.lifecycle.ViewModel() {

  private val backgroundWorkScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
  private val mealService: CatalogServiceModel = CatalogServiceModel.create()
  private val _areaToRecipeLiveData: MutableLiveData<List<Pair<Area, List<Meal>>>> = MutableLiveData()

  val areaToRecipeMapLiveData: LiveData<List<Pair<Area, List<Meal>>>>
    get() = _areaToRecipeLiveData

  init {
    _areaToRecipeLiveData.value = emptyList()
    triggerCatalogFetch()
  }

  private fun triggerCatalogFetch() {
    backgroundWorkScope.launch(Dispatchers.IO) {
      val areas = mealService.getAvailableMealAreas()
      withContext(Dispatchers.Main) {
//       _areaToRecipeLiveData.value = areas.map { it to emptyList<Meal>() }
      }
      val mealListStream = flow<Pair<Area, List<Meal>>> {
        for (area in areas) {
          emit(area to mealService.mealsByArea(area).toList())
        }
      }
      withContext(Dispatchers.Main) {
        mealListStream.collect {
          val oldAreaToRecipeLiveData: List<Pair<Area, List<Meal>>> = _areaToRecipeLiveData.value ?: emptyList()
          _areaToRecipeLiveData.value = oldAreaToRecipeLiveData.toMutableList().apply { add(it) }
        }
      }
    }
  }

  override fun onCleared() {
    // cancel all
    backgroundWorkScope.cancel()
  }

  fun emptyFun(){

  }
}
