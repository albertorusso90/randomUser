
# Random User App

This application is an example to showcase the use of various Android development techniques and best practices, including:

- **Jetpack Compose** for modern UI development
- **Coroutines** for asynchronous operations
- **Room Database** for local data storage
- **Retrofit** for network requests
- **Clean Architecture** for maintainable code structure
- **SOLID Principles** for good object-oriented design
- **Koin** for dependency injection

## Features

The app consists of a **List Screen** where:

- Users can **search for users** by their name, surname, or email.
- The list supports **infinite scrolling**, loading more users as the user scrolls down, requesting data from the API and caching the data locally using Room.
- Users can **swipe left** on items to **delete** them, with the deletion reflecting both in the UI and the local database.
- **Details Screen**: When a user taps on an item, the app opens a detailed view of the selected user, displaying additional information like their name, gender, address, email, and more.

## Technologies Used

- **Jetpack Compose**: A modern toolkit for building native UIs in Kotlin, making the UI development declarative and more concise.
- **Coroutines**: Used for managing background tasks, such as network calls and database operations, without blocking the main thread.
- **Room Database**: Provides a local storage solution, allowing the app to persist the user data locally.
- **Retrofit**: A type-safe HTTP client for making network requests to fetch the random user data.
- **Koin**: For dependency injection, simplifying the management of dependencies and ensuring better testability.
- **Kotlin**: The primary language used to build the app.

## App Flow

1. **User List Screen**:
   - Displays a list of random users fetched from the API and stored locally.
   - Users can search for specific users using a search box.
   - As the user scrolls, new users are loaded from the API and stored locally (infinite scroll).
   - Users can swipe left on an item to delete it.
   
2. **User Details Screen**:
   - When a user selects an item from the list, the app navigates to a detail screen.
   - The details screen displays comprehensive information about the user.

## Architecture

This app follows **Clean Architecture** to separate the concerns of different layers:

- **Domain Layer**: Contains business logic and use cases (e.g., `GetUsers`, `GetUserByEmail`, `HideUser`, `SearchUsers`).
- **Data Layer**: Handles data fetching from the network and local storage (e.g., Retrofit for API calls, Room for local database).
- **Presentation Layer**: The UI layer implemented using Jetpack Compose (e.g., `UserListScreen`, `UserDetailScreen`).
- **Dependency Injection**: Koin is used for dependency injection to manage and provide the required dependencies.

## How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/albertorusso90/randomUser
   ```
2. Open the project in **Android Studio**.
3. Build the project and run the app on either an emulator or a physical device.

## Conclusion

This app serves as a demonstration of how modern Android development techniques and principles can be used together to build a clean, maintainable, and testable application. It leverages Jetpack Compose for UI, Room for local storage, Retrofit for networking, Koin for dependency injection, and follows Clean Architecture and SOLID principles to ensure scalability and flexibility for future development.
