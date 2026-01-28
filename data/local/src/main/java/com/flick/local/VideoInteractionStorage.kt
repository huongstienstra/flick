package com.flick.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Local storage for video interactions (likes and comments)
 * Used when the backend doesn't support these features (e.g., Pexels API)
 */
interface VideoInteractionStorage {
    fun isVideoLiked(videoId: Long): Boolean
    fun toggleLike(videoId: Long): Boolean
    fun getLikedVideoIds(): Set<Long>
    fun getLocalComments(videoId: Long): List<LocalComment>
    fun addComment(videoId: Long, content: String): LocalComment
    fun getCommentCount(videoId: Long): Int
}

/**
 * Local comment data class
 */
data class LocalComment(
    val id: Long,
    val videoId: Long,
    val content: String,
    val createdAt: Long,
    val userName: String = "You"
)

/**
 * Implementation using SharedPreferences
 */
class VideoInteractionStorageImpl(context: Context) : VideoInteractionStorage {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )
    private val gson = Gson()

    companion object {
        private const val PREFS_NAME = "video_interactions"
        private const val KEY_LIKED_VIDEOS = "liked_videos"
        private const val KEY_COMMENTS_PREFIX = "comments_"
        private const val KEY_COMMENT_ID_COUNTER = "comment_id_counter"
    }

    override fun isVideoLiked(videoId: Long): Boolean {
        return videoId in getLikedVideoIds()
    }

    override fun toggleLike(videoId: Long): Boolean {
        val likedVideos = getLikedVideoIds().toMutableSet()
        val isNowLiked = if (videoId in likedVideos) {
            likedVideos.remove(videoId)
            false
        } else {
            likedVideos.add(videoId)
            true
        }
        saveLikedVideoIds(likedVideos)
        return isNowLiked
    }

    override fun getLikedVideoIds(): Set<Long> {
        val json = prefs.getString(KEY_LIKED_VIDEOS, null) ?: return emptySet()
        return try {
            val type = object : TypeToken<Set<Long>>() {}.type
            gson.fromJson(json, type) ?: emptySet()
        } catch (e: Exception) {
            emptySet()
        }
    }

    private fun saveLikedVideoIds(ids: Set<Long>) {
        val json = gson.toJson(ids)
        prefs.edit().putString(KEY_LIKED_VIDEOS, json).apply()
    }

    override fun getLocalComments(videoId: Long): List<LocalComment> {
        val key = KEY_COMMENTS_PREFIX + videoId
        val json = prefs.getString(key, null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<LocalComment>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun addComment(videoId: Long, content: String): LocalComment {
        val commentId = getNextCommentId()
        val comment = LocalComment(
            id = commentId,
            videoId = videoId,
            content = content,
            createdAt = System.currentTimeMillis()
        )

        val comments = getLocalComments(videoId).toMutableList()
        comments.add(0, comment) // Add to beginning

        val key = KEY_COMMENTS_PREFIX + videoId
        val json = gson.toJson(comments)
        prefs.edit().putString(key, json).apply()

        return comment
    }

    override fun getCommentCount(videoId: Long): Int {
        return getLocalComments(videoId).size
    }

    private fun getNextCommentId(): Long {
        val currentId = prefs.getLong(KEY_COMMENT_ID_COUNTER, 0)
        val nextId = currentId + 1
        prefs.edit().putLong(KEY_COMMENT_ID_COUNTER, nextId).apply()
        return nextId
    }
}
