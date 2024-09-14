package com.sd.lib.compose.carousel

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.delay

/**
 * 轮播滚动
 */
@Composable
fun <T> FCarouselVertical(
   modifier: Modifier = Modifier,
   /** 列表 */
   list: List<T>,
   /** 切换间隔 */
   interval: Long = 3000,
   /** 切换动画时长 */
   duration: Int = 1000,
   /** 内容 */
   content: @Composable (target: T) -> Unit,
) {
   val lifecycle = LocalLifecycleOwner.current.lifecycle
   fLoopTarget(
      list = list,
      onLoop = {
         delay(interval)
         if (!lifecycle.fAtLeastState()) {
            delay(interval)
         }
      },
   ).value?.let { target ->
      FCarouselVertical(
         modifier = modifier,
         target = target,
         duration = duration,
         content = content,
      )
   }
}

/**
 * 轮播滚动
 */
@Composable
fun <T> FCarouselVertical(
   modifier: Modifier = Modifier,
   /** 目标 */
   target: T,
   /** 切换动画时长 */
   duration: Int = 1000,
   /** 内容 */
   content: @Composable (target: T) -> Unit,
) {
   AnimatedContent(
      modifier = modifier.clipToBounds(),
      targetState = target,
      label = "FCarouselVertical",
      contentAlignment = Alignment.Center,
      transitionSpec = {
         ContentTransform(
            targetContentEnter = slideIntoContainer(
               towards = AnimatedContentTransitionScope.SlideDirection.Up,
               animationSpec = tween(duration),
            ),
            initialContentExit = slideOutOfContainer(
               towards = AnimatedContentTransitionScope.SlideDirection.Up,
               animationSpec = tween(duration),
            ),
         )
      },
   ) { targetState ->
      content(targetState)
   }
}