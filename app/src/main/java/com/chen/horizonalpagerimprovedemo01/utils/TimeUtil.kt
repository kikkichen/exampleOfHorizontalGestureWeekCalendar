package com.chen.horizonalpagerimprovedemo01.utils

import java.util.Calendar

object TimeUtil {
    /**
     *  获取指定日期的所在星期列表数据
     *  @param  currentDate 指定日期
     */
    fun getCalendarsInCurrentWeek(currentDate: Calendar): List<Calendar> {
        val firstDayOfWeek = currentDate.get(Calendar.DAY_OF_WEEK) - 1
        val calendars = mutableListOf<Calendar>()
        for (i in 0..6) {
            val calendar = Calendar.getInstance()
            calendar.set(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(
                Calendar.DAY_OF_MONTH))
            calendar.add(Calendar.DAY_OF_MONTH, i - firstDayOfWeek)
            calendars.add(calendar)
        }
        return calendars
    }
}