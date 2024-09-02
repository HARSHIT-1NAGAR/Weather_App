package com.example.weather_app

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Apiinterface {

    @GET("weather")

    fun getweatherdata(

        @Query("q")city:String,
          @Query("appid")appid:String,
         @Query("units")units:String
    )  :Call<Weather_app>
}