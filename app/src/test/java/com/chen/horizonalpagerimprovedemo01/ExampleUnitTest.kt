package com.chen.horizonalpagerimprovedemo01

import android.util.Log
import org.junit.Test

import org.junit.Assert.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Calendar

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test_generate_date_list() {
        val date = Calendar.Builder().setDate(2020, 0, 1).build()
        val weeks = getCalendarsInCurrentWeek(date)

        weeks.forEachIndexed { index, dateUnit ->
            println("第${dateUnit.get(Calendar.WEEK_OF_YEAR)}周, 星期${index}, ${dateUnit.get(Calendar.YEAR)}-${dateUnit.get(Calendar.MONTH) + 1}-${dateUnit.get(Calendar.DATE)} ")
        }
    }
}

fun getCalendarsInCurrentWeek(currentDate: Calendar): List<Calendar> {
    val firstDayOfWeek = currentDate.get(Calendar.DAY_OF_WEEK) - 1
    val calendars = mutableListOf<Calendar>()
    for (i in 0..6) {
        val calendar = Calendar.getInstance()
        calendar.set(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH))
        calendar.add(Calendar.DAY_OF_MONTH, i - firstDayOfWeek)
        calendars.add(calendar)
    }
    return calendars
}