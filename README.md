# Flick

A modern TikTok-style vertical video streaming app for Android, built with Kotlin, Jetpack Compose, and clean architecture principles.

![Android](https://img.shields.io/badge/Android-3DDC84?style=flat&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=flat&logo=jetpack-compose&logoColor=white)
![ExoPlayer](https://img.shields.io/badge/ExoPlayer-FF0000?style=flat&logo=youtube&logoColor=white)

## Features

- Vertical video feed with smooth swiping (TikTok-style)
- Video search functionality
- Like and comment on videos
- User profiles and authentication
- Optimized video playback with ExoPlayer
- Clean architecture with multi-module structure

## Architecture

```
Flick/
├── app/                    # Main application module
│   ├── ui/                # UI layer (Compose screens, ViewModels)
│   ├── navigation/        # Compose Navigation
│   └── di/                # Dependency Injection (Koin)
├── data/
│   ├── remote/            # Network layer (Retrofit, Pexels API)
│   ├── repository/        # Repository implementations
│   └── local/             # Local storage (SharedPreferences)
└── common/                # Shared UI components
```

### Tech Stack

| Category | Technology |
|----------|------------|
| UI | Jetpack Compose, Material 3 |
| Video | ExoPlayer (Media3) |
| Networking | Retrofit, OkHttp |
| DI | Koin |
| Image Loading | Coil |
| Async | Kotlin Coroutines, Flow |
| Paging | Paging 3 |
| Logging | Timber |
| Testing | JUnit, MockK, Turbine |

## Setup

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 8 or higher
- Android SDK 34

### API Key Configuration

This app uses the [Pexels API](https://www.pexels.com/api/) for video content. To run the app:

1. Get a free API key from [Pexels](https://www.pexels.com/api/)
2. Add your API key to `local.properties`:

```properties
PEXELS_API_KEY=your_api_key_here
```

### Build

```bash
# Clone the repository
git clone https://github.com/yourusername/flick-android.git

# Open in Android Studio and sync Gradle

# Or build from command line
./gradlew assembleDebug
```

### Run Tests

```bash
./gradlew test
```

## Module Structure

### `:app`
Main application module containing:
- Compose UI screens (Home, Search, Profile)
- ViewModels with state management
- Navigation setup
- Dependency injection configuration

### `:data:remote`
Network layer:
- Pexels API service
- API models and response handling
- OkHttp client configuration

### `:data:repository`
Business logic:
- Repository implementations
- Data mappers (Pexels -> Domain models)
- Caching strategies

### `:data:local`
Local storage:
- Video interaction storage (likes, comments)
- User preferences
- SharedPreferences wrapper

### `:common`
Shared components:
- Custom UI views
- Common composables
- Theme and styling

## Key Components

### Video Playback
- `ExoPlayerCache`: Manages a pool of ExoPlayer instances for efficient memory usage
- `VideoPlayer`: Compose wrapper for ExoPlayer with lifecycle awareness
- `HomeScreen`: VerticalPager-based video feed

### API Integration
- `PexelsApiService`: Retrofit interface for Pexels Video API
- `VideoRepositoryImp`: Combines remote API with local storage
- `PexelsVideoMapping`: Transforms API responses to domain models

### State Management
- `UiState<T>`: Sealed class for Loading/Success/Error states
- `ShowViewModel`: Manages video feed, likes, and comments
- `SearchViewModel`: Handles search query and results

## License

This project is for educational and portfolio purposes.

## Acknowledgments

- [Pexels](https://www.pexels.com/) for providing free video content API
- [ExoPlayer](https://github.com/google/ExoPlayer) for video playback
- [Jetpack Compose](https://developer.android.com/jetpack/compose) for modern UI toolkit
