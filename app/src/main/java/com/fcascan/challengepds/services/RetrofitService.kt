package com.fcascan.challengepds.services

import android.util.Log
import com.fcascan.challengepds.dto.TimeDTO
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

interface RetrofitService {

    companion object {
        private const val _TAG = "FCC#RetrofitService"

        private const val BASE_URL = "https://timeapi.io"
        private var retrofitService: RetrofitService? = null
        fun getInstance() : RetrofitService {
            if (retrofitService == null) {
                Log.d("$_TAG - getInstance", "Creating Retrofit Service")
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(
                        OkHttpClient.Builder()
                            .connectTimeout(100, TimeUnit.SECONDS)
                            .readTimeout(100, TimeUnit.SECONDS).build()
                    )
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(RetrofitService::class.java)
                retrofitService = retrofit
            }
            return retrofitService!!
        }
    }

    //Public Methods:
    @GET("/api/Time/current/zone?timeZone=America/Argentina/Buenos_Aires")  //example: https://timeapi.io/api/Time/current/zone?timeZone=America/Argentina/Buenos_Aires'
    suspend fun getCurrentTime(): Response<TimeDTO>
}