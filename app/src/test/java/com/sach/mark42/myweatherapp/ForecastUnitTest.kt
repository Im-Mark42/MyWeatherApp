package com.sach.mark42.myweatherapp

import org.junit.Assert
import org.junit.Test

class ForecastUnitTest {

    @Test
    fun checkForAvgTempAlgorithm() {

        val forecastInfo = TEST_WEATHER
        val forecastSize = ForecastInfoTest().getForcastSize(forecastInfo)
        Assert.assertEquals(forecastSize, 3)
    }
}