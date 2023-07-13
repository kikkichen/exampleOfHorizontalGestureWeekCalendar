package com.chen.horizonalpagerimprovedemo01.viewmodel

import android.os.Build
import androidx.lifecycle.ViewModel
import com.chen.horizonalpagerimprovedemo01.utils.TimeUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.Calendar

class WeekViewModel: ViewModel() {
    /** 当前日期 */
    val todayCalendar: MutableStateFlow<Calendar> = MutableStateFlow<Calendar>(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Calendar.Builder().setDate(2023, 6, 12).build()
        } else {
            Calendar.getInstance().apply {
                set(Calendar.YEAR, 2023)
                set(Calendar.MONTH, 6)
                set(Calendar.DATE, 12)
            }
        }
    )
    /** 指定日期 */
    var targetCalendar: MutableStateFlow<Calendar> = MutableStateFlow(todayCalendar.value)

    /** 上星期 */
    var previousWeek = mutableListOf<Calendar>()
    /** 这星期 */
    var currentWeek = mutableListOf<Calendar>()
    /** 下星期 */
    var nextWeek = mutableListOf<Calendar>()

    init {
        /* 在VM初始化过程对上一周、这一周、下一周的数据内容，依据今天的日期做初始化 */
        this.calibrateWeeksData(indicatorCalendar = this.todayCalendar.value)
    }

    /**
     *  变更指定日期
     *  @param  newTargetDay    选择新的指定日期
     */
    fun changeTargetCalendar(newTargetDay: Calendar) {
        this.targetCalendar.update { newTargetDay }
    }

    /**
     *  校准星期数据，并依据游标日期，修正星期列表数据
     *  @param  indicatorCalendar   游标日期，用于索引并初始化星期列表
     */
    private fun calibrateWeeksData(indicatorCalendar: Calendar) {
        this.targetCalendar.update { indicatorCalendar }
        /* 首先修正当前星期数据 */
        val newCurrentWeekList = TimeUtil.getCalendarsInCurrentWeek(this.targetCalendar.value)
        this.currentWeek.apply {
            clear()
            newCurrentWeekList.forEach {  calendar ->
                this.add(calendar)
            }
        }
        /* 依据当前星期的第一天日期， 修正上一周数据 */
        val newPreviousWeekList = TimeUtil.getCalendarsInCurrentWeek((this.currentWeek.first().clone() as Calendar).apply { add(Calendar.DATE, -1) })
        this.previousWeek.apply {
            clear()
            newPreviousWeekList.forEach { calendar ->
                this.add(calendar)
            }
        }
        /* 依据当前星期的最后一天， 修正下一周的数据 */
        val newNextWeekList = TimeUtil.getCalendarsInCurrentWeek((this.currentWeek.last().clone() as Calendar).apply { add(Calendar.DATE, 1) })
        this.nextWeek.apply {
            clear()
            newNextWeekList.forEach { calendar ->
                this.add(calendar)
            }
        }
    }

    /**
     *  校准指定周数据
     *  @param  indicatorCalendar   游标日期，用于索引并初始化星期列表
     *  @param  weekType    星期类型，内容范围是上一周、这一周、下一周
     */
    fun calibrateTargetWeeksData(
        indicatorCalendar: Calendar,
        weekType: WeekCalendarType
    ) {
        val newWeekList = TimeUtil.getCalendarsInCurrentWeek(indicatorCalendar)
        when(weekType) {
            WeekCalendarType.PreviousWeek -> {
                this.previousWeek.apply {
                    clear()
                    newWeekList.forEach { calendar ->
                        this.add(calendar)
                    }
                }
            }
            WeekCalendarType.CurrentWeek -> {
                this.currentWeek.apply {
                    clear()
                    newWeekList.forEach {  calendar ->
                        this.add(calendar)
                    }
                }
            }
            WeekCalendarType.NextWeek -> {
                this.nextWeek.apply {
                    clear()
                    newWeekList.forEach { calendar ->
                        this.add(calendar)
                    }
                }
            }
        }
    }

    /**
     *  将上一周的星期内容复制到当前周
     */
    fun copyPreviousToCurrentWeek() {
        this.currentWeek.clear()
        this.previousWeek.forEach { calendar ->
            this.currentWeek.add(calendar)
        }
    }

    /**
     *  将下一周的星期内容赋值到当前周
     */
    fun copyNextToCurrentWeek() {
        this.currentWeek.clear()
        this.nextWeek.forEach { calendar ->
            this.currentWeek.add(calendar)
        }
    }

    /**
     *  上一周、这一周、下一周
     */
    enum class WeekCalendarType() {
        PreviousWeek, CurrentWeek, NextWeek
    }
}