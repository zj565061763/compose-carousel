package com.sd.lib.compose.carousel

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.delay

/**
 * 轮播滚动
 */
@Composable
fun <T> FCarouselList(
   /** 列表 */
   list: List<T>,
   modifier: Modifier = Modifier,
   /** 切换间隔 */
   interval: Long = 3000,
   /** 切换动画时长 */
   duration: Int = 1000,
   /** 内容 */
   content: @Composable (T) -> Unit,
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
   ).value?.let { item ->
      FCarousel(
         target = item,
         modifier = modifier,
         duration = duration,
         content = content,
      )
   }
}