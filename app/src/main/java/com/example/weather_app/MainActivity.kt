package com.example.weather_app

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.weather_app.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//
class MainActivity : AppCompatActivity() {
    private  val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        fetchweatherdata("Surat")
        Searchcity()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun Searchcity() {
        val searchview = binding.searchView

        searchview.setOnQueryTextListener(object:SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchweatherdata(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
return true
            }

        })
    }

    private fun fetchweatherdata(cityname:String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(Apiinterface::class.java)
        val response = retrofit.getweatherdata(cityname,"26d4fc83ed1b28a764bffe6dcd0660bf","metric")
        response.enqueue(object :Callback<Weather_app>{
            override fun onResponse(call: Call<Weather_app>, response: Response<Weather_app>) {
                val responseBody = response.body()
                if(response.isSuccessful()&&responseBody!=null){
                    val temperature = responseBody.main.temp.toString()
                    val humidity = responseBody.main.humidity
                    val windSpeed = responseBody.wind.speed
                    val sunrise = responseBody.sys.sunrise.toLong()
                    val sunset = responseBody.sys.sunset.toLong()

                    val sealevel = responseBody.main.pressure
                    val condition = responseBody.weather.firstOrNull()?.main?:"unknown"
                    Log.d("TAG", "onResponse: $temperature")
                    val maxtemperature = responseBody.main.temp_max
                    val mintemperature = responseBody.main.temp_min



                    binding.temprature.text="$temperature °C"
                    binding.weather.text = condition
                    binding.maxTemp.text = "MAX TEMP $maxtemperature°C"
                    binding.minTemp.text = "MIN TEMP $mintemperature°C"
                    binding.humidity.text = "$humidity"
                    binding.sunrise.text = "${time(sunrise)}"
                    binding.sunset.text= "${time(sunset)}"
                    binding.wind.text= "$windSpeed"

                    binding.sea.text = "$sealevel hPa"
                    binding.condition.text = condition
                    binding.day.text =dayname(System.currentTimeMillis())
                    binding.date.text =date()
                    binding.cityname.text = "$cityname"


                    changeaccordingtoweather(condition)








                }
            }

            private fun date(): String {
                val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                return sdf.format((Date()))
            }

            private fun time(timestamp:Long): String {
                val sdf = SimpleDateFormat("HH mm", Locale.getDefault())
                return sdf.format((Date(timestamp*1000)))
            }

            override fun onFailure(call: Call<Weather_app>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })


    }

    private fun changeaccordingtoweather(condition: String) {
        when(condition){
            "Clear sky","Sunny","Clear"->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)

            }
            "Partly Clouds","Clouds","Overcasts","Mist","Foggy","Haze"->{
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)

            }
            "Light rain","Drizzle","Moderate Rain","Showers","Heavy Rain"->{
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)

            }
            "Light Snow","Moderate Snow","Heavy Snow","Blizzards"->{
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)

            }
            else->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
        }
        binding.lottieAnimationView.playAnimation()
    }

    fun dayname(timestamp:Long):String{
        val sdf = SimpleDateFormat("EEE", Locale.getDefault())
        return sdf.format((Date()))
    }
}