# Smart Pantry Manager

## Project Description

Smart Pantry Manager is an Android application developed as part of the Mobile App Development 700 (MAD700) practical assignment at Richfield Graduate Institute of Technology.

The purpose of the application is to help users manage ingredients they have at home and reduce food waste by identifying recipes they can prepare using their available ingredients.

The application was developed in Java using Android Studio, with SQLite used to store pantry items and recipe information locally.

## Application Features

The application includes the following features:

- Add ingredients to the pantry, including their names, quantities, units and optional expiry dates.
- View saved pantry ingredients in a list.
- Edit and delete pantry items.
- Search for ingredients in the pantry.
- View the recipe collection stored in the application.
- View recipe ingredients and preparation instructions.
- Check required ingredients and quantities against the available pantry stock.
- Display whether a recipe can be made, or identify missing ingredients and insufficient quantities.
- Access a settings screen for the application's available preferences.
- Save pantry information locally so that it remains available after closing and reopening the application.
- Display information about the number of recipes stored, the number of recipes that can be made and the number of pantry entries saved.

## Technologies Used

- **Java:** Used to implement the application's functionality and logic.
- **Android Studio:** Used to develop, build and test the Android application.
- **SQLite:** Used to store pantry ingredients and recipe information locally on the device.
- **XML:** Used for Android layouts, resources and application configuration.
- **Gradle:** Used to configure and build the Android project.
- **GitHub:** Used to store the source code and track development changes.

## Database Choice

SQLite was selected because the application needs to store and retrieve pantry ingredients and recipe information locally on the device. SQLite does not require a separate database server, making it suitable for an Android application that manages personal pantry data.

The pantry database stores ingredient names, quantities, units and optional expiry dates. The recipe database stores recipe names, preparation methods and the ingredients and quantities required for each recipe.

Using local databases allows the application to retrieve saved information when needed and retain pantry records between application sessions.

## Recipe Matching

An important requirement of this assignment is strict ingredient matching. The application compares the ingredients and quantities required by each recipe with the ingredients available in the pantry.

The matching logic checks whether each required ingredient is present and whether its available quantity is sufficient. If an ingredient is missing, the application identifies it. If the available quantity is insufficient, the application displays a message explaining how much is required and how much is available.

The application displays the recipe collection and indicates which recipes can be prepared with the current pantry ingredients. This helps users understand which recipes they can make without needing to purchase additional ingredients.

## Data Storage and Persistence

The application uses SQLite databases to store pantry information and recipe data. Pantry records can be added, edited and deleted as the user's available ingredients change.

The pantry information is saved locally, allowing the application to retrieve saved entries when it is reopened. The recipe database provides the information required to display recipes, their ingredients and their preparation methods.

## How to Run the Application

1. Download or clone the Smart Pantry Manager repository from GitHub.
2. Install and open Android Studio.
3. Select **Open** and choose the downloaded project folder.
4. Allow Android Studio to synchronise Gradle and download the required dependencies.
5. Connect an Android device with USB debugging enabled or start an Android emulator.
6. Wait for the project to finish building.
7. Select the application run configuration and click **Run**.
8. Once the application launches, use the pantry screen to manage ingredients and navigate to the recipe collection and other available screens.

The application should be run using a compatible Android device or emulator and the Android SDK configuration required by the project.

## Project Structure

The main project files and directories include:

- `app/src/main/java/` — contains the Java source code for the application activities and database helper classes.
- `app/src/main/res/` — contains application resources, including layouts, drawable resources and other interface resources.
- `app/src/main/AndroidManifest.xml` — contains the application and activity configuration.
- `gradle/` — contains Gradle wrapper configuration files.
- `app/build.gradle.kts` — contains the application module's build configuration.
- `settings.gradle.kts` — contains the Gradle project settings.

## Conclusion

The Smart Pantry Manager project applies concepts covered in Mobile App Development 700, including Android activities, layouts, Intents, adapters, SQLite database operations and local data persistence.

The application demonstrates how pantry information can be managed and compared with recipe requirements to help users identify meals they can prepare using ingredients already available at home. The project also provides practical experience in Android application development, database management, interface design and software testing.

## Repository

GitHub: https://github.com/tabassumally/Smart-Pantry-Manager
