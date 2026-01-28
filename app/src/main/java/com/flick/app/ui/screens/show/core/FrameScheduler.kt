package com.flick.app.ui.screens.show.core

import android.view.Choreographer
import timber.log.Timber
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Choreographer-based frame scheduler that distributes heavy tasks across multiple frames.
 * This prevents frame drops (jank) during scrolling by ensuring each frame doesn't do too much work.
 *
 * Based on TikTok's frame distribution optimization technique.
 *
 * Usage:
 * ```kotlin
 * val scheduler = FrameScheduler()
 *
 * // In onBindViewHolder, schedule heavy tasks instead of running them immediately
 * scheduler.scheduleTask { loadThumbnail(url) }
 * scheduler.scheduleTask { setupActionButtons() }
 * scheduler.scheduleTask { loadUserAvatar(url) }
 * ```
 */
class FrameScheduler(
    private val maxTasksPerFrame: Int = DEFAULT_MAX_TASKS_PER_FRAME
) {
    private val choreographer = Choreographer.getInstance()
    private val pendingTasks = ConcurrentLinkedQueue<ScheduledTask>()
    private var isFrameScheduled = false
    private var frameCount = 0L

    /**
     * Schedules a task to be executed on the next available frame.
     * Tasks are distributed across frames to prevent jank.
     *
     * @param priority Higher priority tasks are executed first (default: NORMAL)
     * @param task The task to execute
     */
    fun scheduleTask(priority: Priority = Priority.NORMAL, task: () -> Unit) {
        pendingTasks.add(ScheduledTask(priority, task))
        scheduleFrameIfNeeded()
    }

    /**
     * Schedules multiple tasks to be distributed across frames.
     *
     * @param tasks The tasks to execute
     */
    fun scheduleTasks(vararg tasks: () -> Unit) {
        tasks.forEach { task ->
            pendingTasks.add(ScheduledTask(Priority.NORMAL, task))
        }
        scheduleFrameIfNeeded()
    }

    /**
     * Schedules a high-priority task that will be executed before normal tasks.
     */
    fun scheduleHighPriority(task: () -> Unit) {
        scheduleTask(Priority.HIGH, task)
    }

    /**
     * Schedules a low-priority task that will be executed after normal tasks.
     */
    fun scheduleLowPriority(task: () -> Unit) {
        scheduleTask(Priority.LOW, task)
    }

    private fun scheduleFrameIfNeeded() {
        if (!isFrameScheduled && pendingTasks.isNotEmpty()) {
            isFrameScheduled = true
            choreographer.postFrameCallback(frameCallback)
        }
    }

    private val frameCallback = Choreographer.FrameCallback { frameTimeNanos ->
        isFrameScheduled = false
        frameCount++

        // Sort by priority and execute up to maxTasksPerFrame tasks
        val tasksToExecute = mutableListOf<ScheduledTask>()
        repeat(maxTasksPerFrame) {
            pendingTasks.poll()?.let { tasksToExecute.add(it) }
        }

        // Sort by priority (HIGH first, then NORMAL, then LOW)
        tasksToExecute.sortedByDescending { it.priority.ordinal }.forEach { scheduledTask ->
            try {
                scheduledTask.task.invoke()
            } catch (e: Exception) {
                Timber.e(e, "FrameScheduler: Error executing task")
            }
        }

        // If there are more tasks, schedule another frame
        if (pendingTasks.isNotEmpty()) {
            scheduleFrameIfNeeded()
        }

        if (frameCount % 100 == 0L) {
            Timber.d("FrameScheduler: Processed frame $frameCount, ${pendingTasks.size} tasks remaining")
        }
    }

    /**
     * Clears all pending tasks. Call when the view is being destroyed.
     */
    fun clear() {
        pendingTasks.clear()
        Timber.d("FrameScheduler: Cleared ${pendingTasks.size} pending tasks")
    }

    /**
     * Returns the number of pending tasks.
     */
    fun pendingCount(): Int = pendingTasks.size

    /**
     * Task priority levels.
     */
    enum class Priority {
        LOW,
        NORMAL,
        HIGH
    }

    private data class ScheduledTask(
        val priority: Priority,
        val task: () -> Unit
    )

    companion object {
        /**
         * Default maximum tasks to execute per frame.
         * This value is tuned to keep each frame under 16ms (60fps target).
         * Adjust based on task complexity.
         */
        private const val DEFAULT_MAX_TASKS_PER_FRAME = 2

        /**
         * Shared instance for global use.
         */
        @Volatile
        private var instance: FrameScheduler? = null

        fun getInstance(): FrameScheduler {
            return instance ?: synchronized(this) {
                instance ?: FrameScheduler().also { instance = it }
            }
        }
    }
}
