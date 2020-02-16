package com.sach.mark42.myweatherapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
            forecastLiveData.postValue(forecastInfo)
        }
    }
}