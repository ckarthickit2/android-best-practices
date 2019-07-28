package com.ckarthickit.mealdb.service

import com.ckarthickit.mealdb.service.model.MealsAreaResponse
import com.ckarthickit.mealdb.service.model.MealsCategoryResponse
import com.ckarthickit.mealdb.service.model.MealsIngredientsResponse
import com.squareup.moshi.Moshi
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface MealDBService {

  companion object {
    private var _moshi: Moshi? = null

    val moshi: Moshi
      get() {
        if (_moshi == null) {
          synchronized(MealDBService::class.java) {
            if (_moshi == null) {
              _moshi = Moshi.Builder().build()
            }
          }
        }
        return _moshi as Moshi
      }

    fun create(): MealDBService {
      val moshi = Moshi.Builder().build()
      return Retrofit.Builder()
        .baseUrl("https://www.themealdb.com/api/json/v1/1/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build().let {
          it.create(MealDBService::class.java)
        }
    }
  }

  @GET(value = "/list.php?c=list")
  fun getCategories(): Call<MealsCategoryResponse>

  @GET(value = "/list.php?a=list")
  fun getAreas(): Call<MealsAreaResponse>

  @GET(value = "/list.php?i=list")
  fun getAvailableIngredients(): Call<MealsIngredientsResponse>
}
