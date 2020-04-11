package com.mercury.retrofitkotlintutorial.Utils

import com.mercury.retrofitkotlintutorial.Model.Data
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class RetrofitClient {
    val BASE_URL = "https://api.nasa.gov/mars-photos/"
    val retrofitService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RetrofitService::class.java)
    }
}

interface RetrofitService{
    @GET("api/v1/rovers/curiosity/photos?sol=10&api_key=DEMO_KEY")
    suspend fun getMarsData(): Data
}