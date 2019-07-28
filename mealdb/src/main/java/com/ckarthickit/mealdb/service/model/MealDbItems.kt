package com.ckarthickit.mealdb.service.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

sealed class MealDbItem

@JsonClass(generateAdapter = true)
data class Category(
  @Json(name = "strCategory") val name: String
) : MealDbItem()

@JsonClass(generateAdapter = true)
data class Area(
  @Json(name = "strArea") val name: String
) : MealDbItem()

@JsonClass(generateAdapter = true)
data class Ingredient(
  @Json(name = "idIngredient") val id: Int,
  @Json(name = "strIngredient") val name: String,
  @Json(name = "strDescription") val description: String? = "",
  @Json(name = "strType") val type: String? = ""
) : MealDbItem()

@JsonClass(generateAdapter = true)
data class Meal(
  @Json(name = "idMeal") val id: Int,
  @Json(name = "strMeal") val name: String,
  @Json(name = "strMealThumb") val thumbnail: String
) : MealDbItem()
