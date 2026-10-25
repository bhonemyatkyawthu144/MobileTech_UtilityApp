package au.edu.jcu.assessment.utilityapp.di

import au.edu.jcu.assessment.utilityapp.data.remote.NewsApiService
import au.edu.jcu.assessment.utilityapp.data.repository.NewsRepository
import au.edu.jcu.assessment.utilityapp.viewmodel.NewsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import au.edu.jcu.assessment.utilityapp.BuildConfig

val appModule = module {

    single {
        Retrofit.Builder()
            .baseUrl("https://gnews.io/api/")
            .addConverterFactory(
                Json {
                    ignoreUnknownKeys = true
                }.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(NewsApiService::class.java)
    }

    single {
        NewsRepository(
            apiService = get(),
            apiKey = BuildConfig.GNEWS_API_KEY
        )
    }

    viewModel {
        NewsViewModel(
            repository = get()
        )
    }
}