package com.gmail.gerbencdg.mygoalsassistant.utils

import java.util.*

class DateUtils {

    companion object {
        fun from(year: Int, month : Int, day : Int) = GregorianCalendar(year, month, day).time.time

        fun atStartOfDay() = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }
}