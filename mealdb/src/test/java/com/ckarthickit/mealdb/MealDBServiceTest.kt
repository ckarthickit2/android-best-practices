package com.ckarthickit.mealdb

import com.ckarthickit.mealdb.service.MealDBService
import com.squareup.moshi.Moshi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okio.Okio
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
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
  inner class FetchFacetsResponseTest {
    @Order(value = 1)
    @Test
    fun `get available categories`() = runBlocking {
      // given
      enqueueResponse("list_categories.json")

      // when
      val categoriesCall = mealDBService.getCategories()
      val categoryResponse = categoriesCall.await()

      // then
      assert(categoryResponse.categories.size == 14)
    }

    @Order(value = 2)
    @Test
    fun `get available areas`() = runBlocking {
      // given
      enqueueResponse("list_areas.json")

      // when
      val areasCall = mealDBService.getAreas()
      val areasResponse = areasCall.await()

      // then
      assert(areasResponse.areas.size == 23)
      assert(areasResponse.areas[0].name == "American")
    }

    @Order(value = 3)
    @Test
    @DisplayName(value = "get available ingredients")
    fun `get available ingredients`() = runBlocking {
      // given
      enqueueResponse("list_ingredients.json")

      // when
      val ingredientsCall = mealDBService.getAvailableIngredients()
      val ingredientsResponse = ingredientsCall.await()

      // then
      assert(ingredientsResponse.ingredients.size == 529)
    }
  }

  @Nested
  inner class FetchFilteredMealResponseTest {

    @Order(value = 4)
    @Test
    fun `get meal items filtered by area - canadian`() = runBlocking {
      enqueueResponse("list_meals_area_canadian.json")
      // when
      val mealsListCall = mealDBService.mealsFilteredByArea("Canadian")
      val mealsListResponse = mealsListCall.await()

      // then
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
      val mealsListResponse = mealsListCall.await()

      // then
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
      val mealsListCall = mealDBService.mealsFilteredByIngredient("Smoked Salmon")
      val mealsListResponse = mealsListCall.await()

      // then
      assert(mealsListResponse.meals.size == 10)
      assert(mealsListResponse.meals[0].name == "Brown Stew Chicken")
      assert(mealsListResponse.meals[1].id == 52846)
      assert(mealsListResponse.meals[3].thumbnail == "https://www.themealdb.com/images/media/meals/wruvqv1511880994.jpg")
    }
  }

  @Nested
  inner class RequestTest {

    private lateinit var mockServer: MockWebServer
    private lateinit var mealsService: MealDBService

    @BeforeEach
    private fun beforeEach() {
      mockServer = MockWebServer()
      val moshi = Moshi.Builder().build()
      mealsService =
        Retrofit.Builder()
          .baseUrl(mockServer.url("/"))
          .addConverterFactory(MoshiConverterFactory.create(moshi))
          .build().let {
            it.create(MealDBService::class.java)
          }
    }

    @AfterEach
    fun afterEach() {
      mockServer.shutdown()
    }

    @Order(value = 7)
    @Test
    fun `verify filterByArea request format`() = runBlocking {
      enqueueResponse("list_meals_area_canadian.json", mockServer = mockServer)
      val mealsListCall = mealsService.mealsFilteredByArea("Canadian")
      mealsListCall.await()
      val recordedRequestTest: RecordedRequest = mockServer.takeRequest()
      assert(recordedRequestTest.path == "/api/json/v1/1/filter.php?&a=Canadian")
      assert(recordedRequestTest.method == "GET")
    }

    @Order(value = 8)
    @Test
    fun `verify filterByCategory request format`() = runBlocking {
      enqueueResponse("list_meals_category_seafood.json", mockServer = mockServer)
      val mealsListCall = mealsService.mealsFilteredByCategory("Seafood")
      mealsListCall.await()
      val recordedRequestTest: RecordedRequest = mockServer.takeRequest()
      assert(recordedRequestTest.path == "/api/json/v1/1/filter.php?&c=Seafood")
      assert(recordedRequestTest.method == "GET")
    }

    @Order(value = 9)
    @Test
    fun `verify filterByIngredient request format`() = runBlocking {
      enqueueResponse("list_meals_ingredient_chicken.json", mockServer = mockServer)
      val mealsListCall = mealsService.mealsFilteredByIngredient("Smoked Salmon")
      mealsListCall.await()
      val recordedRequestTest: RecordedRequest = mockServer.takeRequest()
      assert(recordedRequestTest.path == "/api/json/v1/1/filter.php?&i=Smoked%20Salmon")
      assert(recordedRequestTest.method == "GET")
    }
  }

  private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap(), mockServer: MockWebServer = mockWebServer) {
    val inputStream = javaClass.classLoader
      .getResourceAsStream("mealdb-api-response/$fileName")
    val source = Okio.buffer(Okio.source(inputStream))
    val mockResponse = MockResponse()
    for ((key, value) in headers) {
      mockResponse.addHeader(key, value)
    }
    mockServer.enqueue(
      mockResponse
        .setBody(source.readString(Charsets.UTF_8))
    )
  }
}
