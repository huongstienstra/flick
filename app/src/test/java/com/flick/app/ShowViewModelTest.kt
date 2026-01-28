package com.flick.app

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.flick.network.ApiResult
import com.flick.repository.VideoRepository
import com.flick.repository.model.Comment
import com.flick.app.ui.screens.show.ShowViewModel
import com.flick.app.ui.screens.show.core.video.ExoPlayerCache
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ShowViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: ShowViewModel
    private lateinit var playerCache: ExoPlayerCache
    private lateinit var repository: VideoRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        playerCache = mockk(relaxed = true)
        repository = mockk(relaxed = true)
        viewModel = ShowViewModel(playerCache, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `likeVideo updates isLikeVideo LiveData on success`() = runTest {
        // Given
        coEvery { repository.likeVideo(1L) } returns ApiResult.success(true)

        // When
        viewModel.likeVideo(1L)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(true, viewModel.isLikeVideo.value)
    }

    @Test
    fun `likeVideo updates errorMessage on error`() = runTest {
        // Given
        val error = RuntimeException("Like failed")
        coEvery { repository.likeVideo(1L) } returns ApiResult.error(error)

        // When
        viewModel.likeVideo(1L)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals("Like failed", viewModel.errorMessage.value)
    }

    @Test
    fun `postComment updates newCommentState on success`() = runTest {
        // Given
        val comment = Comment(
            id = 1L,
            content = "Test comment",
            videoId = 100L,
            date = "2024-01-01",
            repliesCount = 0,
            profileName = "User",
            profileAvatar = null,
            likeCount = 0
        )
        coEvery { repository.postComment(any(), any()) } returns ApiResult.success(comment)

        viewModel.currentVideoId = 100L

        // When
        viewModel.postComment("Test comment")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertNotNull(viewModel.newCommentState.value)
        assertEquals("Test comment", viewModel.newCommentState.value?.comment)
    }

    @Test
    fun `postComment updates errorMessage on error`() = runTest {
        // Given
        val error = RuntimeException("Comment failed")
        coEvery { repository.postComment(any(), any()) } returns ApiResult.error(error)

        viewModel.currentVideoId = 100L

        // When
        viewModel.postComment("Test comment")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals("Comment failed", viewModel.errorMessage.value)
    }

    @Test
    fun `releaseAllPlayers calls playerCache releaseAllPlayers`() {
        // When
        viewModel.releaseAllPlayers()

        // Then
        verify { playerCache.releaseAllPlayers() }
    }

    @Test
    fun `currentPlayingPosition is initialized to NO_POSITION`() {
        assertEquals(-1, viewModel.currentPlayingPosition)
    }

    @Test
    fun `currentPlaybackPosition is initialized to 0`() {
        assertEquals(0L, viewModel.currentPlaybackPosition)
    }
}
