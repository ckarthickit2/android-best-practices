package com.ckarthickit.mealdb.model

import com.ckarthickit.mealdb.service.MealDBService
import com.ckarthickit.mealdb.service.model.Area
import com.ckarthickit.mealdb.service.model.Category
import com.ckarthickit.mealdb.service.model.Ingredient
import com.ckarthickit.mealdb.service.model.Meal
import retrofit2.await

class CatalogServiceModel private constructor() {
  private val mealDBService: MealDBService = MealDBService.create()

  companion object Factory {
    fun create(): CatalogServiceModel {
      return CatalogServiceModel()
    }
  }

  suspend fun getAvailableMealCategories(): Array<Category> {
    val mealsCategoryResp = mealDBService.getCategories().await()
    return mealsCategoryResp.categories
  }

  suspend fun getAvailableMealAreas(): Array<Area> {
    val mealsAreaResponse = mealDBService.getAreas().await()
    return mealsAreaResponse.areas
  }

  suspend fun getAvailableMealIngredients(): Array<Ingredient> {
    val mealsIngredientsResponse = mealDBService.getAvailableIngredients().await()
    return mealsIngredientsResponse.ingredients
  }

  suspend fun mealsByArea(area: Area): Array<Meal> {
    val mealsListResp = mealDBService.mealsFilteredByArea(area.name).await()
    return mealsListResp.meals
  }

  suspend fun mealsByCategory(category: Category): Array<Meal> {
    val mealsListResp = mealDBService.mealsFilteredByCategory(category.name).await()
    return mealsListResp.meals
  }

  suspend fun mealsByIngredient(ingredient: Ingredient): Array<Meal> {
    val mealsListResp = mealDBService.mealsFilteredByIngredient(ingredient.name).await()
    return mealsListResp.meals
  }
}
