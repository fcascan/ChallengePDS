package com.fcascan.challengepds.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Update
import com.fcascan.challengepds.entities.EventEntity

@Dao
@RewriteQueriesToDropUnusedColumns
interface EventsDAO {
    @Query("SELECT * FROM events ORDER BY id DESC")
    fun getAllEvents(): MutableList<EventEntity?>?

    @Query("SELECT * FROM events WHERE id = :id")
    fun getEventById(id: Int): EventEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEvent(event: EventEntity?): Long

    @Update
    fun updateEvent(event: EventEntity?): Int

    @Delete
    fun deleteEvent(event: EventEntity?): Int

    @Query("DELETE FROM events")
    fun deleteAllEvents(): Int
}