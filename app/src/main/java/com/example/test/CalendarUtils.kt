package com.example.test

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.ArrayList

//This stuff is all outside the class to make it a package?? Kotlin way of static stuff I think
lateinit var selectedDate: LocalDate

//Formats day month year
fun formattedDate(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    return date.format(formatter)
}

//Formats time, a is am/pm
fun formattedTime(time: LocalTime): String {
    val formatter = DateTimeFormatter.ofPattern("hh:mm:ss a")
    return time.format(formatter)
}

//Formats only month year
fun monthYearFromDate(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
    return date.format(formatter)
}

//Gets how many days in selected month to feed correct cell count into recyclerview
fun daysInMonthArray(date: LocalDate): ArrayList<LocalDate?> {
    val daysInMonthArray = ArrayList<LocalDate?>()

    val yearMonth = YearMonth.from(date)
    val daysInMonth = yearMonth.lengthOfMonth()

    val firstOfMonth = date.withDayOfMonth(1)
    val dayOfWeek = firstOfMonth.dayOfWeek.value
    for (i in 1..42) {
        if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
            daysInMonthArray.add(null)
        } else {
            daysInMonthArray.add(LocalDate.of(date.year, date.month, i - dayOfWeek))
        }
    }
    return daysInMonthArray
}

class CalendarUtils {
}