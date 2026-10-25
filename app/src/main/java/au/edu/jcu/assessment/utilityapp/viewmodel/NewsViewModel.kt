package au.edu.jcu.assessment.utilityapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.jcu.assessment.utilityapp.data.model.Article
import au.edu.jcu.assessment.utilityapp.data.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewsViewModel(
    private val repository: NewsRepository
) : ViewModel() {

    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadNews(category: String = "general", max: Int = 10) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                _articles.value = repository.getNews(
                    category = category,
                    max = max
                ).articles
            } catch (e: Exception) {
                _error.value = e.message ?: "Unable to load news"
            } finally {
                _isLoading.value = false
            }
        }
    }
}