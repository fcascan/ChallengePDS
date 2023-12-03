package com.fcascan.challengepds.dto

data class EventDTO(
    var id: String = "",
    var name: String = "",
    var timestamp: String = "",
) {
    override fun toString(): String {
        return """Event{id: $id, name: $name, timestamp: $timestamp}"""
    }
}