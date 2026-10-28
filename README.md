
# Smart Pantry Manager

## About the Project

Smart Pantry Manager is an Android application that I developed for my Mobile App Development 700 (MAD700) practical assignment at Richfield Graduate Institute of Technology.

I chose this idea because food sometimes goes to waste when people forget about ingredients they already have at home. This app helps users keep track of their pantry items and find recipes they can make using the ingredients they have available.

## Features

The application includes the following features:

- Add ingredients to the pantry and enter their quantities.
- View all saved pantry ingredients.
- Edit ingredient details and delete ingredients that are no longer needed.
- Search for ingredients in the pantry.
- View suggested recipes based on the ingredients available.
- View recipe ingredients and preparation instructions.
- Display recipes only when all the required ingredients are available in sufficient quantities.
- Show warnings for ingredients that have expired or are close to expiring.
- Save pantry information using a local database.
- Change the expiry alert setting through the settings screen.

## Technologies Used

The following technologies were used to develop the application:

- **Java** – Used to write the application functionality.
- **Android Studio** – Used to develop and test the application.
- **SQLite** – Used to store pantry ingredients and recipe information.
- **XML** – Used to create the application's layouts and resources.
- **GitHub** – Used to store the project files and keep track of the project.

## Database

I used SQLite because the application needs to save pantry ingredients and recipe information on the device. This means users can close the application and open it again without losing their saved pantry items.

## Recipe Matching

One of the main features of my application is the recipe matching system. The app checks the ingredients and quantities available in the user's pantry before suggesting a recipe.

For example, if a recipe requires five ingredients but the user only has four, that recipe should not appear in the suggested recipes list. The user must have all the required ingredients in the correct quantities to make the recipe.

## Testing

I tested the main features of the application to check that they work as expected. This included adding, editing and deleting ingredients, searching the pantry, viewing recipe suggestions, checking expiry warnings and changing the expiry alert setting.

## How to Run the Application

1. Download or clone the project from GitHub.
2. Open Android Studio.
3. Select **Open** and choose the downloaded project folder.
4. Allow Gradle to sync and download any required dependencies.
5. Connect an Android device or start an emulator.
6. Click the **Run** button in Android Studio to launch the application.

## Conclusion

Developing Smart Pantry Manager helped me learn more about Android application development, working with an SQLite database and implementing different application features. It also helped me understand how an application can be used to solve a practical problem, such as reducing food waste at home.
