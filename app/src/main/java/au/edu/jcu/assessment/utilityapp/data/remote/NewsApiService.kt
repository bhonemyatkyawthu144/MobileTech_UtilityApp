package au.edu.jcu.assessment.utilityapp.data.remote

import au.edu.jcu.assessment.utilityapp.data.model.GNewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("v4/top-headlines")
    suspend fun getTopHeadlines(
        @Query("category") category: String? = null,
        @Query("lang") language: String = "en",
        @Query("max") max: Int = 10,
        @Query("apikey") apiKey: String
    ): GNewsResponse
}