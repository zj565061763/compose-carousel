package com.sd.lib.compose.carousel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume

@Composable
internal fun <T> fLoopItem(
   list: List<T>,
   onLoop: suspend () -> Unit,
): State<T?> {
   return remember {
      mutableStateOf(list.firstOrNull())
   }.also { state ->
      if (list.isEmpty()) {
         state.value = null
      } else {
         val listUpdated by rememberUpdatedState(list)
         val onLoopUpdated by rememberUpdatedState(onLoop)
         LaunchedEffect(Unit) {
            var index = 0
            while (true) {
               onLoopUpdated()
               index = (index + 1).takeIf { it <= listUpdated.lastIndex } ?: 0
               state.value = listUpdated.getOrNull(index)
            }
         }
      }
   }
}

internal suspend fun Lifecycle.fAtLeastState(
   state: Lifecycle.State = Lifecycle.State.STARTED,
): Boolean {
   if (currentState == Lifecycle.State.DESTROYED) throw CancellationException()
   if (currentState.isAtLeast(state)) return true
   suspendCancellableCoroutine { continuation ->
      val observer = object : LifecycleEventObserver {
         override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event.targetState >= state) {
               removeObserver(this)
               continuation.resume(Unit)
            }
         }
      }
      addObserver(observer)
      continuation.invokeOnCancellation { removeObserver(observer) }
   }
   return false
}