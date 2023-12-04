package com.fcascan.challengepds.repositories
import com.fcascan.challengepds.entities.EventEntity
import com.fcascan.challengepds.services.RetrofitService
import com.fcascan.challengepds.dao.EventsDAO

class MainRepository constructor(
    private val retrofitService: RetrofitService,
    private val eventsDAO: EventsDAO
) {

    //Public Methods:
    suspend fun getTime() = retrofitService.getCurrentTime()

    fun getAllEvents() = eventsDAO.getAllEvents()

    fun insertEvent(event: EventEntity) = eventsDAO.insertEvent(event)

    fun updateEvent(event: EventEntity) = eventsDAO.updateEvent(event)

    fun deleteEvent(event: EventEntity) = eventsDAO.deleteEvent(event)

    fun deleteAllEvents() = eventsDAO.deleteAllEvents()
}