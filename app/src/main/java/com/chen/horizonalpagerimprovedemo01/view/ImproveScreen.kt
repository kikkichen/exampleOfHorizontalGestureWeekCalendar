package com.chen.horizonalpagerimprovedemo01.view

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.chen.horizonalpagerimprovedemo01.viewmodel.ImproveViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

/**
 * 改善页
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImproveScreen(
    viewModel: ImproveViewModel
) {
    val pageState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    /* 列表数据流状态 */
    val listDataState = viewModel.listData.collectAsState()
    /* 记录当前页Box位置 */
    val mutableOffsetX = viewModel.mutableOffsetX.collectAsState()
    val mutableOffsetY = viewModel.mutableOffsetY.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
                title = {
                    Text(text = "Pager")
                },
                actions = {
                    Row(
                        modifier = Modifier.fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            viewModel.listDataIncreasing()
                        }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        }
                    }
                }
            )
        }
    ) { paddingValue ->
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValue)
        ) {
            HorizontalPager(
                count = listDataState.value.size,
                state = pageState,
                userScrollEnabled = true,
                modifier = Modifier.onGloballyPositioned { layoutCoordinates ->
                    viewModel.changeHorizontalYValue(layoutCoordinates.positionInRoot().y)
                }
            ) { page ->
                /* 尝试达到一种阻止右滑的效果 */
                if (pageState.currentPage > 5) {
                    LaunchedEffect(key1 = Unit, block = {
                        pageState.scrollToPage(5)
                    })
                }
                /* 单页 */
                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(mutableOffsetX.value.value.toInt(), mutableOffsetY.value.value.toInt())
                        }
                        .fillMaxSize()
                        .onGloballyPositioned { coordinates ->
                            viewModel.changeHorizontalXValue(coordinates.positionInRoot().x)
                            viewModel.changeHorizontalYValue(coordinates.positionInRoot().y)
                            Log.d(
                                "_chen",
                                "x: ${coordinates.positionInRoot().x}, y: ${coordinates.positionInRoot().y}"
                            )
                        }
                ) {
                    Text(
                        text = "No.${listDataState.value[page] + 1} page",
                        modifier = Modifier.align(Alignment.Center)
                    )
                    OutlinedButton(
                        onClick = {
                            coroutineScope.launch {
                                pageState.animateScrollToPage(page - 1)
                            }
                        },
                        modifier = Modifier
                            .padding(12.dp)
                            .align(Alignment.BottomStart),
                        enabled = page != 0
                    ) {
                        Text(text = "上一页")
                    }
                    OutlinedButton(
                        onClick = {
                            coroutineScope.launch {
                                pageState.animateScrollToPage(page + 1)
                            }
                        },
                        modifier = Modifier
                            .padding(12.dp)
                            .align(Alignment.BottomEnd),
                        enabled = page != 9
                    ) {
                        Text(text = "下一页")
                    }
                }
            }
        }
    }
}