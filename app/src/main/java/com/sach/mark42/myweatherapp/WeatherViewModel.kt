package com.sach.mark42.myweatherapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class WeatherViewModel : ViewModel() {

    private val weatherRepo by lazy {
        WeatherRepo.getInstance()
    }

    val weatherLiveData = MutableLiveData<WeatherInfo>()
    val forecastLiveData = MutableLiveData<ForecastInfo>()

    fun getWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherInfo = weatherRepo.getWeather()
            weatherLiveData.postValue(weatherInfo)
        }
    }

    fun getForecast() {
        viewModelScope.launch(Dispatchers.IO) {
            val forecastInfo = weatherRepo.getForecast()
            val today = Calendar.getInstance().time
            var max_counter = 4
            var i = 0
            val filterForecastInfo = ForecastInfo()
            if (forecastInfo != null) {
                while (i <= forecastInfo.forecast.size) {
                    val date = forecastInfo.forecast[i].dateText
                    if (date.toDate() > today) {
                        val z = i
                        var tempr = 0.0
                        var avg_counter = 0
                        for (j in z..forecastInfo.forecast.size) {
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
            }
            forecastLiveData.postValue(filterForecastInfo)
        }
    }
}