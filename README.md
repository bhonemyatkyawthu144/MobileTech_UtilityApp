# News Utility App

A simple and modern Android news utility app built with Kotlin and Jetpack Compose. The app connects to the GNews API to display real-time news and lets users customise how the news is shown.

## Features

- View the latest news from a real news API
- Browse news by category:
    - All
    - World
    - Business
    - Technology
    - Sports
- Featured Story section with article images
- Open news articles in the device browser
- Choose how many articles to display
- Show or hide article descriptions
- Choose Light, Dark, or System theme
- Loading and error states
- Material 3 user interface

## Technologies

- Kotlin
- Jetpack Compose
- Material 3
- Retrofit
- Kotlinx Serialization
- Kotlin Coroutines
- Koin Dependency Injection
- Coil
- GNews API

## Project Structure

The app follows a simple architecture using:

- **UI** – Jetpack Compose screens and components
- **ViewModel** – Manages UI state and requests news data
- **Repository** – Handles communication between the ViewModel and API
- **Retrofit API Service** – Connects to the GNews API
- **Koin** – Provides dependency injection

## API Setup

This project uses the GNews API.

The API key is stored locally in `local.properties` and is not committed to GitHub.

Add your API key to `local.properties`:

```properties
GNEWS_API_KEY=71bf5b1c21a5dc88ae985cdf83d0d4bd
```

Then rebuild the project in Android Studio.

## How to Run

1. Clone or download this repository.
2. Open the project in Android Studio.
3. Add your GNews API key to `local.properties`.
4. Sync the Gradle project.
5. Run the app on an Android emulator or physical Android device.

## Assignment

This project was created for **CP3406/CP5307 Mobile Computing – Assignment 1: Utility App** at James Cook University Singapore.