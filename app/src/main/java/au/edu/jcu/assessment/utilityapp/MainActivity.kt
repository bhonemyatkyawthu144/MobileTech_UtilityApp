package au.edu.jcu.assessment.utilityapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.edu.jcu.assessment.utilityapp.ui.theme.MobileTech_UtilityAppTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Card
import androidx.compose.material3.Switch
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import au.edu.jcu.assessment.utilityapp.viewmodel.NewsViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material3.CircularProgressIndicator
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.CardDefaults
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UtilityApp()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UtilityAppPreview() {
    MobileTech_UtilityAppTheme {
        UtilityApp()
    }
}

@Composable
fun UtilityApp() {
    var selectedTab by remember { mutableStateOf("Utility") }
    var showDescriptions by remember { mutableStateOf(true) }
    var defaultCategory by remember { mutableStateOf("All") }
    var articlesToShow by remember { mutableStateOf(5) }
    var theme by remember { mutableStateOf("System") }

    val darkTheme = when (theme) {
        "Dark" -> true
        "Light" -> false
        else -> isSystemInDarkTheme()
    }

    MobileTech_UtilityAppTheme(darkTheme = darkTheme) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Utility") },
                        label = { Text("Utility") },
                        selected = selectedTab == "Utility",
                        onClick = { selectedTab = "Utility" }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                        label = { Text("Settings") },
                        selected = selectedTab == "Settings",
                        onClick = { selectedTab = "Settings" }
                    )
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                when (selectedTab) {
                    "Utility" -> UtilityScreen(
                        showDescriptions = showDescriptions,
                        defaultCategory = defaultCategory,
                        articlesToShow = articlesToShow

                    )

                    "Settings" -> SettingsScreen(
                        showDescriptions = showDescriptions,
                        onShowDescriptionsChange = {
                            showDescriptions = it
                        },
                        defaultCategory = defaultCategory,
                        onDefaultCategoryChange = {
                            defaultCategory = it
                        },
                        articlesToShow = articlesToShow,
                        onArticlesToShowChange = {
                            articlesToShow = it
                        },
                        theme = theme,
                        onThemeChange = {
                            theme = it
                        }
                    )
                }
            }
        }
    }
}
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UtilityScreen(
        showDescriptions: Boolean,
        defaultCategory: String,
        articlesToShow: Int
    ) {
        val context = LocalContext.current
        val viewModel: NewsViewModel = koinViewModel()
        val articles by viewModel.articles.collectAsState()
        val error by viewModel.error.collectAsState()
        val isLoading by viewModel.isLoading.collectAsState()
        var selectedCategory by remember(defaultCategory) {
            mutableStateOf(defaultCategory)
        }
        LaunchedEffect(selectedCategory, articlesToShow) {
            val apiCategory = when (selectedCategory) {
                "World" -> "world"
                "Business" -> "business"
                "Technology" -> "technology"
                "Sports" -> "sports"
                else -> "general"
            }

            viewModel.loadNews(
                category = apiCategory,
                max = articlesToShow
            )
        }
        val categories = listOf(
            "All",
            "World",
            "Business",
            "Technology",
            "Sports"
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "NEWS",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = {
                                selectedCategory = category
                            },
                            label = {
                                Text(category)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Spacer(modifier = Modifier.height(8.dp))

                        articles.firstOrNull()?.let { featuredArticle ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        val intent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse(featuredArticle.url)
                                        )
                                        context.startActivity(intent)
                                    }
                            ) {
                                    Column {
                                        featuredArticle.image?.let { imageUrl ->
                                            AsyncImage(
                                                model = imageUrl,
                                                contentDescription = featuredArticle.title,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(220.dp),
                                                contentScale = ContentScale.Crop
                                            )
                                        }

                                        Column(
                                            modifier = Modifier.padding(12.dp)
                                        ) {
                                            Text(
                                                text = "Featured Story",
                                                style = MaterialTheme.typography.labelMedium
                                            )

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Text(
                                                text = featuredArticle.title,
                                                style = MaterialTheme.typography.titleLarge
                                            )

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Text(
                                                text = featuredArticle.description
                                                    ?: "No description available.",
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }
                                }
                            }


                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Latest News",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(20.dp))

                error?.let {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Unable to load news",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (isLoading) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                } else if (articles.isEmpty() && error == null) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No news available",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "There are no articles available for this category right now.",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    articles.take(articlesToShow).forEach { article ->
                        NewsListItem(
                            title = article.title,
                            description = article.description ?: "No description available.",
                            source = article.source.name,
                            publishedAt = article.publishedAt,
                            image = article.image,
                            showDescription = showDescriptions,
                            url = article.url
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

            }
        }
    }

fun formatPublishedDate(publishedAt: String): String {
    return try {
        val date = Instant.parse(publishedAt)
            .atZone(ZoneId.systemDefault())

        DateTimeFormatter
            .ofPattern("dd MMM yyyy")
            .format(date)
    } catch (e: Exception) {
        publishedAt
    }
}


@Composable
fun NewsListItem(
    title: String,
    description: String,
    source: String,
    publishedAt: String,
    image: String?,
    showDescription: Boolean,
    url: String
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url)
                )
                context.startActivity(intent)
            },
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            image?.let {
                AsyncImage(
                    model = it,
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            }

            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = "$source · ${publishedAt.take(10)}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (showDescription) {
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

        @Composable
        fun SettingsScreen(
            showDescriptions: Boolean,
            onShowDescriptionsChange: (Boolean) -> Unit,
            defaultCategory: String,
            onDefaultCategoryChange: (String) -> Unit,
            articlesToShow: Int,
            onArticlesToShowChange: (Int) -> Unit,
            theme: String,
            onThemeChange: (String) -> Unit
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Appearance",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Theme",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = theme == "System",
                        onClick = {
                            onThemeChange("System")
                        },
                        label = { Text("System") }
                    )

                    FilterChip(
                        selected = theme == "Light",
                        onClick = {
                            onThemeChange("Light")
                        },
                        label = { Text("Light") }
                    )

                    FilterChip(
                        selected = theme == "Dark",
                        onClick = {
                            onThemeChange("Dark")
                        },
                        label = { Text("Dark") }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "News",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Default category",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = defaultCategory == "All",
                        onClick = {
                            onDefaultCategoryChange("All")
                        },
                        label = { Text("All") }
                    )

                    FilterChip(
                        selected = defaultCategory == "Technology",
                        onClick = {
                            onDefaultCategoryChange("Technology")
                        },
                        label = { Text("Technology") }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Articles to show",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = articlesToShow == 5,
                        onClick = {
                            onArticlesToShowChange(5)
                        },
                        label = { Text("5") }
                    )

                    FilterChip(
                        selected = articlesToShow == 10,
                        onClick = {
                            onArticlesToShowChange(10)
                        },
                        label = { Text("10") }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Show descriptions",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Switch(
                        checked = showDescriptions,
                        onCheckedChange = {
                            onShowDescriptionsChange(it)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "About this app",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "News Utility App",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "Version 1.0",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
