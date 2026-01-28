package com.flick.repository

import com.flick.local.LocalComment
import com.flick.local.VideoInteractionStorage
import com.flick.network.ApiResult
import com.flick.network.api.PexelsApiService
import com.flick.network.model.pexels.PexelsUser
import com.flick.network.model.pexels.PexelsVideo
import com.flick.network.model.pexels.PexelsVideoFile
import com.flick.network.model.pexels.PexelsVideoResponse
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PexelsVideoRepositoryTest {

    private lateinit var repository: VideoRepositoryImp
    private lateinit var pexelsApiService: PexelsApiService
    private lateinit var videoInteractionStorage: VideoInteractionStorage

    @Before
    fun setup() {
        pexelsApiService = mockk()
        videoInteractionStorage = mockk(relaxed = true)
        repository = VideoRepositoryImp(pexelsApiService, videoInteractionStorage)
    }

    @Test
    fun `getVideos returns success with mapped videos`() = runTest {
        // Given
        val mockVideo = createMockPexelsVideo(id = 1L, userName = "Test User")
        val mockResponse = PexelsVideoResponse(
            page = 1,
            perPage = 10,
            totalResults = 1,
            videos = listOf(mockVideo)
        )

        coEvery { pexelsApiService.getPopularVideos(any(), any()) } returns mockResponse
        every { videoInteractionStorage.getLikedVideoIds() } returns emptySet()
        every { videoInteractionStorage.getCommentCount(any()) } returns 0

        // When
        val result = repository.getVideos(1, 10)

        // Then
        assertTrue(result is ApiResult.Success)
        val videos = (result as ApiResult.Success).data
        assertEquals(1, videos.size)
        assertEquals(1L, videos[0].id)
        assertEquals("Test User", videos[0].profileNickname)
    }

    @Test
    fun `getVideos returns error on exception`() = runTest {
        // Given
        coEvery { pexelsApiService.getPopularVideos(any(), any()) } throws RuntimeException("Network error")

        // When
        val result = repository.getVideos(1, 10)

        // Then
        assertTrue(result is ApiResult.Error)
    }

    @Test
    fun `likeVideo toggles like status`() = runTest {
        // Given
        every { videoInteractionStorage.toggleLike(1L) } returns true

        // When
        val result = repository.likeVideo(1L)

        // Then
        assertTrue(result is ApiResult.Success)
        assertEquals(true, (result as ApiResult.Success).data)
        verify { videoInteractionStorage.toggleLike(1L) }
    }

    @Test
    fun `searchVideos returns mapped results`() = runTest {
        // Given
        val mockVideo = createMockPexelsVideo(id = 2L, userName = "Search User")
        val mockResponse = PexelsVideoResponse(
            page = 1,
            perPage = 15,
            totalResults = 1,
            videos = listOf(mockVideo)
        )

        coEvery { pexelsApiService.searchVideos(any(), any(), any(), any(), any()) } returns mockResponse
        every { videoInteractionStorage.getLikedVideoIds() } returns emptySet()
        every { videoInteractionStorage.getCommentCount(any()) } returns 0

        // When
        val result = repository.searchVideos("test", 1, 15)

        // Then
        assertTrue(result is ApiResult.Success)
        val videos = (result as ApiResult.Success).data
        assertEquals(1, videos.size)
        assertEquals("Search User", videos[0].profileNickname)
    }

    @Test
    fun `getComments returns local comments`() = runTest {
        // Given
        val mockComments = listOf(
            LocalComment(1L, 100L, "Test comment", System.currentTimeMillis(), "You")
        )
        every { videoInteractionStorage.getLocalComments(100L) } returns mockComments

        // When
        val result = repository.getComments(100L, 1, 10)

        // Then
        assertTrue(result is ApiResult.Success)
        val comments = (result as ApiResult.Success).data
        assertEquals(1, comments.size)
        assertEquals("Test comment", comments[0].content)
    }

    @Test
    fun `postComment adds comment to storage`() = runTest {
        // Given
        val mockComment = LocalComment(1L, 100L, "New comment", System.currentTimeMillis(), "You")
        every { videoInteractionStorage.addComment(100L, "New comment") } returns mockComment

        // When
        val result = repository.postComment(100L, "New comment")

        // Then
        assertTrue(result is ApiResult.Success)
        val comment = (result as ApiResult.Success).data
        assertEquals("New comment", comment?.content)
    }

    @Test
    fun `isVideoLiked returns correct status`() = runTest {
        // Given
        every { videoInteractionStorage.isVideoLiked(1L) } returns true
        every { videoInteractionStorage.isVideoLiked(2L) } returns false

        // When/Then
        assertTrue(repository.isVideoLiked(1L))
        assertTrue(!repository.isVideoLiked(2L))
    }

    private fun createMockPexelsVideo(
        id: Long,
        userName: String = "Test User"
    ): PexelsVideo {
        return PexelsVideo(
            id = id,
            width = 1080,
            height = 1920,
            duration = 30,
            url = "https://pexels.com/video/$id",
            image = "https://pexels.com/image/$id.jpg",
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
}
