package com.sd.lib.compose.carousel

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds

/**
 * 轮播滚动
 */
@Composable
fun <T> FCarousel(
   /** 目标 */
   target: T,
   modifier: Modifier = Modifier,
   /** 滚动方向 */
   towards: SlideDirection = SlideDirection.Up,
   /** 切换动画时长 */
   duration: Int = 1000,
   /** 内容 */
   content: @Composable (T) -> Unit,
) {
   AnimatedContent(
      modifier = modifier.clipToBounds(),
      targetState = target,
      label = "FCarousel",
      contentAlignment = Alignment.Center,
      transitionSpec = {
         ContentTransform(
            targetContentEnter = slideIntoContainer(
               towards = towards,
               animationSpec = tween(duration),
            ),
            initialContentExit = slideOutOfContainer(
               towards = towards,
               animationSpec = tween(duration),
            ),
         )
      },
   ) { targetState ->
      content(targetState)
   }
}