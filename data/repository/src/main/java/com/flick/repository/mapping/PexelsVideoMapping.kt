package com.flick.repository.mapping

import com.flick.network.model.pexels.PexelsVideo
import com.flick.network.model.pexels.PexelsVideoFile
import com.flick.repository.model.VideoInfo

/**
 * Extension function to convert PexelsVideo to VideoInfo
 * Selects the best quality MP4 file for playback
 */
fun PexelsVideo.toVideoInfo(
    isFavourite: Boolean = false,
    voteCount: Int = 0,
    commentCount: Int = 0
): VideoInfo {
    val bestVideoFile = selectBestVideoFile()

    return VideoInfo(
        id = this.id,
        description = "Video by ${this.user.name}",
        videoUrl = bestVideoFile?.link,
        thumbnailUrl = this.image,
        voteCount = voteCount,
        commentCount = commentCount,
        profileId = this.user.id.toInt(),
        profilePhoto = null, // Pexels doesn't provide user profile photos
        profileNickname = this.user.name,
        contestId = null,
        contestTitle = null,
        contestImage = null,
        tags = null,
        isFavourite = if (isFavourite) 1 else 0
    )
}

/**
 * Select the best quality MP4 video file
 * Priority: HD (720p) > SD (480p) > any MP4
 * We prefer HD over Full HD for faster loading on mobile
 */
fun PexelsVideo.selectBestVideoFile(): PexelsVideoFile? {
    val mp4Files = videoFiles.filter { it.fileType == "video/mp4" }

    if (mp4Files.isEmpty()) return videoFiles.firstOrNull()

    // For portrait videos (TikTok-like), prefer files with height around 720-1080
    // Sort by quality preference
    val qualityOrder = listOf("hd", "sd", "hls")

    return mp4Files
        .filter { it.height in 480..1080 }
        .maxByOrNull { file ->
            when {
                file.quality == "hd" && file.height <= 1080 -> 100
                file.quality == "sd" -> 50
                else -> 10
            }
        }
        ?: mp4Files.minByOrNull { it.height } // Fallback to smallest file
        ?: videoFiles.firstOrNull()
}

/**
 * Convert a list of PexelsVideos to VideoInfos
 */
fun List<PexelsVideo>.toVideoInfoList(
    favouriteIds: Set<Long> = emptySet(),
    voteCounts: Map<Long, Int> = emptyMap(),
    commentCounts: Map<Long, Int> = emptyMap()
): List<VideoInfo> {
    return this.map { video ->
        video.toVideoInfo(
            isFavourite = video.id in favouriteIds,
            voteCount = voteCounts[video.id] ?: 0,
            commentCount = commentCounts[video.id] ?: 0
        )
    }
}
