package au.edu.jcu.assessment.utilityapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GNewsResponse(
    val totalArticles: Int,
    val articles: List<Article>
)

@Serializable
data class Article(
    val id: String,
    val title: String,
    val description: String?,
    val content: String?,
    val url: String,
    val image: String?,
    val publishedAt: String,
    val source: Source
)

@Serializable
data class Source(
    val id: String?,
    val name: String,
    val url: String
)