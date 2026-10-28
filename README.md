
# Smart Pantry Manager

## Project Description

Smart Pantry Manager is an Android application developed as part of the Mobile App Development 700 (MAD700) practical assignment at Richfield Graduate Institute of Technology.

The purpose of the application is to help users manage ingredients they have at home and reduce food waste by suggesting recipes they can prepare using their available ingredients.

The application was developed in Java using Android Studio, with SQLite used to store pantry items and recipe information.

## Application Features

The application includes the following features:

- Add ingredients to the pantry, including their quantities.
- View the current pantry ingredients in a list.
- Edit and delete pantry items.
- Search for ingredients in the pantry.
- View suggested recipes based on the ingredients available.
- View the ingredients and preparation instructions for each recipe.
- Check ingredient names and quantities before suggesting recipes.
- Display expiry warnings for expired and soon-to-expire ingredients.
- Change the expiry alert preference through the settings screen.
- Save pantry information locally so that it remains available after closing and reopening the application.
- Display a message when no recipes match the available pantry ingredients.

## Technologies Used

- **Java:** Used to develop the Android application functionality.
- **Android Studio:** Used to develop and test the application.
- **SQLite:** Used to store pantry ingredients and recipe information locally.
- **XML:** Used for the application's layouts and resources.
- **GitHub:** Used to store the source code and track development changes.
- **Gradle:** Used to configure and build the Android project.

## Database Choice

SQLite was used because the application needs to store and retrieve pantry ingredients and recipe information on the device. It does not require a separate database server, and the information can remain saved when the application is closed.

The database supports the pantry management functions, including adding, viewing, editing and deleting ingredients.

## Recipe Matching

An important requirement of this assignment is that recipe suggestions must use strict ingredient matching.

The application checks whether the user has every ingredient required by a recipe and whether the available quantities are sufficient. If an ingredient is missing or there is not enough of it, the recipe should not appear in the suggested recipes list.

This allows the application to suggest recipes that the user can prepare with the ingredients already available, without needing to buy additional ingredients.

## How to Run the Application

1. Download or clone the Smart Pantry Manager repository from GitHub.
2. Open Android Studio.
3. Select **Open** and choose the downloaded project folder.
4. Allow Gradle to sync the project and download any required dependencies.
5. Connect an Android device or start an Android emulator.
6. Run the application using the Run button in Android Studio.

## Project Structure

- `app/src/main/java/` contains the application source code.
- `app/src/main/res/` contains layouts, drawable resources and other application resources.
- `app/src/main/AndroidManifest.xml` contains the application and activity configuration.
- `gradle/` contains Gradle configuration files and the wrapper.
- `app/build.gradle.kts` contains the application build configuration.
- `settings.gradle.kts` contains the project settings.

## Conclusion

This project applies the concepts covered in Mobile App Development 700, including Android activities, layouts, navigation, data storage and application functionality. The application demonstrates how an Android app can manage local data and use that information to provide recipe suggestions based on the user's available ingredients.
