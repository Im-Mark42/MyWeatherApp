package com.sach.mark42.myweatherapp

import java.text.SimpleDateFormat
import java.util.*

fun Double.kelvinToCelsius() : Int {
    return  (this - 273.15).toInt()
}

fun String.dateToDay() : String {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date = format.parse(this) ?: Date()
    val dayFormat = SimpleDateFormat("EEEE")
    return dayFormat.format(date)
}