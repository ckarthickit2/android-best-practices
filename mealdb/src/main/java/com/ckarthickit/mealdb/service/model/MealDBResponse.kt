package com.ckarthickit.mealdb.service.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

internal sealed class MealDBResponse<T>(val items: Array<T>) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other.javaClass) return false

    other as MealDBResponse<T>

    if (!items.contentEquals(other.items)) return false

    return true
  }

  override fun hashCode(): Int {
    return items.contentHashCode()
  }
}

// fun <T> MealDBResponse<T>.equals(other: Any?): Boolean {
//  if (this === other) return true
//  if (javaClass != other?.javaClass) return false
//
//  other as MealDBResponse<T>
//
//  if (!items.contentEquals(other.items)) return false
//
//  return true
// }

@JsonClass(generateAdapter = true)
internal data class MealsCategoryResponse(
  @Json(name = "meals") val categories: Array<Category>
) : MealDBResponse<Category>(categories) {
  override fun equals(other: Any?): Boolean {
    return super.equals(other)
  }

  override fun hashCode(): Int {
    return super.hashCode()
  }
}

@JsonClass(generateAdapter = true)
internal data class MealsAreaResponse(@Json(name = "meals") val areas: Array<Area>) : MealDBResponse<Area>(areas) {
  override fun equals(other: Any?): Boolean {
    return super.equals(other)
  }

  override fun hashCode(): Int {
    return super.hashCode()
  }
}

@JsonClass(generateAdapter = true)
internal data class MealsIngredientsResponse(
  @Json(name = "meals") val ingredients: Array<Ingredient>
) : MealDBResponse<Ingredient>(ingredients) {
  override fun equals(other: Any?): Boolean {
    return super.equals(other)
  }

  override fun hashCode(): Int {
    return super.hashCode()
  }
}

@JsonClass(generateAdapter = true)
internal data class MealsListResponse(@Json(name = "meals") val meals: Array<Meal>) : MealDBResponse<Meal>(meals) {
  override fun equals(other: Any?): Boolean {
    return super.equals(other)
  }

  override fun hashCode(): Int {
    return super.hashCode()
  }
}
