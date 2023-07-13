package com.chen.horizonalpagerimprovedemo01.view

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.chen.horizonalpagerimprovedemo01.viewmodel.WeekViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WeekScreen(
    viewModel: WeekViewModel,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val coroutineScope = rememberCoroutineScope()

    val today = viewModel.todayCalendar.collectAsState()
    val targetDay = viewModel.targetCalendar.collectAsState()

    val weekPagerState = rememberPagerState(initialPage = 1)

    /** 当前目标日期所在周历startX坐标 */
    val anchorStartXOfCurrentWeek: MutableState<Float> = remember { mutableStateOf(0f) }
    /** 当前目标日期所在周历的endY坐标 */
//    val anchorEndXOfCurrentWeek: MutableState<Float> = remember { mutableStateOf(0f) }
    /** 当前目标日期上一周所在周历startX坐标 */
    val anchorStartXOfPreviousWeek: MutableState<Float> = remember { mutableStateOf(0f) }
    /** 当前目标日期下一周所在周历的startY坐标 */
    val anchorStartXOfNextWeek: MutableState<Float> = remember { mutableStateOf(0f) }

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
                    Text(text = "Week Calendar")
                },
            )
        },
        backgroundColor = Color(0xFF3C59CF)
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValue),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(28.dp))
            targetDay.value.let {  dateUnit ->
                Text(
                    text = "${dateUnit.get(Calendar.YEAR)}-${dateUnit.get(Calendar.MONTH) + 1}-${dateUnit.get(Calendar.DATE)}",
                    fontStyle = MaterialTheme.typography.h2.fontStyle,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
            /* 周历本体 */
            HorizontalPager(
                count = 3,
                state = weekPagerState,
                userScrollEnabled = true,
                modifier = Modifier
                    .height(148.dp)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 32.dp)
            ) { page ->
                /* 周历内容 */
                when(page) {
                    0 -> {
                        /* 上一周 */
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    anchorStartXOfPreviousWeek.value = coordinates.boundsInRoot().left
                                },
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            viewModel.previousWeek.forEach { calendar ->
                                CalendarUnitComponent(
                                    calendar = calendar,
                                    enableClick = false,
                                    isTargetDate = targetDay.value.let {
                                        it.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                                        it.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                                        it.get(Calendar.DATE) == calendar.get(Calendar.DATE)
                                    }
                                ) {
                                    /* empty */
                                }
                            }
                        }
                    }
                    1 -> {
                        /* 这一周 */
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    anchorStartXOfCurrentWeek.value = coordinates.boundsInRoot().left
                                },
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            viewModel.currentWeek.forEach { calendar ->
                                CalendarUnitComponent(
                                    calendar = calendar,
                                    enableClick = true,
                                    isTargetDate = targetDay.value.let {
                                        it.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                                        it.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                                        it.get(Calendar.DATE) == calendar.get(Calendar.DATE)
                                    }
                                ) {
                                    viewModel.changeTargetCalendar(calendar)
                                }
                            }
                        }
                    }
                    2 -> {
                        /* 下一周 */
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    anchorStartXOfNextWeek.value = coordinates.boundsInRoot().left
                                },
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            viewModel.nextWeek.forEach { calendar ->
                                CalendarUnitComponent(
                                    calendar = calendar,
                                    enableClick = false,
                                    isTargetDate = targetDay.value.let {
                                        it.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                                        it.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                                        it.get(Calendar.DATE) == calendar.get(Calendar.DATE)
                                    }
                                ) {
                                    /* empty */
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    LaunchedEffect(key1 = anchorStartXOfPreviousWeek.value, key2 = anchorStartXOfCurrentWeek.value, key3 = anchorStartXOfNextWeek.value) {
        coroutineScope.launch {
            if (weekPagerState.currentPage == 1 &&
                anchorStartXOfPreviousWeek.value == anchorStartXOfCurrentWeek.value &&
                anchorStartXOfNextWeek.value == 0f) {
                /* 显示到指定日期当前星期 */
                viewModel.apply {
                    this.calibrateTargetWeeksData((this.currentWeek.first().clone() as Calendar).apply { add(Calendar.DATE, -1) }, WeekViewModel.WeekCalendarType.PreviousWeek)
                    this.calibrateTargetWeeksData((this.currentWeek.last().clone() as Calendar).apply { add(Calendar.DATE, 1) }, WeekViewModel.WeekCalendarType.NextWeek)
                }
            } else if (weekPagerState.currentPage == 0 &&
                anchorStartXOfCurrentWeek.value == 0f &&
                anchorStartXOfNextWeek.value == 0f) {
                /* 显示到指定日期上一周的星期 */
                viewModel.copyPreviousToCurrentWeek()
                weekPagerState.scrollToPage(1)
            } else if (weekPagerState.currentPage == 2 &&
                anchorStartXOfPreviousWeek.value == anchorStartXOfCurrentWeek.value &&
                anchorStartXOfPreviousWeek.value == anchorStartXOfNextWeek.value) {
                /* 显示到指定日期下一周的星期 */
                viewModel.copyNextToCurrentWeek()
                weekPagerState.scrollToPage(1)
            } else {
                /* empty */
            }
        }
    }
    DisposableEffect(key1 = weekPagerState) {
        val observer = LifecycleEventObserver { _, event ->
//            if (event == Lifecycle.Event.ON_START) {
//                coroutineScope.launch {
//                    weekPagerState.scrollToPage(1)
//                }
//            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

/**
 *  日历内容单元组件
 *  @param  calendar    日期单元
 *  @param  isTargetDate    是否为当前选择日期
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CalendarUnitComponent(
    calendar: Calendar,
    isTargetDate: Boolean,
    enableClick: Boolean,
    onClickEvent: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(30.dp, 90.dp),
        shape = RoundedCornerShape(4.dp),
        elevation = 0.dp,
        backgroundColor = if (isTargetDate) Color(0xFFE9EEFF) else Color.Transparent,
        enabled = enableClick,
        onClick = onClickEvent
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (weekSignal, date) = createRefs()
            val centerGuideline = createGuidelineFromTop(.5f)
            Text(
                text = when(calendar.get(Calendar.DAY_OF_WEEK)) {
                    1 -> { "S" }
                    2 -> { "M" }
                    3 -> { "T" }
                    4 -> { "W" }
                    5 -> { "T" }
                    6 -> { "F" }
                    7 -> { "S" }
                    else -> {"N/A"}
                },
                fontSize = 10.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                color = Color(0xFFCBCBCB),
                modifier = Modifier.constrainAs(weekSignal) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top, 6.dp)
                    end.linkTo(parent.end)
                    bottom.linkTo(centerGuideline, 10.dp)
                }
            )
            Text(
                text = calendar.get(Calendar.DATE).toString(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                color = if (isTargetDate) Color(0xFF3C59CF) else Color.White,
                modifier = Modifier.constrainAs(date) {
                    start.linkTo(parent.start)
                    top.linkTo(centerGuideline)
                    end.linkTo(parent.end)
                }
            )
        }
    }
}
