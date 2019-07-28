package com.ckarthickit.mealdb

import com.ckarthickit.mealdb.service.MealDBService
import com.squareup.moshi.Moshi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.moshi.MoshiConverterFactory

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation::class)
class MealDBServiceTest {
  private val mealDBService: MealDBService
  private val mockWebServer: MockWebServer = MockWebServer()

  init {
    println("===========created===========")
    mockWebServer.start()
    val moshi = Moshi.Builder().build()
    mealDBService =
      Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build().let {
          it.create(MealDBService::class.java)
        }
  }

  @AfterAll
  fun cleanUp() {
    println("=====cleanup=====")
    mockWebServer.shutdown()
  }

  @Nested
  inner class FetchLists {
    @Order(value = 1)
    @Test
    fun `get available categories`() = runBlocking {
      // given
      enqueueResponse("list_categories.json")

      // when
      val categoriesCall = mealDBService.getCategories()

      // then
      // val request: RecordedRequest = mockWebServer.takeRequest(1, TimeUnit.SECONDS)
      // println("===request is ${request.path}===")
      // assert(request.path == "/list.php?c=list")

      val categoryResponse = categoriesCall.await()
      assert(categoryResponse.categories.size == 14)
    }

    @Order(value = 2)
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
      assert(areasResponse.areas.size == 23)
      assert(areasResponse.areas[0].name == "American")
    }

    @Order(value = 3)
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

  @Nested
  inner class FilteredLists {
    @Order(value = 4)
    @Test
    fun `get meal items filtered by area - canadian`() = runBlocking {
      enqueueResponse("list_meals_area_canadian.json")
      // when
      val mealsListCall = mealDBService.mealsFilteredByArea("Canadian")

      // then
      // val request = mockWebServer.takeRequest()
      // assert(request.path == "/list.php?c=list")

      val mealsListResponse = mealsListCall.await()
      assert(mealsListResponse.meals.size == 13)
      assert(mealsListResponse.meals[0].name == "BeaverTails")
      assert(mealsListResponse.meals[1].id == 52965)
      assert(mealsListResponse.meals[3].thumbnail == "https://www.themealdb.com/images/media/meals/uttupv1511815050.jpg")
    }

    @Order(value = 5)
    @Test
    fun `get meal items filtered by category - seafood`() = runBlocking {
      enqueueResponse("list_meals_category_seafood.json")
      // when
      val mealsListCall = mealDBService.mealsFilteredByCategory("Seafood")

      // then
      // val request = mockWebServer.takeRequest()
      // assert(request.path == "/list.php?c=list")

      val mealsListResponse = mealsListCall.await()
      assert(mealsListResponse.meals.size == 19)
      assert(mealsListResponse.meals[0].name == "Baked salmon with fennel & tomatoes")
      assert(mealsListResponse.meals[1].id == 52819)
      assert(mealsListResponse.meals[3].thumbnail == "https://www.themealdb.com/images/media/meals/ysxwuq1487323065.jpg")
    }

    @Order(value = 6)
    @Test
    fun `get meal items filtered by ingredient - chicken`() = runBlocking {
      enqueueResponse("list_meals_ingredient_chicken.json")
      // when
      val mealsListCall = mealDBService.mealsFilteredByIngredient("Canadian")

      // then
      // val request = mockWebServer.takeRequest()
      // assert(request.path == "/list.php?c=list")

      val mealsListResponse = mealsListCall.await()
      assert(mealsListResponse.meals.size == 10)
      assert(mealsListResponse.meals[0].name == "Brown Stew Chicken")
      assert(mealsListResponse.meals[1].id == 52846)
      assert(mealsListResponse.meals[3].thumbnail == "https://www.themealdb.com/images/media/meals/wruvqv1511880994.jpg")
    }
  }

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
