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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Card

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileTech_UtilityAppTheme {
                UtilityApp()
            }
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
                "Utility" -> UtilityScreen()
                "Settings" -> SettingsScreen()
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UtilityScreen() {
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = listOf(
        "All",
        "World",
        "Business",
        "Technology",
        "Sports"
    )

    Column(
        modifier = Modifier.fillMaxSize()
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

            Spacer(modifier = Modifier.height(8.dp))

            Spacer(modifier = Modifier.height(12.dp))

            NewsListItem(
                category = "Technology",
                title = "New technology is changing the way we stay informed",
                description = "Discover the latest developments and trends in technology."
            )

            Spacer(modifier = Modifier.height(12.dp))

            NewsListItem(
                category = "World",
                title = "Major events making headlines around the world",
                description = "A quick look at the latest international news."
            )

            Spacer(modifier = Modifier.height(12.dp))

            NewsListItem(
                category = "Business",
                title = "Markets and businesses respond to today's news",
                description = "The latest updates from the business world."
            )
        }
    }
}

@Composable
fun NewsListItem(
    category: String,
    title: String,
    description: String
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

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


@Composable
fun SettingsScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp), Arrangement.spacedBy(16.dp)
    ) {
        Text("Settings Screen", style = MaterialTheme.typography.headlineMedium)
        Text("This is where you can add toggles or preferences.")
    }
}