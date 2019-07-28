package com.ckarthickit.mealdb.service

import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

interface MealDBService {

  companion object {
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
}
