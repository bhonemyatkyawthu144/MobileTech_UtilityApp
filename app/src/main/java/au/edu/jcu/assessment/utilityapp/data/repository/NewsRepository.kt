package au.edu.jcu.assessment.utilityapp.data.repository

import au.edu.jcu.assessment.utilityapp.data.model.GNewsResponse
import au.edu.jcu.assessment.utilityapp.data.remote.NewsApiService

class NewsRepository(
    private val apiService: NewsApiService,
    private val apiKey: String
) {

    suspend fun getNews(
        category: String? = null,
        max: Int = 10
    ): GNewsResponse {
        return apiService.getTopHeadlines(
            category = category,
            max = max,
            apiKey = apiKey
        )
    }
}