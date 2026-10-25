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

        val viewModel: NewsViewModel = koinViewModel()
        val articles by viewModel.articles.collectAsState()
        val error by viewModel.error.collectAsState()
        var selectedCategory by remember { mutableStateOf(defaultCategory) }
        LaunchedEffect(selectedCategory) {
            val apiCategory = when (selectedCategory) {
                "World" -> "world"
                "Business" -> "business"
                "Technology" -> "technology"
                "Sports" -> "sports"
                else -> "general"
            }

            viewModel.loadNews(category = apiCategory)
        }
        val categories = listOf(
            "All",
            "World",
            "Business",
            "Technology",
            "Sports"
        )

        val sampleArticles = listOf(
            Triple(
                "Technology",
                "New technology is changing the way we stay informed",
                "Discover the latest developments and trends in technology."
            ),
            Triple(
                "World",
                "Major events making headlines around the world",
                "A quick look at the latest international news."
            ),
            Triple(
                "Business",
                "Markets and businesses respond to today's news",
                "The latest updates from the business world."
            ),
            Triple(
                "Technology",
                "New devices bring smarter features to everyday life",
                "Technology companies continue to introduce new products."
            ),
            Triple(
                "World",
                "Countries respond to important global developments",
                "The latest updates from around the world."
            ),
            Triple(
                "Business",
                "Companies prepare for changes in the global market",
                "Businesses are adapting to new market conditions."
            ),
            Triple(
                "Technology",
                "Artificial intelligence continues to attract attention",
                "New AI tools are changing how people work and create."
            ),
            Triple(
                "World",
                "Global communities focus on important issues",
                "A summary of major international developments."
            ),
            Triple(
                "Business",
                "Markets continue to react to new economic data",
                "Investors and businesses watch the latest market movements."
            ),
            Triple(
                "Technology",
                "Mobile technology continues to evolve",
                "New mobile technologies are creating new possibilities."
            )
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
                        Text(
                            text = "Featured Story",
                            style = MaterialTheme.typography.labelLarge
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Latest news and important stories",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Stay informed with the latest updates from around the world.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Latest News",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(20.dp))

                error?.let {
                    Text(
                        text = "Error: $it",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }

                articles.take(articlesToShow).forEach { article ->

                    NewsListItem(
                        category = selectedCategory,
                        title = article.title,
                        description = article.description ?: "No description available.",
                        showDescription = showDescriptions
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }

    @Composable
    fun NewsListItem(
        category: String,
        title: String,
        description: String,
        showDescription: Boolean
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = category,
                    style = MaterialTheme.typography.labelMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )

                if (showDescription) {
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium
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
