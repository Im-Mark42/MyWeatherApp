package com.sach.mark42.myweatherapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
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

        val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(WeatherViewModel::class.java)
        viewModel.getWeather()
        viewModel.getForecast()

        val forecastInfo: ForecastInfo = ForecastInfo()
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = WeatherListingAdapter(this, forecastInfo)
        recyclerView.adapter = adapter

        val rotate = RotateAnimation(
            0f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotate.duration = 1200
        rotate.repeatCount = Animation.INFINITE
        rotate.interpolator = LinearInterpolator()

        val imageView = findViewById<ImageView>(R.id.imageView)
        imageView.startAnimation(rotate)

        viewModel.weatherLiveData.observe(this, Observer {
            if (it == null) {
                errorLayout.postDelayed({
                    loadingLayout.visibility = View.GONE
                    errorLayout.visibility = View.VISIBLE
                    imageView.clearAnimation()
                }, 1000)
            } else {
                loadingLayout.visibility = View.GONE
                temperatureText.text = it.main.temp.kelvinToCelsius().toString()
                cityText.text = it.name
                mainLayout.visibility = View.VISIBLE
                recyclerView.visibility = View.VISIBLE
                imageView.clearAnimation()
            }
        })

        viewModel.forecastLiveData.observe(this, Observer {
            if (it != null) {
                forecastInfo.forecast = it.forecast
                val controller =
                    AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation)

                recyclerView.layoutAnimation = controller
                adapter.notifyDataSetChanged()
                recyclerView.scheduleLayoutAnimation()
            }
        })

        button.setOnClickListener {
            errorLayout.visibility = View.GONE
            loadingLayout.visibility = View.VISIBLE
            imageView.startAnimation(rotate)
            viewModel.getWeather()
            viewModel.getForecast()
        }
    }
}

class WeatherListingAdapter(private val context: AppCompatActivity, var forecastInfo: ForecastInfo) :
    RecyclerView.Adapter<WeatherListingAdapter.CustomViewHolder>() {

    override fun getItemCount(): Int {
        return forecastInfo.forecast.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val dayView = holder.itemView.findViewById<TextView>(R.id.dayView)
        val temperatureView = holder.itemView.findViewById<TextView>(R.id.temperatureView)

        val item = forecastInfo.forecast[position]
        dayView.text = item.dateText.dateToDay()
        temperatureView.text = item.main.temp.kelvinToCelsius().toString() + " C"
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