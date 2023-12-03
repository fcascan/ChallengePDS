package com.fcascan.challengepds.repositories
import com.fcascan.challengepds.services.RetrofitService

class MainRepository constructor(private val retrofitService: RetrofitService) {
    private val _TAG = "FCC#MainRepository"

    //Public Methods:
    suspend fun getTime() = retrofitService.getCurrentTime()

    //TODO funciones de set y get de Room
}