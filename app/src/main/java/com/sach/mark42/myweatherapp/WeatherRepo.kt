package com.sach.mark42.myweatherapp

import android.util.Log
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.UnknownHostException

class WeatherRepo {

    suspend fun getWeather() : WeatherInfo? {
        val url = "https://api.openweathermap.org/data/2.5/weather?q=Bengaluru&APPID=9b8cb8c7f11c077f8c4e217974d9ee40"
        val url2 = "https://samples.openweathermap.org/data/2.5/weather?q=Bengaluru&appid=b6907d289e10d714a6e88b30761fae22"
        var weatherInfo : WeatherInfo? = null
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        try {
            val response = okHttpClient.newCall(request).execute()
            val weather = response.body()?.string()
            weatherInfo = Gson().fromJson(weather, WeatherInfo::class.java)
            Log.v("okhttp", weather)
        } catch (e: UnknownHostException) {
            Log.v("okhttpexception", e.toString())
        } catch (e: Exception) {
            Log.v("okhttpexception2", e.toString())
        }
        return weatherInfo
    }

    suspend fun getForecast() : ForecastInfo? {
        val url2 = "https://samples.openweathermap.org/data/2.5/forecast?q=Bengaluru&appid=b6907d289e10d714a6e88b30761fae22"
        val url ="https://api.openweathermap.org/data/2.5/forecast?q=Bengaluru&APPID=9b8cb8c7f11c077f8c4e217974d9ee40"
        var forecastInfo : ForecastInfo? = null
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        try {
            val response = okHttpClient.newCall(request).execute()
            val forecast = response.body()?.string()
            forecastInfo = Gson().fromJson(forecast, ForecastInfo::class.java)
            Log.v("okhttpff", forecast)
        } catch (e: UnknownHostException) {
            Log.v("okhttpexceptionff", e.toString())
        } catch (e: Exception) {
            Log.v("okhttpexception2ff", e.toString())
        }
        return forecastInfo
    }

    companion object {
        private var instance : WeatherRepo? = null

        fun getInstance(): WeatherRepo {
            if (instance == null)
                instance = WeatherRepo()

            return instance!!
        }
    }
}