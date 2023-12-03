package com.fcascan.challengepds.services

import android.util.Log
import com.fcascan.challengepds.dto.TimeDTO
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET

interface RetrofitService {

    companion object {
        private val _TAG = "FCC#RetrofitService"
        private val BASE_URL = "http://worldclockapi.com"

        var retrofitService: RetrofitService? = null
        fun getInstance() : RetrofitService {
            if (retrofitService == null) {
                Log.d("$_TAG - getInstance", "Creating Retrofit Service")
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }

    //Public Methods:
    @GET("/api/json/est/now")
    suspend fun getCurrentTime(): Response<TimeDTO>
}