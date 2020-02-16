package com.sach.mark42.myweatherapp

import java.util.*

class ForecastInfoTest {

    fun getForcastSize(forecastInfo: ForecastInfo) : Int {
        val today = Calendar.getInstance().time
        var max_counter = 3
        var i = 0
        val filterForecastInfo = ForecastInfo()
        while (i <= forecastInfo.forecast.size) {
            val date = forecastInfo.forecast[i].dateText
            if (date.toDate() > today) {
                val z = i
                var tempr = 0.0
                var avg_counter = 0
                for (j in z until forecastInfo.forecast.size) {
                    val weatherInfo = forecastInfo.forecast[j]
                    val forecastDate = forecastInfo.forecast[j].dateText.toDate()
                    if (date.toDate() == forecastDate) {
                        tempr += weatherInfo.main.temp
                        avg_counter += 1
                        i = j
                    } else {
                        i = j
                        break
                    }
                }
                max_counter -= 1
                filterForecastInfo.forecast.add(
                    WeatherInfo(
                        main = Main(temp = tempr/avg_counter),
                        dateText = date)
                )
            } else {
                i+= 1
            }
            if (max_counter == 0 || i >= forecastInfo.forecast.size) {
                break
            }
        }
        return filterForecastInfo.forecast.size
    }
}

val TEST_WEATHER = ForecastInfo(mutableListOf(
    WeatherInfo(
        main = Main(temp = 296.6),
        dateText = "2020-02-16 03:00:00"
    ),
    WeatherInfo(
        main = Main(temp = 298.2),
        dateText = "2020-02-16 09:00:00"
    ),
    WeatherInfo(
        main = Main(temp = 298.2),
        dateText = "2020-02-17 03:00:00"
    ),
    WeatherInfo(
        main = Main(temp = 298.2),
        dateText = "2020-02-17 06:00:00"
    ),
    WeatherInfo(
        main = Main(temp = 298.2),
        dateText = "2020-02-18 03:00:00"
    ),
    WeatherInfo(
        main = Main(temp = 298.2),
        dateText = "2020-02-18 06:00:00"
    ),
    WeatherInfo(
        main = Main(temp = 298.2),
        dateText = "2020-02-19 03:00:00"
    ),
    WeatherInfo(
        main = Main(temp = 298.2),
        dateText = "2020-02-19 06:00:00"
    ),
    WeatherInfo(
        main = Main(temp = 298.2),
        dateText = "2020-02-20 03:00:00"
    ),
    WeatherInfo(
        main = Main(temp = 298.2),
        dateText = "2020-02-20 06:00:00"
    ),
    WeatherInfo(
        main = Main(temp = 298.2),
        dateText = "2020-02-21 03:00:00"
    ),
    WeatherInfo(
        main = Main(temp = 298.2),
        dateText = "2020-02-22 03:00:00"
    ))
)