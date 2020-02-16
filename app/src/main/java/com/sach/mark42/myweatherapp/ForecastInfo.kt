package com.sach.mark42.myweatherapp

import com.google.gson.annotations.SerializedName

data class ForecastInfo(
    @SerializedName("list")
    var forecast: List<WeatherInfo> = listOf()
)