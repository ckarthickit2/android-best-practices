package com.ckarthickit.mealdb

import com.ckarthickit.mealdb.service.MealDBService
import com.squareup.moshi.Moshi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.moshi.MoshiConverterFactory

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MealDBServiceTest {
  private val mealDBService: MealDBService
  private val mockWebServer: MockWebServer = MockWebServer()

  init {
    println("===========created===========")
    val moshi = Moshi.Builder().build()
    mealDBService =
      Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build().let {
          it.create(MealDBService::class.java)
        }
  }

  @Nested
  inner class FetchLists {
    @Test
    fun `get available categories`() = runBlocking {
      // given
      enqueueResponse("list_categories.json")

      // when
      val categoriesCall = mealDBService.getCategories()

      // then
      // val request = mockWebServer.takeRequest()
      // assert(request.path == "/list.php?c=list")

      val categoryResponse = categoriesCall.await()
      println("categories = $categoryResponse")
      assert(categoryResponse.categories.size == 14)
    }

    @Test
    fun `get available areas`() = runBlocking {
      // given
      enqueueResponse("list_areas.json")

      // when
      val areasCall = mealDBService.getAreas()

      // then
      // val request = mockWebServer.takeRequest()
      // assert(request.path == "/list.php?c=list")

      val areasResponse = areasCall.await()
      println("ingredients = $areasResponse")
      assert(areasResponse.areas.size == 23)
      println("firstAres = ${areasResponse.areas[0].name}")
      assert(areasResponse.areas[0].name == "American")
    }

    @Test
    fun `get available ingredients`() = runBlocking {
      // given
      enqueueResponse("list_ingredients.json")

      // when
      val ingredientsCall = mealDBService.getAvailableIngredients()

      // then
      // val request = mockWebServer.takeRequest()
      // assert(request.path == "/list.php?c=list")

      val ingredientsResponse = ingredientsCall.await()
      assert(ingredientsResponse.ingredients.size == 529)
    }
  }

  inner class Fetc

  private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
    val inputStream = javaClass.classLoader
      .getResourceAsStream("mealdb-api-response/$fileName")
    val source = Okio.buffer(Okio.source(inputStream))
    val mockResponse = MockResponse()
    for ((key, value) in headers) {
      mockResponse.addHeader(key, value)
    }
    mockWebServer.enqueue(
      mockResponse
        .setBody(source.readString(Charsets.UTF_8))
    )
  }
}
