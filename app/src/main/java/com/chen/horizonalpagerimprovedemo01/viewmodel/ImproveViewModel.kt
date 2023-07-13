package com.chen.horizonalpagerimprovedemo01.viewmodel

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ImproveViewModel: ViewModel() {
    val listData = MutableStateFlow<MutableList<Int>>(mutableListOf())

    /* 记录当前页Box位置 - 带动画缓冲属性 */
    val mutableOffsetX = MutableStateFlow(Animatable(0F))
    val mutableOffsetY = MutableStateFlow(Animatable(0F))

    init {
        repeat(10) { time ->
            listData.value.add(time)
        }
    }

    /* 自增尾随 */
    fun listDataIncreasing() {
        listData.value.add(listData.value.lastIndex + 1)
    }

    /* 变更Pager内X轴位置 */
    fun changeHorizontalXValue(newXValue: Float) {
        viewModelScope.cancel()
        viewModelScope.launch(Dispatchers.Default) {
            mutableOffsetX.value.animateTo(newXValue)
        }
    }

    /* 变更Pager内Y轴位置 */
    fun changeHorizontalYValue(newYValue: Float) {
        viewModelScope.cancel()
        viewModelScope.launch(Dispatchers.Default) {
            mutableOffsetX.value.snapTo(newYValue)
        }
    }
}