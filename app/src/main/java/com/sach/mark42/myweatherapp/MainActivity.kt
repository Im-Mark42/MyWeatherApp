package com.sach.mark42.myweatherapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.error_layout.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loadingLayout = findViewById<ConstraintLayout>(R.id.loading_layout)
        val errorLayout = findViewById<ConstraintLayout>(R.id.error_layout)
        val mainLayout = findViewById<ConstraintLayout>(R.id.main_layout)

        val model = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(WeatherViewModel::class.java)
        model.getWeather()
        model.getForecast()

        val forecastInfo: ForecastInfo = ForecastInfo()
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = WeatherListingAdapter(this, forecastInfo)
        recyclerView.adapter = adapter

        model.weatherLiveData.observe(this, Observer {
            if (it == null) {
                errorLayout.postDelayed({
                    loadingLayout.visibility = View.GONE
                    errorLayout.visibility = View.VISIBLE
                }, 1000)
            } else {
                loadingLayout.visibility = View.GONE
                textView2.text = it.main.temp.kelvinToCelsius().toString()
                textView4.text = it.name
                mainLayout.visibility = View.VISIBLE
                recyclerView.visibility = View.VISIBLE
            }
        })

        model.forecastLiveData.observe(this, Observer {
            if (it != null) {
                forecastInfo.forecast = it.forecast
                adapter.notifyDataSetChanged()
            }
        })

        button.setOnClickListener {
            errorLayout.visibility = View.GONE
            loadingLayout.visibility = View.VISIBLE
            model.getWeather()
            model.getForecast()
        }
    }
}

class WeatherListingAdapter(private val context: AppCompatActivity, var forecastInfo: ForecastInfo) :
    RecyclerView.Adapter<WeatherListingAdapter.CustomViewHolder>() {

    override fun getItemCount(): Int {
        return forecastInfo.forecast.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val dayView = holder.itemView.findViewById<TextView>(R.id.textView3)
        val temperatureView = holder.itemView.findViewById<TextView>(R.id.textView6)

        val item = forecastInfo.forecast[position]
        dayView.text = item.dateText.dateToDay()
        temperatureView.text = item.main.temp.kelvinToCelsius().toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater: LayoutInflater = context.getSystemService(
            Context.LAYOUT_INFLATER_SERVICE
        ) as LayoutInflater
        val rowView = inflater.inflate(R.layout.weather_list_item, parent, false)
        return CustomViewHolder(rowView)
    }

    class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)
}