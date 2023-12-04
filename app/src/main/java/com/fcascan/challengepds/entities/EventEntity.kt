package com.fcascan.challengepds.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "events", indices = [Index(value = ["id"], unique = true)])
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,              // "1"
    var name: String = "",          // "MainActivity - onStart()"
    var timeStamp: String = "",     // "2023-12-02T22:40-05:00"
) {
    override fun toString(): String {
        return """EventEntity{id: $id, name: $name, timeStamp: $timeStamp}"""
    }
}