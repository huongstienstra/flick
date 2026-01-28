package com.flick.repository

import com.flick.network.model.pexels.PexelsUser
import com.flick.network.model.pexels.PexelsVideo
import com.flick.network.model.pexels.PexelsVideoFile
import com.flick.repository.mapping.selectBestVideoFile
import com.flick.repository.mapping.toVideoInfo
import com.flick.repository.mapping.toVideoInfoList
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class PexelsVideoMappingTest {

    @Test
    fun `toVideoInfo maps all fields correctly`() {
        // Given
        val video = createPexelsVideo(
            id = 123L,
            userName = "John Doe",
            imageUrl = "https://example.com/thumb.jpg"
        )

        // When
        val videoInfo = video.toVideoInfo(
            isFavourite = true,
            voteCount = 10,
            commentCount = 5
        )

        // Then
        assertEquals(123L, videoInfo.id)
        assertEquals("Video by John Doe", videoInfo.description)
        assertEquals("https://example.com/thumb.jpg", videoInfo.thumbnailUrl)
        assertEquals(10, videoInfo.voteCount)
        assertEquals(5, videoInfo.commentCount)
        assertEquals(123, videoInfo.profileId)
        assertEquals("John Doe", videoInfo.profileNickname)
        assertEquals(1, videoInfo.isFavourite)
    }

    @Test
    fun `toVideoInfo sets isFavourite to 0 when false`() {
        // Given
        val video = createPexelsVideo(id = 1L, userName = "User")

        // When
        val videoInfo = video.toVideoInfo(isFavourite = false)

        // Then
        assertEquals(0, videoInfo.isFavourite)
    }

    @Test
    fun `selectBestVideoFile prefers HD quality MP4`() {
        // Given
        val video = createPexelsVideoWithMultipleFiles()

        // When
        val bestFile = video.selectBestVideoFile()

        // Then
        assertNotNull(bestFile)
        assertEquals("hd", bestFile?.quality)
        assertEquals("video/mp4", bestFile?.fileType)
    }

    @Test
    fun `selectBestVideoFile falls back to SD when HD is too large`() {
        // Given
        val video = PexelsVideo(
            id = 1L,
            width = 1080,
            height = 1920,
            duration = 30,
            url = "https://pexels.com/video/1",
            image = "https://pexels.com/image/1.jpg",
            user = PexelsUser(1L, "User", "https://pexels.com/user/1"),
            videoFiles = listOf(
                PexelsVideoFile(1L, "hd", "video/mp4", 1920, 3840, 30.0, "https://link.mp4"), // Too large
                PexelsVideoFile(2L, "sd", "video/mp4", 480, 854, 30.0, "https://sd.mp4")
            ),
            videoPictures = null
        )

        // When
        val bestFile = video.selectBestVideoFile()

        // Then
        assertNotNull(bestFile)
        assertEquals("sd", bestFile?.quality)
    }

    @Test
    fun `selectBestVideoFile returns null for empty video files`() {
        // Given
        val video = PexelsVideo(
            id = 1L,
            width = 1080,
            height = 1920,
            duration = 30,
            url = "https://pexels.com/video/1",
            image = "https://pexels.com/image/1.jpg",
            user = PexelsUser(1L, "User", "https://pexels.com/user/1"),
            videoFiles = emptyList(),
            videoPictures = null
        )

        // When
        val bestFile = video.selectBestVideoFile()

        // Then
        assertNull(bestFile)
    }

    @Test
    fun `selectBestVideoFile filters by MP4 file type`() {
        // Given
        val video = PexelsVideo(
            id = 1L,
            width = 1080,
            height = 1920,
            duration = 30,
            url = "https://pexels.com/video/1",
            image = "https://pexels.com/image/1.jpg",
            user = PexelsUser(1L, "User", "https://pexels.com/user/1"),
            videoFiles = listOf(
                PexelsVideoFile(1L, "hd", "video/webm", 720, 1280, 30.0, "https://hd.webm"),
                PexelsVideoFile(2L, "sd", "video/mp4", 480, 854, 30.0, "https://sd.mp4")
            ),
            videoPictures = null
        )

        // When
        val bestFile = video.selectBestVideoFile()

        // Then
        assertNotNull(bestFile)
        assertEquals("video/mp4", bestFile?.fileType)
    }

    @Test
    fun `toVideoInfoList maps list with favourite ids`() {
        // Given
        val videos = listOf(
            createPexelsVideo(id = 1L, userName = "User1"),
            createPexelsVideo(id = 2L, userName = "User2"),
            createPexelsVideo(id = 3L, userName = "User3")
        )
        val favouriteIds = setOf(1L, 3L)

        // When
        val videoInfoList = videos.toVideoInfoList(favouriteIds = favouriteIds)

        // Then
        assertEquals(3, videoInfoList.size)
        assertEquals(1, videoInfoList[0].isFavourite) // id=1 is in favourites
        assertEquals(0, videoInfoList[1].isFavourite) // id=2 is not in favourites
        assertEquals(1, videoInfoList[2].isFavourite) // id=3 is in favourites
    }

    @Test
    fun `toVideoInfoList applies vote counts correctly`() {
        // Given
        val videos = listOf(
            createPexelsVideo(id = 1L, userName = "User1"),
            createPexelsVideo(id = 2L, userName = "User2")
        )
        val voteCounts = mapOf(1L to 100, 2L to 50)

        // When
        val videoInfoList = videos.toVideoInfoList(voteCounts = voteCounts)

        // Then
        assertEquals(100, videoInfoList[0].voteCount)
        assertEquals(50, videoInfoList[1].voteCount)
    }

    @Test
    fun `toVideoInfoList applies comment counts correctly`() {
        // Given
        val videos = listOf(
            createPexelsVideo(id = 1L, userName = "User1"),
            createPexelsVideo(id = 2L, userName = "User2")
        )
        val commentCounts = mapOf(1L to 25, 2L to 10)

        // When
        val videoInfoList = videos.toVideoInfoList(commentCounts = commentCounts)

        // Then
        assertEquals(25, videoInfoList[0].commentCount)
        assertEquals(10, videoInfoList[1].commentCount)
    }

    private fun createPexelsVideo(
        id: Long,
        userName: String,
        imageUrl: String = "https://example.com/image.jpg"
    ): PexelsVideo {
        return PexelsVideo(
            id = id,
            width = 1080,
            height = 1920,
            duration = 30,
            url = "https://pexels.com/video/$id",
            image = imageUrl,
            user = PexelsUser(
                id = id,
                name = userName,
                url = "https://pexels.com/user/$id"
            ),
            videoFiles = listOf(
                PexelsVideoFile(
                    id = id,
                    quality = "hd",
                    fileType = "video/mp4",
                    width = 720,
                    height = 1280,
                    fps = 30.0,
                    link = "https://pexels.com/video/$id.mp4"
                )
            ),
            videoPictures = null
        )
    }

    private fun createPexelsVideoWithMultipleFiles(): PexelsVideo {
        return PexelsVideo(
            id = 1L,
            width = 1080,
            height = 1920,
            duration = 30,
            url = "https://pexels.com/video/1",
            image = "https://pexels.com/image/1.jpg",
            user = PexelsUser(1L, "User", "https://pexels.com/user/1"),
            videoFiles = listOf(
                PexelsVideoFile(1L, "hd", "video/mp4", 720, 1280, 30.0, "https://hd.mp4"),
                PexelsVideoFile(2L, "sd", "video/mp4", 480, 854, 30.0, "https://sd.mp4"),
                PexelsVideoFile(3L, "hd", "video/webm", 720, 1280, 30.0, "https://hd.webm")
            ),
            videoPictures = null
        )
    }
}
