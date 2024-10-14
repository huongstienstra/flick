package com.shinlee.showplus.extension

import android.view.View
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun View.awaitPost() = suspendCancellableCoroutine<Unit> { cont ->
    post { cont.resume(Unit) }
}