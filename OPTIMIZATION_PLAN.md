# Flick Video Optimization Plan

Based on TikTok's Android optimization techniques, here's a step-by-step implementation plan.

---

## Phase 1: Dynamic Buffer Adjustment

### Goal
Adjust video buffer size based on network conditions to reduce initial load time on fast networks and prevent buffering on slow networks.

### Files to Modify
- `app/src/main/java/com/flick/app/ui/screens/show/core/video/ExoPlayerCache.kt`

### Implementation
```kotlin
// 1. Create NetworkMonitor class
class NetworkMonitor(context: Context) {
    enum class NetworkQuality { POOR, MODERATE, GOOD, EXCELLENT }

    fun getNetworkQuality(): NetworkQuality {
        // Check network speed using ConnectivityManager
        // Return quality based on bandwidth
    }
}

// 2. Create adaptive LoadControl
fun createAdaptiveLoadControl(quality: NetworkQuality): LoadControl {
    return DefaultLoadControl.Builder()
        .setBufferDurationsMs(
            when (quality) {
                POOR -> 30000      // 30s min buffer
                MODERATE -> 20000  // 20s min buffer
                GOOD -> 15000      // 15s min buffer
                EXCELLENT -> 10000 // 10s min buffer
            },
            when (quality) {
                POOR -> 60000      // 60s max buffer
                MODERATE -> 45000  // 45s max buffer
                GOOD -> 35000      // 35s max buffer
                EXCELLENT -> 25000 // 25s max buffer
            },
            2500,  // buffer for playback
            5000   // buffer for rebuffer
        )
        .build()
}
```

### Tasks
- [x] Create `NetworkMonitor.kt` in `core/video/`
- [x] Add network quality detection using ConnectivityManager
- [x] Modify `ExoPlayerCache` to accept dynamic LoadControl
- [ ] Test on different network conditions (WiFi, 4G, 3G)

---

## Phase 2: Video Surface Pre-rendering

### Goal
Pre-render the next video's surface while current video plays, enabling instant playback on swipe.

### Files to Create/Modify
- `app/src/main/java/com/flick/app/ui/screens/show/core/video/VideoPreloader.kt` (new)
- `app/src/main/java/com/flick/app/ui/screens/show/ShowViewModel.kt`
- `app/src/main/java/com/flick/app/ui/screens/show/HomeScreen.kt`

### Implementation
```kotlin
// 1. VideoPreloader - prepares next video
class VideoPreloader(
    private val playerCache: ExoPlayerCache
) {
    private var preloadedPlayer: ExoPlayer? = null
    private var preloadedVideoUrl: String? = null

    fun preloadNext(videoUrl: String) {
        if (videoUrl == preloadedVideoUrl) return

        // Release previous preloaded player
        preloadedPlayer?.release()

        // Get player and prepare (but don't play)
        preloadedPlayer = playerCache.getPlayer()
        preloadedPlayer?.apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
            playWhenReady = false
        }
        preloadedVideoUrl = videoUrl
    }

    fun getPreloadedPlayer(videoUrl: String): ExoPlayer? {
        return if (videoUrl == preloadedVideoUrl) {
            preloadedPlayer.also {
                preloadedPlayer = null
                preloadedVideoUrl = null
            }
        } else null
    }
}

// 2. In HomeScreen, preload next video when page settles
LaunchedEffect(pagerState.settledPage) {
    val nextIndex = pagerState.settledPage + 1
    if (nextIndex < videos.itemCount) {
        videos[nextIndex]?.videoUrl?.let { url ->
            videoPreloader.preloadNext(url)
        }
    }
}
```

### Tasks
- [x] Create `VideoPreloader.kt`
- [x] Add preloader to DI module
- [x] Integrate with `HomeScreen` VerticalPager
- [x] Handle preloader lifecycle (release on destroy)
- [ ] Test swipe performance improvement

---

## Phase 3: View Hierarchy Optimization

### Goal
Reduce View depth in video items to improve rendering performance.

### Files to Analyze/Modify
- `app/src/main/res/layout/video_item_view_v2.xml`
- `common/src/main/res/layout/view_video_controller_overlay.xml`

### Implementation
1. **Audit current hierarchy** using Layout Inspector
2. **Replace nested LinearLayouts** with ConstraintLayout
3. **Use merge tags** where possible
4. **Remove unnecessary wrapper views**

### Current Structure (video_item_view_v2.xml)
```
FrameLayout
├── PlayerView
├── ImageView (thumbnail)
├── ImageView (play/pause)
├── LinearLayout (side menu)      <- Can flatten
│   ├── VideoActionButton
│   ├── VideoActionButton
│   ├── VideoActionButton
│   └── VideoActionButton
├── ConstraintLayout (footer)
│   ├── ImageView (avatar)
│   ├── TextView (name)
│   ├── TextView (subscribe)
│   ├── TextView (desc)
│   └── TextView (tag)
├── VideoControllerOverlay
└── LinearLayout (contest)        <- Can flatten
```

### Optimized Structure
```
ConstraintLayout (single root)
├── PlayerView
├── ImageView (thumbnail)
├── ImageView (play/pause)
├── VideoActionButton x4 (direct children, use chains)
├── ImageView (avatar)
├── TextView (name)
├── TextView (subscribe)
├── TextView (desc)
├── TextView (tag)
├── VideoControllerOverlay
└── Contest views (use Group for visibility)
```

### Tasks
- [ ] Profile current layout with Layout Inspector
- [ ] Convert `video_item_view_v2.xml` to flat ConstraintLayout
- [ ] Use ConstraintLayout chains for action buttons
- [ ] Use ConstraintLayout Groups for visibility toggling
- [ ] Measure frame render time before/after

---

## Phase 4: Async View Loading

### Goal
Load heavy views in background thread to prevent main thread blocking during scroll.

### Files to Create/Modify
- `app/src/main/java/com/flick/app/ui/screens/show/VideoAdapter.kt`
- `app/src/main/java/com/flick/app/ui/screens/show/core/ViewPreloader.kt` (new)

### Implementation
```kotlin
// 1. AsyncLayoutInflater for view creation
class ViewPreloader(private val context: Context) {
    private val asyncInflater = AsyncLayoutInflater(context)
    private val preloadedViews = mutableMapOf<Int, View>()

    fun preloadView(@LayoutRes layoutId: Int, parent: ViewGroup) {
        asyncInflater.inflate(layoutId, parent) { view, _, _ ->
            preloadedViews[layoutId] = view
        }
    }

    fun getPreloadedView(@LayoutRes layoutId: Int): View? {
        return preloadedViews.remove(layoutId)
    }
}

// 2. In RecyclerView.Adapter
override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    // Try to get preloaded view first
    val view = viewPreloader.getPreloadedView(R.layout.video_item_view_v2)
        ?: LayoutInflater.from(parent.context)
            .inflate(R.layout.video_item_view_v2, parent, false)

    // Preload next view in background
    viewPreloader.preloadView(R.layout.video_item_view_v2, parent)

    return VideoViewHolder(view)
}
```

### Tasks
- [ ] Create `ViewPreloader.kt` using AsyncLayoutInflater
- [ ] Integrate with VideoAdapter
- [ ] Preload views during idle time
- [ ] Test scroll smoothness improvement

---

## Phase 5: Frame Distribution

### Goal
Distribute heavy tasks across multiple frames to prevent jank.

### Files to Create/Modify
- `app/src/main/java/com/flick/app/ui/screens/show/core/FrameScheduler.kt` (new)

### Implementation
```kotlin
// Choreographer-based frame scheduler
class FrameScheduler {
    private val choreographer = Choreographer.getInstance()
    private val pendingTasks = ConcurrentLinkedQueue<() -> Unit>()
    private val maxTasksPerFrame = 2

    fun scheduleTask(task: () -> Unit) {
        pendingTasks.add(task)
        scheduleFrame()
    }

    private fun scheduleFrame() {
        choreographer.postFrameCallback {
            repeat(maxTasksPerFrame) {
                pendingTasks.poll()?.invoke()
            }
            if (pendingTasks.isNotEmpty()) {
                scheduleFrame()
            }
        }
    }
}

// Usage in VideoAdapter.onBindViewHolder
frameScheduler.scheduleTask {
    holder.loadThumbnail(video.thumbnailUrl)
}
frameScheduler.scheduleTask {
    holder.setupActionButtons(video)
}
```

### Tasks
- [x] Create `FrameScheduler.kt`
- [x] Identify heavy operations in onBindViewHolder
- [x] Distribute image loading, text formatting across frames
- [ ] Measure frame times with Systrace

---

## Implementation Priority

| Phase | Impact | Effort | Priority |
|-------|--------|--------|----------|
| Phase 2: Video Pre-rendering | High | Medium | 1 |
| Phase 1: Dynamic Buffer | High | Low | 2 |
| Phase 3: View Hierarchy | Medium | Medium | 3 |
| Phase 5: Frame Distribution | Medium | Medium | 4 |
| Phase 4: Async View Loading | Low | High | 5 |

---

## Measurement & Verification

### Metrics to Track
1. **First frame time** - Time from video request to first frame displayed
2. **Swipe-to-play latency** - Time from swipe completion to video playing
3. **Frame drop rate** - Number of dropped frames during scroll
4. **Memory usage** - Peak memory during video playback

### Tools
- Android Studio Profiler (CPU, Memory, Energy)
- Systrace / Perfetto for frame analysis
- Layout Inspector for view hierarchy
- Custom logging with Timber

### Baseline Commands
```bash
# Record baseline metrics
adb shell dumpsys gfxinfo com.flick.app

# Capture systrace
python systrace.py -o trace.html gfx view

# Memory stats
adb shell dumpsys meminfo com.flick.app
```

---

## Timeline Estimate

- Phase 1: 1 day
- Phase 2: 2 days
- Phase 3: 1 day
- Phase 4: 2 days
- Phase 5: 1 day
- Testing & refinement: 2 days

**Total: ~9 days**
