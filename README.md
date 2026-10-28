
# Smart Pantry Manager

## About the Project

Smart Pantry Manager is an Android application developed for the Mobile App Development 700 (MAD700) practical assignment at Richfield Graduate Institute of Technology.

The application helps users manage pantry ingredients, reduce food waste, and discover recipes they can prepare using ingredients they already have available.

## Features

- **Pantry Management:** Add, view, edit, and delete pantry ingredients.
- **Local Database:** Store pantry ingredients and recipe information locally using SQLite.
- **Recipe Suggestions:** Match available pantry ingredients against recipe requirements.
- **Strict Recipe Matching:** Only suggest recipes when the user has all the required ingredients in sufficient quantities.
- **Recipe Details:** View recipe ingredients and preparation instructions.
- **Expiry Warnings:** Identify expired and soon-to-expire pantry ingredients when opening the application.
- **Search:** Search through pantry ingredients.
- **Settings:** Manage application preferences, including the expiry alert setting.
- **Persistent Storage:** Keep pantry information available after closing and reopening the application.

## Technologies Used

- **Java** — Android application functionality.
- **Android Studio** — Application development and testing.
- **SQLite** — Local storage for pantry items and recipe information.
- **XML** — Android application layouts and resources.
- **GitHub** — Source code hosting and version control.
- **Gradle** — Project build configuration and dependency management.

## Database

SQLite is used to store and retrieve pantry and recipe information locally on the device. This allows core pantry management functionality to work without requiring a separate database server.

## Recipe Matching

Recipe suggestions are designed to follow strict ingredient matching rules. A recipe should only appear in the main suggested recipes list when the user has every required ingredient in sufficient quantities. Recipes requiring missing ingredients or quantities that the user does not have should not be suggested as available recipes.

## How to Run the Application

1. Install Android Studio.
2. Clone or download this repository from GitHub.
3. Open the project in Android Studio by selecting the project root folder containing `settings.gradle.kts`.
4. Allow Android Studio to sync the Gradle project and download the required dependencies.
5. Connect an Android device with USB debugging enabled or start an Android emulator.
6. Run the `app` configuration.

## Project Structure

- `app/src/main/java/` — Java and Kotlin source files.
- `app/src/main/res/` — Android layouts, drawable resources, and application themes.
- `app/src/main/AndroidManifest.xml` — Application and activity configuration.
- `gradle/` — Gradle wrapper, version catalog, and related configuration.
- `app/build.gradle.kts` — Android application build configuration.
- `settings.gradle.kts` — Project settings and module configuration.

## Developer

Developed by Tabassum Beemath for the MAD700 practical assignment at Richfield Graduate Institute of Technology.
