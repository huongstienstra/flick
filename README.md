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
- **Advanced video optimizations** (see below)
- Clean architecture with multi-module structure

---

## Video Optimization Techniques

This app implements **4 key optimizations** inspired by [TikTok's Android engineering](https://android-developers.googleblog.com/2022/08/precise-improvements-how-tiktok-enhanced-its-social-experience-on-android.html):

```
┌─────────────────────────────────────────────────────────────────────────┐
│                        VIDEO PLAYBACK OPTIMIZATIONS                      │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│   ┌──────────────┐    ┌──────────────┐    ┌──────────────┐              │
│   │   Network    │    │    Video     │    │    Frame     │              │
│   │   Monitor    │───▶│  Preloader   │───▶│  Scheduler   │              │
│   └──────────────┘    └──────────────┘    └──────────────┘              │
│          │                   │                   │                       │
│          ▼                   ▼                   ▼                       │
│   ┌──────────────┐    ┌──────────────┐    ┌──────────────┐              │
│   │   Adaptive   │    │   Instant    │    │   Smooth     │              │
│   │   Buffering  │    │   Playback   │    │   Scrolling  │              │
│   └──────────────┘    └──────────────┘    └──────────────┘              │
│                                                                          │
└─────────────────────────────────────────────────────────────────────────┘
```

---

### 1. Adaptive Buffer Sizing

Dynamically adjusts video buffer based on network conditions to optimize startup time and prevent rebuffering.

```
                    NETWORK QUALITY DETECTION
    ┌─────────────────────────────────────────────────────┐
    │                                                     │
    │   WiFi ────────┐                                    │
    │                │      ┌─────────────────┐           │
    │   5G ──────────┼─────▶│ NetworkMonitor  │           │
    │                │      └────────┬────────┘           │
    │   4G/LTE ──────┤               │                    │
    │                │               ▼                    │
    │   3G ──────────┘      ┌─────────────────┐           │
    │                       │ Quality Level   │           │
    │                       └────────┬────────┘           │
    └────────────────────────────────┼────────────────────┘
                                     │
                                     ▼
    ┌─────────────────────────────────────────────────────┐
    │              ADAPTIVE BUFFER CONFIGURATION          │
    ├─────────────────────────────────────────────────────┤
    │                                                     │
    │  EXCELLENT (WiFi/5G)     POOR (3G/Edge)            │
    │  ┌───────────────┐       ┌───────────────┐         │
    │  │ Min: 10s      │       │ Min: 30s      │         │
    │  │ Max: 25s      │       │ Max: 60s      │         │
    │  │ ▶ Quick Start │       │ ▶ No Rebuffer │         │
    │  └───────────────┘       └───────────────┘         │
    │                                                     │
    │  GOOD (4G/LTE)           MODERATE (Slow LTE)       │
    │  ┌───────────────┐       ┌───────────────┐         │
    │  │ Min: 15s      │       │ Min: 20s      │         │
    │  │ Max: 35s      │       │ Max: 45s      │         │
    │  └───────────────┘       └───────────────┘         │
    │                                                     │
    └─────────────────────────────────────────────────────┘
```

**Files:** `NetworkMonitor.kt` · `ExoPlayerCache.kt`

---

### 2. Video Pre-rendering

Pre-loads the next video while the current one plays, enabling **instant playback** on swipe.

```
                         VIDEO PRELOADING FLOW

    ┌─────────────────────────────────────────────────────────────┐
    │                                                             │
    │   CURRENT VIDEO                    NEXT VIDEO               │
    │   ┌─────────────┐                  ┌─────────────┐          │
    │   │             │                  │             │          │
    │   │  ▶ Playing  │    ──────────▶   │  Preparing  │          │
    │   │             │    Background    │   (Hidden)  │          │
    │   │             │                  │             │          │
    │   └─────────────┘                  └─────────────┘          │
    │                                                             │
    └─────────────────────────────────────────────────────────────┘
                              │
                              │ User Swipes Up
                              ▼
    ┌─────────────────────────────────────────────────────────────┐
    │                                                             │
    │   PREVIOUS VIDEO                   CURRENT VIDEO            │
    │   ┌─────────────┐                  ┌─────────────┐          │
    │   │             │                  │             │          │
    │   │  Released   │                  │  ▶ Instant  │          │
    │   │             │                  │    Play!    │          │
    │   │             │                  │             │          │
    │   └─────────────┘                  └─────────────┘          │
    │                                                             │
    └─────────────────────────────────────────────────────────────┘
```

**Performance Impact:**

```
    Without Preloading:
    ├── Swipe ──┤── Load URL ──┤── Buffer ──┤── Decode ──┤▶ Play
                |<────────── 800-2000ms ──────────────>|

    With Preloading:
    ├── Swipe ──┤▶ Play
                |< ~50ms >|
```

**Files:** `VideoPreloader.kt` · `HomeScreen.kt` · `ShowViewModel.kt`

---

### 3. Frame Distribution

Distributes heavy tasks across multiple frames using `Choreographer` to prevent jank during scrolling.

```
    ❌ WITHOUT FRAME SCHEDULING (Janky):
    ┌─────────────────────────────────────────────────────────┐
    │ Frame 1                                                 │
    │ ┌─────────────────────────────────────────────────────┐ │
    │ │ Load Avatar │ Load Thumb │ Setup UI │ Bindngs │ ... │ │
    │ └─────────────────────────────────────────────────────┘ │
    │ |<──────────────── 45ms (DROPPED!) ──────────────────>| │
    └─────────────────────────────────────────────────────────┘


    ✅ WITH FRAME SCHEDULING (Smooth 60fps):
    ┌─────────────────────────────────────────────────────────┐
    │ Frame 1              Frame 2              Frame 3       │
    │ ┌──────────────┐     ┌──────────────┐     ┌───────────┐ │
    │ │ Text Updates │     │ Load Avatar  │     │ Load Thumb│ │
    │ │ (Fast)       │     │              │     │           │ │
    │ └──────────────┘     └──────────────┘     └───────────┘ │
    │ |<── 8ms ────>|      |<── 12ms ───>|      |<── 10ms ─>| │
    └─────────────────────────────────────────────────────────┘
```

**Choreographer Callback:**

```
         ┌──────────────────────────────────────────┐
         │            Pending Task Queue            │
         │  ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐        │
         │  │ T1  │ │ T2  │ │ T3  │ │ T4  │  ...   │
         │  └─────┘ └─────┘ └─────┘ └─────┘        │
         └──────────────────┬───────────────────────┘
                            │
                            ▼
         ┌──────────────────────────────────────────┐
         │           Frame Callback                 │
         │  Execute max 2 tasks per frame           │
         │  ┌─────┐ ┌─────┐                         │
         │  │ T1  │ │ T2  │ ───▶ Done               │
         │  └─────┘ └─────┘                         │
         │           │                              │
         │           ▼ Schedule next frame          │
         └───────────┬──────────────────────────────┘
                     │
                     ▼
         ┌──────────────────────────────────────────┐
         │           Next Frame                     │
         │  ┌─────┐ ┌─────┐                         │
         │  │ T3  │ │ T4  │ ───▶ Done               │
         │  └─────┘ └─────┘                         │
         └──────────────────────────────────────────┘
```

**Files:** `FrameScheduler.kt` · `VideoAdapter.kt`

---

### 4. Thumbnail Loading State

Shows video thumbnail during buffering to eliminate black screen flash.

```
    ❌ WITHOUT THUMBNAIL:              ✅ WITH THUMBNAIL:

    ┌─────────────────────┐            ┌─────────────────────┐
    │                     │            │  ┌───────────────┐  │
    │                     │            │  │   Thumbnail   │  │
    │    BLACK SCREEN     │            │  │     Image     │  │
    │                     │            │  │       +       │  │
    │                     │            │  │   ◷ Loading   │  │
    │                     │            │  └───────────────┘  │
    └─────────────────────┘            └─────────────────────┘
           ↓                                    ↓
    ┌─────────────────────┐            ┌─────────────────────┐
    │                     │            │                     │
    │   VIDEO PLAYING     │            │   VIDEO PLAYING     │
    │                     │            │  (Smooth transition)│
    │                     │            │                     │
    └─────────────────────┘            └─────────────────────┘
```

**State Flow:**

```
    ┌─────────┐   prepare()   ┌───────────┐  first frame  ┌─────────┐
    │  IDLE   │ ────────────▶ │ BUFFERING │ ────────────▶ │  READY  │
    └─────────┘               └───────────┘               └─────────┘
         │                          │                          │
         │                          │                          │
         ▼                          ▼                          ▼
      Nothing              Thumbnail + Spinner              Video
```

**Files:** `VideoPlayer.kt`

---

## Architecture

```
┌─────────────────────────────────────────────────────────────────────────┐
│                              APP MODULE                                  │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│  ┌─────────────────────────────────────────────────────────────────┐    │
│  │                         UI LAYER                                 │    │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐              │    │
│  │  │ HomeScreen  │  │SearchScreen │  │ProfileScreen│              │    │
│  │  │ (Compose)   │  │  (Compose)  │  │  (Compose)  │              │    │
│  │  └──────┬──────┘  └─────────────┘  └─────────────┘              │    │
│  │         │                                                        │    │
│  │         ▼                                                        │    │
│  │  ┌─────────────────────────────────────────────────────────┐    │    │
│  │  │                    VideoPlayer                           │    │    │
│  │  │  ┌───────────┐  ┌───────────┐  ┌───────────┐            │    │    │
│  │  │  │ PlayerView│  │ Thumbnail │  │  Loading  │            │    │    │
│  │  │  └───────────┘  └───────────┘  └───────────┘            │    │    │
│  │  └─────────────────────────────────────────────────────────┘    │    │
│  └─────────────────────────────────────────────────────────────────┘    │
│                                    │                                     │
│                                    ▼                                     │
│  ┌─────────────────────────────────────────────────────────────────┐    │
│  │                      VIEWMODEL LAYER                             │    │
│  │  ┌─────────────────────────────────────────────────────────┐    │    │
│  │  │                   ShowViewModel                          │    │    │
│  │  │  • videos: Flow<PagingData<VideoShow>>                   │    │    │
│  │  │  • getPlayerForVideo(url): ExoPlayer                     │    │    │
│  │  │  • preloadNextVideo(url)                                 │    │    │
│  │  └─────────────────────────────────────────────────────────┘    │    │
│  └─────────────────────────────────────────────────────────────────┘    │
│                                    │                                     │
│                                    ▼                                     │
│  ┌─────────────────────────────────────────────────────────────────┐    │
│  │                    VIDEO CORE LAYER                              │    │
│  │                                                                  │    │
│  │  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐           │    │
│  │  │ExoPlayerCache│  │VideoPreloader│  │NetworkMonitor│           │    │
│  │  │              │  │              │  │              │           │    │
│  │  │ • getPlayer()│  │• preloadNext │  │• getQuality()│           │    │
│  │  │ • adaptive   │  │• getPreload  │  │• WiFi/4G/3G  │           │    │
│  │  │   buffering  │  │              │  │  detection   │           │    │
│  │  └──────────────┘  └──────────────┘  └──────────────┘           │    │
│  │                                                                  │    │
│  │  ┌──────────────────────────────────────────────────┐           │    │
│  │  │              FrameScheduler                       │           │    │
│  │  │  • Choreographer-based task distribution          │           │    │
│  │  │  • Max 2 tasks per frame for 60fps               │           │    │
│  │  └──────────────────────────────────────────────────┘           │    │
│  └─────────────────────────────────────────────────────────────────┘    │
│                                                                          │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                           DATA MODULES                                   │
├──────────────────┬──────────────────┬───────────────────────────────────┤
│   data:remote    │  data:repository │      data:local                   │
│  ┌────────────┐  │  ┌────────────┐  │  ┌────────────┐                   │
│  │PexelsAPI   │  │  │VideoRepo   │  │  │SharedPrefs │                   │
│  │Service     │──┼─▶│Impl        │──┼─▶│DataSource  │                   │
│  └────────────┘  │  └────────────┘  │  └────────────┘                   │
└──────────────────┴──────────────────┴───────────────────────────────────┘
```

---

## Key Files

```
app/src/main/java/com/flick/app/
├── ui/screens/show/
│   ├── core/
│   │   ├── video/
│   │   │   ├── ExoPlayerCache.kt      # Player pooling + adaptive buffering
│   │   │   ├── NetworkMonitor.kt      # Network quality detection
│   │   │   └── VideoPreloader.kt      # Next video pre-rendering
│   │   └── FrameScheduler.kt          # Choreographer task distribution
│   ├── composable/
│   │   └── VideoPlayer.kt             # Video player with thumbnail
│   ├── HomeScreen.kt                  # Vertical pager with preloading
│   └── ShowViewModel.kt               # Video feed state management
└── di/
    └── AppModule.kt                   # Dependency injection
```

---

## Tech Stack

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

---

## Setup

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 8 or higher
- Android SDK 34

### API Key Configuration

This app uses the [Pexels API](https://www.pexels.com/api/) for video content:

1. Get a free API key from [Pexels](https://www.pexels.com/api/)
2. Add your API key to `local.properties`:

```properties
PEXELS_API_KEY=your_api_key_here
```

### Build

```bash
./gradlew assembleDebug
```

---

## Acknowledgments

- [TikTok Android Engineering Blog](https://android-developers.googleblog.com/2022/08/precise-improvements-how-tiktok-enhanced-its-social-experience-on-android.html) for optimization techniques
- [Pexels](https://www.pexels.com/) for free video content API
- [ExoPlayer](https://github.com/google/ExoPlayer) for video playback
- [Jetpack Compose](https://developer.android.com/jetpack/compose) for modern UI toolkit
